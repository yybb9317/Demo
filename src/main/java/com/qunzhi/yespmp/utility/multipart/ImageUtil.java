package com.qunzhi.yespmp.utility.multipart;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public final class ImageUtil {

  /**
   * default compression extension
   */
  public static final String COMPRESS_FORMAT = "jpg";

  /**
   * simple compress image to file
   *
   * @param input input stream
   * @param out output file
   */
  public static void compress(InputStream input, File out) throws IOException {
    ImageWriter writer = ImageIO.getImageWritersByFormatName(COMPRESS_FORMAT).next();
    ImageWriteParam param = writer.getDefaultWriteParam();
    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    param.setCompressionQuality(0.7F);

    ImageOutputStream outputStream = new FileImageOutputStream(out);
    writer.setOutput(outputStream);
    IIOImage outputImage = new IIOImage(ImageIO.read(input), null, null);
    writer.write(null, outputImage, param);

    outputStream.close();
    writer.dispose();
  }

}
