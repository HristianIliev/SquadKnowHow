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

/**
 * Model class for 'advices' table.
 */
@Entity
@Table(name = "advices")
public class Advice implements Model {
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

  @JsonProperty
  public ResponseNotificationSender getAdviceSender() {
    return new ResponseNotificationSender(this.getSender().getId(),
            this.getSender().getFirstName() + " "
                    + this.getSender().getLastName(),
            this.getSender().getImage());
  }

  public int getId() {
    return id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(final String content) {
    this.content = content;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(final String tmestamp) {
    this.timestamp = tmestamp;
  }

  public User getSender() {
    return sender;
  }

  public void setSender(final User sender) {
    this.sender = sender;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(final Project project) {
    this.project = project;
  }
}
