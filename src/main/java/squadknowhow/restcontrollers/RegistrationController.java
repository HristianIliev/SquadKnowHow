package squadknowhow.restcontrollers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import squadknowhow.contracts.IRegistrationService;
import squadknowhow.dbmodels.User;
import squadknowhow.request.models.EditedUser;
import squadknowhow.response.models.ResponseRegister;
import squadknowhow.response.models.ResponseUpload;

@RestController
@RequestMapping("/api")
public class RegistrationController {
  private IRegistrationService registrationService;

  @Autowired
  public RegistrationController(IRegistrationService registrationService) {
    this.registrationService = registrationService;
  }

  @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
  @ResponseBody
  public boolean changePassword(@RequestParam("id") int id,
                                @RequestParam("newPass") String newPass) {
    return this.registrationService.changePassword(id, newPass);
  }

  @RequestMapping(value = "/changeOtherInfo", method = RequestMethod.POST)
  @ResponseBody
  public boolean changeOtherInfo(@RequestParam("id") int id,
                                 @RequestBody User newInfo) {
    System.out.println(newInfo);
    return this.registrationService.changeOtherInfo(id, newInfo);
  }

  @RequestMapping(value = "/checkIfPassIsCorrect", method = RequestMethod.GET)
  @ResponseBody
  public boolean checkIfPassIsCorrect(
          @RequestParam("id") int id,
          @RequestParam("password") String password) {
    return this.registrationService.checkIfPassIsCorrect(id, password);
  }

  @RequestMapping(value = "/areTagsCorrect", method = RequestMethod.POST)
  @ResponseBody
  public boolean areTagsCorrect(@RequestBody User tags) {
    return this.registrationService.areTagsCorrect(tags);
  }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  @ResponseBody
  public ResponseRegister register(@RequestBody User newUser) {
    return this.registrationService.attemptRegister(newUser);
  }

  @RequestMapping(value = "/checkIfEmailIsTaken", method = RequestMethod.POST)
  public boolean checkIfEmailIsTaken(@RequestParam("email") String email) {
    return this.registrationService.isEmailFree(email);
  }

  @RequestMapping(value = "/uploadProfilePicture", method = RequestMethod.POST)
  @ResponseBody
  public ResponseUpload uploadProfilePicture(
          @RequestParam("qqfile") MultipartFile file,
          @RequestParam("qquuid") String uuid,
          @RequestParam("qqfilename") String fileName,
          @RequestParam(value = "qqpartindex",
                  required = false,
                  defaultValue = "-1") int partIndex,
          @RequestParam(value = "qqtotalparts",
                  required = false,
                  defaultValue = "-1") int totalParts,
          @RequestParam(value = "qqtotalfilesize",
                  required = false,
                  defaultValue = "-1") long totalFileSize,
          @RequestParam("id") int id) throws IOException {
    return this.registrationService.uploadImage(file, id);
  }

  @RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
  public boolean forgotPassword(@RequestParam("email") String email) {
    return this.registrationService.forgotPassword(email);
  }

  @RequestMapping(value = "/reset-password", method = RequestMethod.GET)
  public boolean resetPassword(@RequestParam("password") String password,
                               @RequestParam("id") int id) {
    return this.registrationService.resetPassword(password, id);
  }

  @RequestMapping(value = "/editProfile", method = RequestMethod.POST)
  public boolean editProfile(@RequestBody EditedUser user) {
    return this.registrationService.editProfile(user);
  }
}
