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
  public ResponseUpload uploadProfilePicture(@RequestParam("qqfile") MultipartFile file,
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
}
