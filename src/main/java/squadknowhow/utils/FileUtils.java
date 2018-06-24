package squadknowhow.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
  /**
   * Method that converts MultipartFile to File. Mainly used for uploading an image.
   *
   * @param multipart The multipart file
   * @return The File from the Multipart.
   * @throws IOException Could be thrown when ting in the FileOutputStream
   */
  public static File convertToFile(MultipartFile multipart) throws IOException {
    if (multipart == null) {
      return null;
    }

    File convFile = null;
    convFile = new File(multipart.getOriginalFilename());
    convFile.createNewFile();

    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(multipart.getBytes());
    fos.close();
    return convFile;
  }

  /**
   * Method that converts a file to a byte array so it can be inserted into a database.
   *
   * @param file The file to be converted
   * @return the byte array from the file.
   * @throws IOException Could be thrown while converting the file.
   */
  public static byte[] toByteArray(File file) throws IOException {
    if (file == null) {
      return null;
    }

    byte[] bytesArray = new byte[(int) file.length()];

    FileInputStream fis = new FileInputStream(file);
    fis.read(bytesArray); // read file into bytes[]
    fis.close();

    return bytesArray;
  }
}
