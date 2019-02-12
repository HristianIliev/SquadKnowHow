package squadknowhow.request.models;

public class RequestBase64 {
  private String base64;
  private String extension;

  public RequestBase64() {

  }

  public RequestBase64(String base64, String extension) {
    this.setBase64(base64);
    this.setExtension(extension);
  }

  public String getBase64() {
    return base64;
  }

  public void setBase64(String base64) {
    this.base64 = base64;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }
}
