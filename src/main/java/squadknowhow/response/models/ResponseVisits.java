package squadknowhow.response.models;

import java.util.List;
import java.util.Objects;

public class ResponseVisits {
  private List<String> dates;
  private List<Integer> visits;

  public ResponseVisits(List<String> dates, List<Integer> visits) {
    this.setDates(dates);
    this.setVisits(visits);
  }

  public List<String> getDates() {
    return dates;
  }

  public void setDates(List<String> dates) {
    this.dates = dates;
  }

  public List<Integer> getVisits() {
    return visits;
  }

  public void setVisits(List<Integer> visits) {
    this.visits = visits;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ResponseVisits that = (ResponseVisits) o;

    return Objects.equals(getDates(), that.getDates()) &&
            Objects.equals(getVisits(), that.getVisits());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getDates(), getVisits());
  }
}
