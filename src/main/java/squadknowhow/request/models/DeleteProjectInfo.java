package squadknowhow.request.models;

public class DeleteProjectInfo {
  private int memberId;
  private int communication;
  private int initiative;
  private int leadership;
  private int innovation;
  private int responsibility;
  private String review;
  private double rating;
  private String activities;
  private boolean recommended;

  public int getMemberId() {
    return memberId;
  }

  public void setMemberId(int memberId) {
    this.memberId = memberId;
  }

  public int getCommunication() {
    return communication;
  }

  public void setCommunication(int communication) {
    this.communication = communication;
  }

  public int getInitiative() {
    return initiative;
  }

  public void setInitiative(int initiative) {
    this.initiative = initiative;
  }

  public int getLeadership() {
    return leadership;
  }

  public void setLeadership(int leadership) {
    this.leadership = leadership;
  }

  public int getInnovation() {
    return innovation;
  }

  public void setInnovation(int innovation) {
    this.innovation = innovation;
  }

  public int getResponsibility() {
    return responsibility;
  }

  public void setResponsibility(int responsibility) {
    this.responsibility = responsibility;
  }

  public String getReview() {
    return review;
  }

  public void setReview(String review) {
    this.review = review;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  public String getActivities() {
    return activities;
  }

  public void setActivities(String activities) {
    this.activities = activities;
  }

  public boolean isRecommended() {
    return recommended;
  }

  public void setRecommended(boolean recommended) {
    this.recommended = recommended;
  }
}
