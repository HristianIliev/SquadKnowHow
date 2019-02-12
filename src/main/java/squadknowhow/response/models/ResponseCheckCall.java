package squadknowhow.response.models;

import java.util.Objects;

public class ResponseCheckCall {
  private int callerId;
  private String image;
  private String name;
  private String city;
  private boolean onlyAudio;

  public ResponseCheckCall(int callerId,
                           String image,
                           String name,
                           String city,
                           boolean onlyAudio) {
    this.setCallerId(callerId);
    this.setImage(image);
    this.setName(name);
    this.setCity(city);
    this.setOnlyAudio(onlyAudio);
  }

  public int getCallerId() {
    return this.callerId;
  }

  public void setCallerId(int callerId) {
    this.callerId = callerId;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public boolean isOnlyAudio() {
    return onlyAudio;
  }

  public void setOnlyAudio(boolean onlyAudio) {
    this.onlyAudio = onlyAudio;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ResponseCheckCall that = (ResponseCheckCall) o;
    return getCallerId() == that.getCallerId() &&
            isOnlyAudio() == that.isOnlyAudio() &&
            Objects.equals(getImage(), that.getImage()) &&
            Objects.equals(getName(), that.getName()) &&
            Objects.equals(getCity(), that.getCity());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCallerId(),
            getImage(),
            getName(),
            getCity(),
            isOnlyAudio());
  }
}
