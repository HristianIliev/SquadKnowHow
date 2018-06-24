package squadknowhow.request.models;

public class SentQuestion {
  private String topic;
  private String content;
  private String timestamp;
  private int senderId;
  private int projectId;

  public SentQuestion() {

  }

  public SentQuestion(int projectId) {
    this.setProjectId(projectId);
  }

  public SentQuestion(int projectId, int senderId) {
    this.setProjectId(projectId);
    this.setSenderId(senderId);
  }

  /**
   * Constructor made for the unit tests.
   *
   * @param projectId projectId
   * @param senderId  senderId
   * @param topic     topic
   */
  public SentQuestion(int projectId, int senderId, String topic) {
    this.setProjectId(projectId);
    this.setSenderId(senderId);
    this.setTopic(topic);
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
