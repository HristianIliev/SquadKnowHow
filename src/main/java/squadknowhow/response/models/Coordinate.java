package squadknowhow.response.models;

import java.util.Objects;

public class Coordinate {
  private double latitude;
  private double longitude;
  private String title;

  public Coordinate() {

  }

  public Coordinate(double latitude, double longitude, String title) {
    this.setLatitude(latitude);
    this.setLongitude(longitude);
    this.setTitle(title);
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coordinate that = (Coordinate) o;
    return Double.compare(that.getLatitude(), getLatitude()) == 0
            && Double.compare(that.getLongitude(), getLongitude()) == 0
            && Objects.equals(getTitle(), that.getTitle());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getLatitude(), getLongitude(), getTitle());
  }
}
