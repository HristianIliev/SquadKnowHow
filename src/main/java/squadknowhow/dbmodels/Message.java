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
import squadknowhow.response.models.ResponseMessageSender;

@Entity
@Table(name = "messages")
public class Message implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "topic")
  private String topic;
  @Column(name = "content")
  private String content;
  @ManyToOne
  @JsonIgnore
  private User sender;
  @ManyToOne
  @JsonBackReference
  private User recipient;
  @Column(name = "timestamp")
  private String timestamp;
  @Column(name = "kind")
  private String kind;

  @JsonProperty
  public ResponseMessageSender getMessageSender() {
    return new ResponseMessageSender(this.getSender().getId(),
            this.getSender().getFirstName()
                    + " "
                    + this.getSender().getLastName());
  }

  @JsonProperty
  public ResponseMessageSender getMessageRecipient() {
    return new ResponseMessageSender(this.getRecipient().getId(),
            this.getRecipient().getFirstName()
                    + " "
                    + this.getRecipient().getLastName());
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
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

}
