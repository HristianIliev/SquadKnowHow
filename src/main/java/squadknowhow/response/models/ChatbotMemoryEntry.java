package squadknowhow.response.models;

public class ChatbotMemoryEntry {
  private String value;
  private String raw;
  private double confidence;

  public ChatbotMemoryEntry() {

  }

  public ChatbotMemoryEntry(String value, String raw, double confidence) {

  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getRaw() {
    return raw;
  }

  public void setRaw(String raw) {
    this.raw = raw;
  }

  public double getConfidence() {
    return confidence;
  }

  public void setConfidence(double confidence) {
    this.confidence = confidence;
  }
}
