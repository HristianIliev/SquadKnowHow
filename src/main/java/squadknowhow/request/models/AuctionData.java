package squadknowhow.request.models;

public class AuctionData {
  private String endDate;
  private String endHour;
  private int projectId;
  private double buyMeNow;

  public AuctionData() {

  }

  public AuctionData(String endDate,
                     String endHour,
                     int projectId,
                     double buyMeNow) {
    this.setEndDate(endDate);
    this.setEndHour(endHour);
    this.setProjectId(projectId);
    this.setBuyMeNow(buyMeNow);
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getEndHour() {
    return endHour;
  }

  public void setEndHour(String endHour) {
    this.endHour = endHour;
  }

  public int getProjectId() {
    return projectId;
  }

  public void setProjectId(int projectId) {
    this.projectId = projectId;
  }

  @Override
  public String toString() {
    return "AuctionData{"
            + "endDate='" + endDate + '\''
            + ", endHour='" + endHour + '\''
            + ", projectId=" + projectId
            + '}';
  }

  public double getBuyMeNow() {
    return buyMeNow;
  }

  public void setBuyMeNow(double buyMeNow) {
    this.buyMeNow = buyMeNow;
  }
}
