package squadknowhow.response.models;

public class ChatbotConversation {
  private String language;
  private ChatbotMemory memory;

  public ChatbotConversation() {

  }

  public ChatbotConversation(String language, ChatbotMemory memory) {
    this.setLanguage(language);
    this.setMemory(memory);
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public ChatbotMemory getMemory() {
    return memory;
  }

  public void setMemory(ChatbotMemory memory) {
    this.memory = memory;
  }
}
