package com.qunzhi.yespmp.utility.multipart;

import com.google.common.util.concurrent.RateLimiter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * multipart file sender for spring mvc
 */
@Slf4j
@NoArgsConstructor
public class FileSender {

  private static final int DEFAULT_BUFFER_SIZE = 8192; // ..bytes = 8KB.
  private static final long DEFAULT_EXPIRE_TIME = 604800000L; // ..ms = 1 week.
  private static final String MULTIPART_BOUNDARY = "MULTIPART_BYTERANGES";
  private static final double RATE_LIMIT = 2048000D; // ..bytes = 2MB/s.

  private Path filePath;
  private HttpServletRequest request;
  private HttpServletResponse response;

  public static FileSender from(Path path) {
    return new FileSender().setFilePath(path);
  }

  public static FileSender from(File file) {
    return new FileSender().setFilePath(file.toPath());
  }

  public static FileSender from(String uri) {
    return new FileSender().setFilePath(Paths.get(uri));
  }

  /**
   * Returns true if the given accept header accepts the given value.
   *
   * @param acceptHeader The accept header.
   * @param toAccept The value to be accepted.
   * @return True if the given accept header accepts the given value.
   */
  private static boolean accepts(String acceptHeader, String toAccept) {
    String[] acceptValues = acceptHeader.split("\\s*(,|;)\\s*");
    Arrays.sort(acceptValues);

    return Arrays.binarySearch(acceptValues, toAccept) > -1
        || Arrays.binarySearch(acceptValues, toAccept.replaceAll("/.*$", "/*")) > -1
        || Arrays.binarySearch(acceptValues, "*/*") > -1;
  }

  /**
   * Returns true if the given match header matches the given value.
   *
   * @param matchHeader The match header.
   * @param toMatch The value to be matched.
   * @return True if the given match header matches the given value.
   */
  private static boolean matches(String matchHeader, String toMatch) {
    String[] matchValues = matchHeader.split("\\s*,\\s*");
    Arrays.sort(matchValues);
    return Arrays.binarySearch(matchValues, toMatch) > -1
        || Arrays.binarySearch(matchValues, "*") > -1;
  }

  /**
   * Copy the given byte range of the given input to the given output.
   *
   * @param input The input to copy the given range to the given output for.
   * @param output The output to copy the given range from the given input for.
   * @param inputSize The input size of given input.
   * @param start Start of the byte range.
   * @param length Length of the byte range.
   * @throws IOException If something fails at I/O level.
   */
  private static void copy(RandomAccessFile input, OutputStream output, long inputSize,
      long start, long length) throws IOException {
    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    int read;

    RateLimiter limiter = RateLimiter.create(RATE_LIMIT);
    if (inputSize == length) {
      // Write full range.
      while ((read = input.read(buffer)) > 0) {
        limiter.acquire(DEFAULT_BUFFER_SIZE);
        output.write(buffer, 0, read);
        output.flush();
      }
    } else {
//        input.skip(start);
      input.seek(start);
      long toRead = length;

      while ((read = input.read(buffer)) > 0) {
        limiter.acquire(DEFAULT_BUFFER_SIZE);
        if ((toRead -= read) > 0) {
          output.write(buffer, 0, read);
          output.flush();
        } else {
          output.write(buffer, 0, (int) toRead + read);
          output.flush();
          break;
        }
      }
    }
  }

  private static long subLong(String value, int beginIndex, int endIndex) {
    String substring = value.substring(beginIndex, endIndex);
    return (substring.length() > 0) ? Long.parseLong(substring) : -1;
  }

  //** internal setter **//
  private FileSender setFilePath(Path filePath) {
    this.filePath = filePath;
    return this;
  }

  public FileSender with(HttpServletRequest httpRequest) {
    request = httpRequest;
    return this;
  }

  public FileSender with(HttpServletResponse httpResponse) {
    response = httpResponse;
    return this;
  }

  public void serveResource() throws IOException {
    if (response == null || request == null) {
      return;
    }

    if (!Files.exists(filePath)) {
      log.error("File doesn't exist at URI : {}", filePath.toAbsolutePath().toString());
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    String fileName = filePath.getFileName().toString();
    long length = Files.size(filePath);
    long lastModified = filePath.toFile().lastModified();

    if (StringUtils.isEmpty(fileName)) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }

    // Validate request headers for caching ---------------------------------------------------

    // If-None-Match header should contain "*" or ETag. If so, then return 304.
    String ifNoneMatch = request.getHeader("If-None-Match");
    if (ifNoneMatch != null && matches(ifNoneMatch, fileName)) {
      response.setHeader("ETag", fileName); // Required in 304.
      response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
      return;
    }

    // If-Modified-Since header should be greater than LastModified. If so, then return 304.
    // This header is ignored if any If-None-Match header is specified.
    long ifModifiedSince = request.getDateHeader("If-Modified-Since");
    if (ifNoneMatch == null && ifModifiedSince != -1 && ifModifiedSince + 1000 > lastModified) {
      response.setHeader("ETag", fileName); // Required in 304.
      response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
      return;
    }

    // Validate request headers for resume ----------------------------------------------------

    // If-Match header should contain "*" or ETag. If not, then return 412.
    String ifMatch = request.getHeader("If-Match");
    if (ifMatch != null && !matches(ifMatch, fileName)) {
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
      return;
    }

    // If-Unmodified-Since header should be greater than LastModified. If not, then return 412.
    long ifUnmodifiedSince = request.getDateHeader("If-Unmodified-Since");
    if (ifUnmodifiedSince != -1 && ifUnmodifiedSince + 1000 <= lastModified) {
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
      return;
    }

    // Validate and process range -------------------------------------------------------------

    // Prepare some variables. The full Range represents the complete file.
    Range full = new Range(0, length - 1, length);
    List<Range> ranges = new ArrayList<>();

    // Validate and process Range and If-Range headers.
    String range = request.getHeader("Range");
    if (range != null) {

      // Range header should match format "bytes=n-n,n-n,n-n...". If not, then return 416.
      if (!range.matches("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")) {
        response.setHeader("Content-Range", "bytes */" + length); // Required in 416.
        response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
        return;
      }

      String ifRange = request.getHeader("If-Range");
      if (ifRange != null && !ifRange.equals(fileName)) {
        try {
          long ifRangeTime = request.getDateHeader("If-Range"); // Throws IAE if invalid.
          if (ifRangeTime != -1) {
            ranges.add(full);
          }
        } catch (IllegalArgumentException ignore) {
          ranges.add(full);
        }
      }

      // If any valid If-Range header, then process each part of byte range.
      if (ranges.isEmpty()) {
        for (String part : range.substring(6).split(",")) {
          // Assuming a file with length of 100, the following examples returns bytes at:
          // 50-80 (50 to 80), 40- (40 to length=100), -20 (length-20=80 to length=100).
          long start = subLong(part, 0, part.indexOf("-"));
          long end = subLong(part, part.indexOf("-") + 1, part.length());

          if (start == -1) {
            start = length - end;
            end = length - 1;
          } else if (end == -1 || end > length - 1) {
            end = length - 1;
          }

          // Check if Range is syntactically valid. If not, then return 416.
          if (start > end) {
            response.setHeader("Content-Range", "bytes */" + length); // Required in 416.
            response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            return;
          }

          // Add range.
          ranges.add(new Range(start, end, length));
        }
      }
    }

    // Prepare and initialize response --------------------------------------------------------

    // Get content type by file name and set content disposition.
    String contentType = Files.probeContentType(filePath);
    String disposition = "inline";

    // If content type is unknown, then set the default value.
    if (contentType == null) {
      contentType = "application/octet-stream";
    } else if (!contentType.startsWith("image")) {
      // Else, expect for images, determine content disposition. If content type is supported by
      // the browser, then set to inline, else attachment which will pop a 'save as' dialogue.
      String accept = request.getHeader("Accept");
      disposition = accept != null && accepts(accept, contentType) ? "inline" : "attachment";
    }
    log.debug("Content-Type : {}", contentType);

    // Initialize response.
    response.reset();
    response.setBufferSize(DEFAULT_BUFFER_SIZE);
    response.setHeader("Content-Type", contentType);
    response.setHeader("Content-Disposition", disposition + ";filename=\"" + fileName + "\"");
    log.debug("Content-Disposition : {}", disposition);
    response.setHeader("Accept-Ranges", "bytes");
    response.setHeader("ETag", fileName);
    response.setDateHeader("Last-Modified", lastModified);
    response.setDateHeader("Expires", System.currentTimeMillis() + DEFAULT_EXPIRE_TIME);

    // Send requested file (part(s)) to client ------------------------------------------------

    // Prepare streams.
    try (RandomAccessFile input = new RandomAccessFile(filePath.toFile(), "r");
        OutputStream output = response.getOutputStream()) {

      // set duration for audio/video
//      if (contentType.startsWith("video") || contentType.startsWith("audio")) {
//        IContainer container = IContainer.make();
//        int result0 = container.open(new RandomAccessFile(filePath.toFile(), "r"), IContainer.Type.READ, null);
////        int result = container.open(input, IContainer.Type.READ, null);
//        long duration = container.getDuration();
//        response.setHeader("X-Content-Duration", String.valueOf(duration));
//      }

      if (ranges.isEmpty() || ranges.get(0) == full) {

        // Return full file.
        log.info("Return full file");
        response.setContentType(contentType);
        response
            .setHeader("Content-Range", "bytes " + full.start + "-" + full.end + "/" + full.total);
        response.setHeader("Content-Length", String.valueOf(full.length));
        copy(input, output, length, full.start, full.length);

      } else if (ranges.size() == 1) {

        // Return single part of file.
        Range r = ranges.get(0);
        log.info("Return 1 part of file : from ({}) to ({})", r.start, r.end);
        response.setContentType(contentType);
        response.setHeader("Content-Range", "bytes " + r.start + "-" + r.end + "/" + r.total);
        response.setHeader("Content-Length", String.valueOf(r.length));
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.

        // Copy single part range.
        copy(input, output, length, r.start, r.length);

      } else {

        // Return multiple parts of file.
        response.setContentType("multipart/byteranges; boundary=" + MULTIPART_BOUNDARY);
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.

        // Cast back to ServletOutputStream to get the easy println methods.
        ServletOutputStream sos = (ServletOutputStream) output;

        // Copy multi part range.
        for (Range r : ranges) {
          log.info("Return multi part of file : from ({}) to ({})", r.start, r.end);
          // Add multipart boundary and header fields for every range.
          sos.println();
          sos.println("--" + MULTIPART_BOUNDARY);
          sos.println("Content-Type: " + contentType);
          sos.println("Content-Range: bytes " + r.start + "-" + r.end + "/" + r.total);

          // Copy single part range of multi part range.
          copy(input, output, length, r.start, r.length);
        }

        // End with multipart boundary.
        sos.println();
        sos.println("--" + MULTIPART_BOUNDARY + "--");
      }
    }

  }

  private static class Range {

    long start;
    long end;
    long length;
    long total;

    /**
     * Construct a byte range.
     *
     * @param start Start of the byte range.
     * @param end End of the byte range.
     * @param total Total length of the byte source.
     */
    public Range(long start, long end, long total) {
      this.start = start;
      this.end = end;
      this.length = end - start + 1;
      this.total = total;
    }

  }
}
