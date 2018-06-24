package squadknowhow.utils.validators;

import java.util.Objects;

import squadknowhow.utils.validators.base.IValidator;

public class EmailValidator implements IValidator<String> {
  @Override
  public boolean isValid(String email) {
    return isNotEmpty(email)
            && isNotNull(email)
            && email.contains("@");
  }

  private boolean isNotEmpty(String str) {
    return !Objects.equals(str, "");
  }

  private boolean isNotNull(String str) {
    return str != null;
  }
}
