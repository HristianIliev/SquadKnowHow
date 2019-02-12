package squadknowhow.utils.validators;

import java.util.Objects;

import squadknowhow.utils.validators.base.IValidator;

public class PasswordValidator implements IValidator<String> {
  private static final int MIN_PASSWORD_LENGTH = 8;
  private static final int MAX_PASSWORD_LENGTH = 20;

  @Override
  public boolean isValid(String password) {
    return isNotEmpty(password)
            && isNotNull(password)
            && password.length() >= MIN_PASSWORD_LENGTH
            && password.length() < MAX_PASSWORD_LENGTH;
  }

  private boolean isNotEmpty(String str) {
    return !Objects.equals(str, "");
  }

  private boolean isNotNull(String str) {
    return str != null;
  }
}
