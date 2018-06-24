package squadknowhow.request.models;

public class SentAdvice {
  private String topic;
  private String content;
  private String timestamp;
  private int senderId;
  private int projectId;

  public SentAdvice() {

  }

  public SentAdvice(int projectId) {
    this.setProjectId(projectId);
  }

  public SentAdvice(int projectId, String topic) {
    this.setProjectId(projectId);
    this.setTopic(topic);
  }

  /**
   * Constructor made for the unit tests.
   *
   * @param projectId projectId
   * @param topic     topic
   * @param senderId  senderId
   */
  public SentAdvice(int projectId, String topic, int senderId) {
    this.setProjectId(projectId);
    this.setTopic(topic);
    this.setSenderId(senderId);
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

  public int getProjectId() {
    return projectId;
  }

  public void setProjectId(int projectId) {
    this.projectId = projectId;
  }
}
