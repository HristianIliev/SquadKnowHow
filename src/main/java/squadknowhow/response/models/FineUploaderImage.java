package squadknowhow.response.models;

import java.util.Objects;

public class FineUploaderImage {
  private String name;
  private String uuid;
  private String thumbnailUrl;
  private long size;
  private String deleteFileEndpoint;
  private FileUploaderParameters deleteFileParams;

  public FineUploaderImage() {

  }

  public FineUploaderImage(String name,
                           String uuid,
                           String thumbnailUrl,
                           long size,
                           String deleteFileEndpoint,
                           FileUploaderParameters deleteFileParams) {
    this.setName(name);
    this.setUuid(uuid);
    this.setThumbnailUrl(thumbnailUrl);
    this.setSize(size);
    this.setDeleteFileEndpoint(deleteFileEndpoint);
    this.setDeleteFileParams(deleteFileParams);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getThumbnailUrl() {
    return thumbnailUrl;
  }

  public void setThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public String getDeleteFileEndpoint() {
    return deleteFileEndpoint;
  }

  public void setDeleteFileEndpoint(String deleteFileEndpoint) {
    this.deleteFileEndpoint = deleteFileEndpoint;
  }

  public FileUploaderParameters getDeleteFileParams() {
    return deleteFileParams;
  }

  public void setDeleteFileParams(FileUploaderParameters deleteFileParams) {
    this.deleteFileParams = deleteFileParams;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FineUploaderImage that = (FineUploaderImage) o;
    return Objects.equals(getName(), that.getName())
            && Objects.equals(getThumbnailUrl(),
            that.getThumbnailUrl())
            && Objects.equals(getDeleteFileEndpoint(),
            that.getDeleteFileEndpoint())
            && Objects.equals(getDeleteFileParams(),
            that.getDeleteFileParams());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(),
            getThumbnailUrl(),
            getSize(),
            getDeleteFileEndpoint(),
            getDeleteFileParams());
  }
}
