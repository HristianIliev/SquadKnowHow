package squadknowhow.contracts;

import org.springframework.web.multipart.MultipartFile;
import squadknowhow.dbmodels.User;
import squadknowhow.request.models.EditedUser;
import squadknowhow.response.models.ResponseRegister;
import squadknowhow.response.models.ResponseUpload;

import java.io.IOException;

public interface IRegistrationService {
  boolean isEmailFree(String email);

  ResponseRegister attemptRegister(User newUser);

  ResponseUpload uploadImage(MultipartFile multipart,
                             int id) throws IOException;

  boolean forgotPassword(String email);

  boolean resetPassword(String password, int id);

  boolean editProfile(EditedUser user);

  void sendActivationEmail(String email, String activationKey);

  boolean checkActivationKey(String activationKey);

  boolean changeOtherInfo(int id, User newInfo);

  boolean checkIfPassIsCorrect(int id, String password);

  boolean changePassword(int id, String newPass);

  boolean areTagsCorrect(User tags);
}
