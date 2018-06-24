package squadknowhow.contracts;

import squadknowhow.response.models.ResponseLogin;

public interface ILoginService {
  ResponseLogin attemptLogin(String email, String password);
}
