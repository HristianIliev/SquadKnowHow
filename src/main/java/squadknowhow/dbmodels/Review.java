package squadknowhow.dbmodels;

import squadknowhow.contracts.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "reviews")
public class Review implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "title")
  private String title;
  @Column(name = "description")
  private String description;
  @Column(name = "activities")
  private String activities;
  @Column(name = "rating")
  private double rating;
  @ManyToOne
  private User writer;
  @ManyToOne
  private User user;
  @Column(name = "communication")
  private int communication;
  @Column(name = "initiative")
  private int initiative;
  @Column(name = "leadership")
  private int leadership;
  @Column(name = "innovation")
  private int innovation;
  @Column(name = "responsibility")
  private int responsibility;
  @Column(name = "isCompleted")
  private boolean isCompleted;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getActivities() {
    return activities;
  }

  public void setActivities(String activities) {
    this.activities = activities;
  }

  public User getWriter() {
    return writer;
  }

  public void setWriter(User writer) {
    this.writer = writer;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  public int getCommunication() {
    return communication;
  }

  public void setCommunication(int communication) {
    this.communication = communication;
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

  public int getInitiative() {
    return initiative;
  }

  public void setInitiative(int initiative) {
    this.initiative = initiative;
  }

  public boolean isCompleted() {
    return isCompleted;
  }

  public void setCompleted(boolean completed) {
    isCompleted = completed;
  }
}
