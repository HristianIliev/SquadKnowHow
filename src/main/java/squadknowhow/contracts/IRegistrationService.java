package squadknowhow.contracts;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import squadknowhow.dbmodels.User;
import squadknowhow.request.models.Email;
import squadknowhow.request.models.Registration;
import squadknowhow.response.models.ResponseRegister;
import squadknowhow.response.models.ResponseUpload;

public interface IRegistrationService {
  boolean isEmailFree(String email);

  ResponseRegister attemptRegister(User newUser);

  ResponseUpload uploadImage(MultipartFile multipart, int id) throws IOException;
}
