package squadknowhow.response.models;

import squadknowhow.contracts.IChatbotResponse;

import java.util.List;

public class ChatbotResponseText implements IChatbotResponse {
  private List<ChatbotReplyText> replies;
  private ChatbotConversation conversation;

  public ChatbotResponseText() {

  }

  public ChatbotResponseText(List<ChatbotReplyText> replies,
                             ChatbotConversation conversation) {
    this.setReplies(replies);
    this.setConversation(conversation);
  }

  public List<ChatbotReplyText> getReplies() {
    return replies;
  }

  public void setReplies(List<ChatbotReplyText> replies) {
    this.replies = replies;
  }

  public ChatbotConversation getConversation() {
    return conversation;
  }

  public void setConversation(ChatbotConversation conversation) {
    this.conversation = conversation;
  }
}
