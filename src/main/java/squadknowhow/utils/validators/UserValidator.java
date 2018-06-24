package squadknowhow.utils.validators;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import squadknowhow.dbmodels.User;
import squadknowhow.utils.validators.base.IValidator;

public class UserValidator implements IValidator<User> {
  private static final int MIN_NAME_LENGTH = 2;
  private static final int MAX_NAME_LENGTH = 16;
  private static final int MIN_DESCRIPTION_LENGTH = 25;

  private final IValidator<String> passwordValidator;
  private final IValidator<String> emailValidator;

  @Autowired
  public UserValidator(@Qualifier("PasswordValidator") IValidator<String> passwordValidator,
                       @Qualifier("EmailValidator") IValidator<String> emailValidator) {
    this.passwordValidator = passwordValidator;
    this.emailValidator = emailValidator;
  }

  @Override
  public boolean isValid(User user) {
    return user != null
            && isNameValid(user.getFirstName())
            && isNameValid(user.getLastName())
            && this.emailValidator.isValid(user.getEmail())
            && this.passwordValidator.isValid(user.getPassword())
            && isDescriptionValid(user.getDescription());
  }

  private boolean isDescriptionValid(String description) {
    return isNotEmptyOrNull(description)
            && description.length() >= MIN_DESCRIPTION_LENGTH;
  }

  private boolean isNameValid(String name) {
    return isNotEmptyOrNull(name)
            && name.length() >= MIN_NAME_LENGTH
            && name.length() < MAX_NAME_LENGTH;
  }

  private boolean isNotEmptyOrNull(String str) {
    return isNotEmpty(str) && isNotNull(str);
  }

  private boolean isNotEmpty(String str) {
    return !Objects.equals(str, "");
  }

  private boolean isNotNull(String str) {
    return str != null;
  }
}
