package squadknowhow.response.models;

public class FileUploaderParameters {
  private int id;
  private int pictureNum;

  public FileUploaderParameters() {

  }

  public FileUploaderParameters(int id, int pictureNum) {
    this.setId(id);
    this.setPictureNum(pictureNum);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getPictureNum() {
    return pictureNum;
  }

  public void setPictureNum(int pictureNum) {
    this.pictureNum = pictureNum;
  }
}
