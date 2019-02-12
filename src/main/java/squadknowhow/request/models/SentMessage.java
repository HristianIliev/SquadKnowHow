package squadknowhow.request.models;

public class SentMessage {
  private String topic;
  private String content;
  private String timestamp;
  private int senderId;
  private int recipientId;
  private String kind;

  public SentMessage() {

  }

  public SentMessage(String topic) {
    this.setTopic(topic);
  }

  public SentMessage(String topic, String content) {
    this.setTopic(topic);
    this.setContent(content);
  }

  public SentMessage(int senderId) {
    this.setSenderId(senderId);
  }

  public SentMessage(int senderId, int recipientId) {
    this.setSenderId(senderId);
    this.setRecipientId(recipientId);
  }

  /**
   * Constructor made for the unit tests.
   * @param senderId senderId
   * @param recipientId recipientId
   * @param topic topic
   */
  public SentMessage(int senderId, int recipientId, String topic) {
    this.setSenderId(senderId);
    this.setRecipientId(recipientId);
    this.setTopic(topic);
  }

  @Override
  public String toString() {
    return "SentMessage [topic=" + topic
            + ", content=" + content
            + ", timestamp=" + timestamp + ", senderId="
            + senderId + ", recipientId=" + recipientId + "]";
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

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public int getSenderId() {
    return senderId;
  }

  public void setSenderId(int senderId) {
    this.senderId = senderId;
  }

  public int getRecipientId() {
    return recipientId;
  }

  public void setRecipientId(int recipientId) {
    this.recipientId = recipientId;
  }

  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }
}
