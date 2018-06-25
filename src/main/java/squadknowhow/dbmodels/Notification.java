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

import org.apache.tomcat.util.codec.binary.Base64;
import squadknowhow.contracts.Model;
import squadknowhow.response.models.ResponseMessageSender;
import squadknowhow.response.models.ResponseNotificationSender;

@Entity
@Table(name = "notifications")
public class Notification implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "kind")
  private String kind;
  @Column(name = "content")
  private String content;
  @Column(name = "timestamp")
  private String timestamp;
  @ManyToOne
  @JsonIgnore
  private User sender;
  @ManyToOne
  @JsonBackReference
  private User recipient;

  @JsonProperty
  public ResponseNotificationSender getNotificationSender() {
    return new ResponseNotificationSender(this.getSender().getId(),
            this.getSender().getFirstName() + " " + this.getSender().getLastName(),
            this.getSender().getImage());
  }

  @JsonProperty
  public ResponseMessageSender getNotificationRecipient() {
    return new ResponseMessageSender(this.getRecipient().getId(),
            this.getRecipient().getFirstName() + " " + this.getRecipient().getLastName());
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public User getSender() {
    return sender;
  }

  public void setSender(User sender) {
    this.sender = sender;
  }

  public User getRecipient() {
    return recipient;
  }

  public void setRecipient(User recipient) {
    this.recipient = recipient;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }

  public String getBase64Image() {
    return Base64.encodeBase64String(this.getSender().getImage());
  }
}
