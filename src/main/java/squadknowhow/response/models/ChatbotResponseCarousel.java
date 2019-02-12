package squadknowhow.response.models;

import squadknowhow.contracts.IChatbotResponse;

import java.util.List;

public class ChatbotResponseCarousel implements IChatbotResponse {
  private List<ChatbotReplyCarousel> replies;
  private ChatbotConversation conversation;

  public ChatbotResponseCarousel() {

  }

  public ChatbotResponseCarousel(List<ChatbotReplyCarousel> replies,
                                 ChatbotConversation conversation) {
    this.setReplies(replies);
    this.setConversation(conversation);
  }

  public List<ChatbotReplyCarousel> getReplies() {
    return replies;
  }

  public void setReplies(List<ChatbotReplyCarousel> replies) {
    this.replies = replies;
  }

  public ChatbotConversation getConversation() {
    return conversation;
  }

  public void setConversation(ChatbotConversation conversation) {
    this.conversation = conversation;
  }
}
