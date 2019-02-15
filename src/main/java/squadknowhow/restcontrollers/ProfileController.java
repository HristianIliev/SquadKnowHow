package squadknowhow.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import squadknowhow.contracts.IProfileService;
import squadknowhow.dbmodels.Note;
import squadknowhow.dbmodels.User;
import squadknowhow.request.models.PersonalInfo;
import squadknowhow.request.models.RequestBase64;
import squadknowhow.request.models.SentMessage;
import squadknowhow.response.models.ResponseCall;
import squadknowhow.response.models.ResponseCheckCall;
import squadknowhow.response.models.ResponsePagination;
import squadknowhow.response.models.ResponseSuccessful;
import squadknowhow.response.models.ResponseUsers;
import squadknowhow.response.models.UserInformation;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProfileController {
  private IProfileService profileService;

  @Autowired
  public ProfileController(IProfileService profileService) {
    this.profileService = profileService;
  }

  @RequestMapping(value = "/changeImage", method = RequestMethod.POST)
  @ResponseBody
  public boolean changeImage(@RequestParam("id") int id,
                             @RequestBody RequestBase64 base64) throws IOException {
    return this.profileService.changeImage(id, base64);
  }

  @RequestMapping(value = "/changePersonalInfo", method = RequestMethod.POST)
  @ResponseBody
  public boolean changePersonalInfo(@RequestParam("id") int id,
                                    @RequestBody PersonalInfo personalInfo) {
    return this.profileService.changePersonalInfo(id, personalInfo);
  }

  @RequestMapping(value = "/deleteNote", method = RequestMethod.DELETE)
  @ResponseBody
  public Note deleteNote(@RequestParam("noteId") int noteId) {
    return this.profileService.deleteNote(noteId);
  }

  @RequestMapping(value = "/getNotes", method = RequestMethod.GET)
  @ResponseBody
  public List<Note> getNotes(@RequestParam("id") int id) {
    return this.profileService.getNotes(id);
  }

  @RequestMapping(value = "/createNote", method = RequestMethod.POST)
  @ResponseBody
  public Note createNote(@RequestParam("id") int id,
                         @RequestParam("projectId") int projectId,
                         @RequestBody Note note) {
    return this.profileService.createNote(note, id, projectId);
  }

  @RequestMapping(value = "/thereIsNoCall", method = RequestMethod.GET)
  @ResponseBody
  public boolean thereIsNoCall(@RequestParam("id") int id) {
    return this.profileService.thereIsNoCall(id);
  }

  @RequestMapping(value = "/checkIfThereIsACall", method = RequestMethod.GET)
  @ResponseBody
  public ResponseCheckCall checkIfThereIsACall(@RequestParam("id") int id) {
    return this.profileService.checkIfThereIsACall(id);
  }

  @RequestMapping(value = "/checkOnline", method = RequestMethod.GET)
  @ResponseBody
  public ResponseCall checkOnline(@RequestParam("emails") String emails,
                                  @RequestParam("senderId") int senderId,
                                  @RequestParam("isAudio") boolean isAudio) {
    System.out.println(senderId);
    return this.profileService.checkIfOnline(emails, senderId, isAudio);
  }

  @RequestMapping(value = "/noMoreCalling", method = RequestMethod.GET)
  @ResponseBody
  public boolean noMoreCalling(@RequestParam("emails") String emails,
                               @RequestParam("id") int id) {
    return this.profileService.noMoreCalling(emails, id);
  }

  @RequestMapping(value = "/deleteNotification", method = RequestMethod.DELETE)
  @ResponseBody
  public ResponseSuccessful deleteNotification(@RequestParam("notificationId") int notificationId) {
    return this.profileService.deleteNotification(notificationId);
  }

  @RequestMapping(value = "/deleteNotifications", method = RequestMethod.DELETE)
  @ResponseBody
  public ResponseSuccessful deleteNotifications(@RequestParam("id") int id) {
    return this.profileService.deleteNotifications(id);
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

  @RequestMapping(value = "/checkIfUserNeedsTour", method = RequestMethod.GET)
  @ResponseBody
  public UserInformation checkIfUserNeedsTour(@RequestParam("id") int id) {
    return this.profileService.checkIfUserNeedsTour(id);
  }

  @RequestMapping(value = "/users")
  @ResponseBody
  public ResponseUsers getUsers(@RequestParam("page") int page,
                                @RequestParam("name") String name,
                                @RequestParam("userCategory") String userCategory,
                                @RequestParam("city") String city,
                                @RequestParam("skills") String skills,
                                @RequestParam("languages") String languages,
                                @RequestParam("sortBy") String sortBy) {
    System.out.println(sortBy);
    return new ResponseUsers(this.profileService.getUsers(page, name, userCategory, city, skills, languages, sortBy));
  }

  @RequestMapping(value = "/getPeoplePages")
  @ResponseBody
  public ResponsePagination getPeoplePages(@RequestParam("name") String name,
                                           @RequestParam("userCategory") String userCategory,
                                           @RequestParam("city") String city,
                                           @RequestParam("skills") String skills,
                                           @RequestParam("languages") String languages,
                                           @RequestParam("sortBy") String sortBy) {
    return this.profileService.getPeoplePages(name, userCategory, city, skills, languages, sortBy);
  }

  @RequestMapping(value = "/tourCompleted", method = RequestMethod.GET)
  public boolean tourCompleted(@RequestParam("id") int id) {
    return this.profileService.tourCompleted(id);
  }

  @RequestMapping(value = "/contact", method = RequestMethod.GET)
  public boolean contact(@RequestParam("name") String name,
                         @RequestParam("email") String email,
                         @RequestParam("subject") String subject,
                         @RequestParam("message") String message) {
    System.out.println("here");
    return this.profileService.contact(name, email, subject, message);
  }
}
