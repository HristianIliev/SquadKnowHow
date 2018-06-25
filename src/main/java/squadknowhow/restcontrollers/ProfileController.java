package squadknowhow.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import squadknowhow.contracts.IProfileService;
import squadknowhow.dbmodels.User;
import squadknowhow.request.models.EditedUser;
import squadknowhow.request.models.SentMessage;
import squadknowhow.response.models.ResponsePagination;
import squadknowhow.response.models.ResponseSuccessful;
import squadknowhow.response.models.ResponseUsers;

@RestController
@RequestMapping("/api")
public class ProfileController {
  private IProfileService profileService;

  @Autowired
  public ProfileController(IProfileService profileService) {
    this.profileService = profileService;
  }

  @RequestMapping(value = "/deleteNotification", method = RequestMethod.DELETE)
  @ResponseBody
  public ResponseSuccessful deleteNotification(@RequestParam("notificationId") int notificationId) {
    return this.profileService.deleteNotification(notificationId);
  }

  @RequestMapping(value = "/deleteMessage", method = RequestMethod.DELETE)
  @ResponseBody
  public ResponseSuccessful deleteMessage(@RequestParam("messageId") int messageId) {
    return this.profileService.deleteMessage(messageId);
  }

  @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
  @ResponseBody
  public ResponseSuccessful sendMessage(@RequestBody SentMessage message) {
    return this.profileService.sendMessage(message);
  }

  @RequestMapping(value = "/user", method = RequestMethod.GET)
  @ResponseBody
  public User getUser(@RequestParam("id") int id) {
    return this.profileService.getUserById(id);
  }

  @RequestMapping(value = "/users")
  @ResponseBody
  public ResponseUsers getUsers(@RequestParam("page") int page,
                                @RequestParam("name") String name,
                                @RequestParam("userCategory") String userCategory,
                                @RequestParam("city") String city,
                                @RequestParam("skills") String skills,
                                @RequestParam("interests") String interests) {
    return new ResponseUsers(this.profileService.getUsers(page,
            name,
            userCategory,
            city,
            skills,
            interests));
  }

  @RequestMapping(value = "/getPeoplePages")
  @ResponseBody
  public ResponsePagination getPeoplePages(@RequestParam("name") String name,
                                           @RequestParam("userCategory") String userCategory,
                                           @RequestParam("city") String city,
                                           @RequestParam("skills") String skills,
                                           @RequestParam("interests") String interests) {
    return this.profileService.getPeoplePages(name, userCategory, city, skills, interests);
  }

  @RequestMapping(value = "/editProfile", method = RequestMethod.POST)
  public boolean editProfile(@RequestBody EditedUser user) {
    return this.profileService.editProfile(user);
  }

  @RequestMapping(value = "/tourCompleted", method = RequestMethod.GET)
  public boolean tourCompleted(@RequestParam("id") int id) {
    return this.profileService.tourCompleted(id);
  }
}
