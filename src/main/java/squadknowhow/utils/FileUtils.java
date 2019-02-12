package squadknowhow.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
  public static String convertToFilepath(
          MultipartFile multipart) throws IOException {
    if (multipart == null) {
      return "";
    }

    String ext = multipart.getOriginalFilename()
            .substring(multipart.getOriginalFilename().indexOf(".") + 1);
    System.out.println(ext);

    File convFile = null;
    String path = "./src/main/webapp/static/all-images/"
            + getNameOfFile() + "." + ext;
    convFile = new File(path);
    boolean resultCreate = convFile.createNewFile();
    if (resultCreate) {
      System.out.println("File created");
    } else {
      System.out.println("File not created");
    }

    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(multipart.getBytes());
    fos.close();
    path = path.substring(path.indexOf("/static"));
    return path;
  }

  public static String convertToFilepathFromBase64(String base64Image,
                                                   String extension)
          throws IOException {
    base64Image = base64Image.substring(23);
    byte[] data = Base64.decodeBase64(base64Image);
    String path = "./src/main/webapp/static/all-images/"
            + getNameOfFile() + "." + extension;

    try (OutputStream stream = new FileOutputStream(path)) {
      stream.write(data);
    }

    path = path.substring(path.indexOf("/static"));
    return path;
  }

  private static String getNameOfFile() {
    String saltchars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    StringBuilder salt = new StringBuilder();
    Random rnd = new Random();
    while (salt.length() < 20) { // length of the random string.
      int index = (int) (rnd.nextFloat() * saltchars.length());
      salt.append(saltchars.charAt(index));
    }

    return salt.toString();
  }

  public static boolean deleteFile(String path) {
    File file = new File("./src/main/webapp" + path);

    return file.delete();
  }

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

  public static long getSizeOfFile(String path) {
    File file = new File("./src/main/webapp" + path);

    return file.length();
  }
}
