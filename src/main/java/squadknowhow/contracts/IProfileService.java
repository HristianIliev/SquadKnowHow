package squadknowhow.contracts;

import org.springframework.security.core.userdetails.UserDetailsService;
import squadknowhow.dbmodels.City;
import squadknowhow.dbmodels.Interest;
import squadknowhow.dbmodels.Note;
import squadknowhow.dbmodels.Skill;
import squadknowhow.dbmodels.User;
import squadknowhow.dbmodels.UserCategory;
import squadknowhow.dbmodels.UserShort;
import squadknowhow.request.models.PersonalInfo;
import squadknowhow.request.models.RequestBase64;
import squadknowhow.request.models.SentMessage;
import squadknowhow.response.models.ResponseCall;
import squadknowhow.response.models.ResponseCheckCall;
import squadknowhow.response.models.ResponsePagination;
import squadknowhow.response.models.ResponseSuccessful;
import squadknowhow.response.models.UserInformation;

import java.io.IOException;
import java.util.List;

public interface IProfileService extends UserDetailsService {
  User getUserById(int id);

  List<UserShort> getUsers(int page,
                           String name,
                           String userCategory,
                           String city,
                           String skills,
                           String interests, String sortBy);

  User getUserByEmail(String email);

  ResponsePagination getPeoplePages(String name,
                                    String userCategory,
                                    String city,
                                    String skills,
                                    String interests, String sortBy);

  Skill getSkill(String skillName);

  City getCity(String cityName);

  UserCategory getUserCategory(String userCategoryName);

  ResponseSuccessful sendMessage(SentMessage message);

  ResponseSuccessful deleteMessage(int messageId);

  ResponseSuccessful deleteNotification(int notificationId);

  boolean tourCompleted(int id);

  Interest getInterest(String interestName);

  UserInformation checkIfUserNeedsTour(int id);

  boolean contact(String name, String email, String subject, String message);

  void setOffline(String name);

  void setOnline(String name);

  String getUserName(int creatorId);

  ResponseCall checkIfOnline(String emails, int senderId, boolean isAudio);

  ResponseCheckCall checkIfThereIsACall(int id);

  boolean thereIsNoCall(int id);

  boolean noMoreCalling(String emails, int id);

  Note createNote(Note note, int id, int projectId);

  List<Note> getNotes(int id);

  Note deleteNote(int noteId);

  boolean changePersonalInfo(int id, PersonalInfo personalInfo);

  boolean changeImage(int id, RequestBase64 base64) throws IOException;

  ResponseSuccessful deleteNotifications(int id);

  boolean stopGithubStatistics(int id);

  boolean startGithubStatistics(int id);
}
