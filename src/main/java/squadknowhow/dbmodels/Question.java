package squadknowhow.dbmodels;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import squadknowhow.contracts.Model;
import squadknowhow.response.models.ResponseNotificationSender;

@Entity
@Table(name = "questions")
public class Question implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "title")
  private String title;
  @Column(name = "content")
  private String content;
  @Column(name = "timestamp")
  private String timestamp;
  @ManyToOne
  @JsonBackReference
  private Project project;
  @ManyToOne
  @JsonIgnore
  private User sender;

  /**
   * Method that generates the question sender into JSON.
   *
   * @return object that gets parsed to JSON.
   */
  @JsonProperty
  public ResponseNotificationSender getQuestionSender() {
    return new ResponseNotificationSender(this.getSender().getId(),
            this.getSender().getFirstName() + " " + this.getSender().getLastName(),
            this.getSender().getImage());
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String tmestamp) {
    this.timestamp = tmestamp;
  }

  public User getSender() {
    return sender;
  }

  public void setSender(User sender) {
    this.sender = sender;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }
}
