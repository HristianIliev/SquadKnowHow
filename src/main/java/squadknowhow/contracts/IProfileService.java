package squadknowhow.contracts;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import squadknowhow.dbmodels.City;
import squadknowhow.dbmodels.Interest;
import squadknowhow.dbmodels.Skill;
import squadknowhow.dbmodels.User;
import squadknowhow.dbmodels.UserCategory;
import squadknowhow.dbmodels.UserShort;
import squadknowhow.request.models.EditedUser;
import squadknowhow.request.models.SentMessage;
import squadknowhow.response.models.ResponsePagination;
import squadknowhow.response.models.ResponseSuccessful;

public interface IProfileService extends UserDetailsService {
  User getUserById(int id);

  List<UserShort> getUsers(int page,
                           String name,
                           String userCategory,
                           String city,
                           String skills,
                           String interests);

  User getUserByEmail(String email);

  ResponsePagination getPeoplePages(String name,
                                    String userCategory,
                                    String city,
                                    String skills,
                                    String interests);

  Skill getSkill(String skillName);

  City getCity(String cityName);

  UserCategory getUserCategory(String userCategoryName);

  ResponseSuccessful sendMessage(SentMessage message);

  ResponseSuccessful deleteMessage(int messageId);

  ResponseSuccessful deleteNotification(int notificationId);

  boolean editProfile(EditedUser user);

  boolean tourCompleted(int id);

  Interest getInterest(String interestName);
}
