package squadknowhow.services;

import java.security.InvalidParameterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import squadknowhow.contracts.ILoginService;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.User;
import squadknowhow.response.models.ResponseLogin;
import squadknowhow.utils.validators.base.IValidator;

@Service
public class DbLoginService implements ILoginService {
  private final IValidator<String> passwordValidator;
  private final IValidator<String> emailValidator;
  private final IRepository<User> usersRepository;

  /**
   * Service for the business login when dealing with the login.
   * @param usersRepository Repository for the users.
   * @param passwordValidator Validator for password parameters.
   * @param emailValidator Validator for email parameters.
   */
  @Autowired
  public DbLoginService(IRepository<User> usersRepository,
                        @Qualifier("PasswordValidator") IValidator<String> passwordValidator,
                        @Qualifier("EmailValidator") IValidator<String> emailValidator) {
    this.usersRepository = usersRepository;
    this.passwordValidator = passwordValidator;
    this.emailValidator = emailValidator;
  }

  @Override
  public ResponseLogin attemptLogin(String email, String password) {
    if (!this.emailValidator.isValid(email)) {
      throw new InvalidParameterException("Email is not valid");
    }

    if (!this.passwordValidator.isValid(password)) {
      throw new InvalidParameterException("Password is not valid");
    }

    User user = usersRepository.getAll().stream()
            .filter(u -> u.getEmail().equals(email))
            .findFirst()
            .orElse(null);

    if (user == null) {
      return new ResponseLogin("Invalid credetentials", -1);
    }

    if (!user.getPassword().equals(password)) {
      return new ResponseLogin("Invalid credetentials", -1);
    }

    return new ResponseLogin("Login successfull", user.getId());
  }

}
