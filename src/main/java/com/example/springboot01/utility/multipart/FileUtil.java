package com.example.springboot01.utility.multipart;

import com.google.common.collect.ImmutableSet;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public final class FileUtil {

  public static final Tika TIKA = new Tika();

  /**
   * only support image type with jpg, jpeg, png, gif, webp
   */
  public static final Set<String> IMG_EXT = ImmutableSet.of(
      "jpg", "jpeg", "png", "gif", "webp"
  );

  /**
   * support video type: mp4, flv, avi, mkv, wmv
   */
  public static final Set<String> VIDEO_EXT = ImmutableSet.of(
      "mp4", "flv", "avi", "mkv", "wmv"
  );

  /**
   * calculate file's md5
   */
  public static String md5(MultipartFile file) throws IOException {
    return Hashing.md5().hashBytes(file.getBytes()).toString();
  }

  public static String md5(File file) throws IOException {
    return Hashing.md5().hashBytes(Files.toByteArray(file)).toString();
  }

  /**
   * set real extension to filename
   *
   * @param fileName filename
   * @param extension file's real extension, lower case, e.g. .jpg, .pdf
   * @return filename with lower case extension
   */
  public static String setExt(String fileName, String extension) {
    if (extension.isEmpty()) {
      return fileName;
    }
    if (fileName.toLowerCase().endsWith(extension)) {
      return fileName.substring(0, fileName.length() - extension.length()) + extension;
    }
    return fileName + extension;
  }

}
