package squadknowhow.utils.validators;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import squadknowhow.request.models.EditedUser;
import squadknowhow.utils.validators.base.IValidator;

public class EditedUserValidator implements IValidator<EditedUser> {
  private static final int MIN_NAME_LENGTH = 2;
  private static final int MAX_NAME_LENGTH = 16;
  private static final int MIN_DESCRIPTION_LENGTH = 25;

  private final IValidator<String> passwordValidator;
  private final IValidator<String> emailValidator;

  @Autowired
  public EditedUserValidator(@Qualifier("PasswordValidator") IValidator<String> passwordValidator,
                             @Qualifier("EmailValidator") IValidator<String> emailValidator) {
    this.passwordValidator = passwordValidator;
    this.emailValidator = emailValidator;
  }

  @Override
  public boolean isValid(EditedUser editedUser) {
    return editedUser != null
            && isNameValid(editedUser.getFirstName())
            && isNameValid(editedUser.getLastName())
            && isDescriptionValid(editedUser.getDescription())
            && this.passwordValidator.isValid(editedUser.getPassword())
            && this.emailValidator.isValid(editedUser.getEmail());
  }

  private boolean isNameValid(String name) {
    return isNotEmpty(name)
            && isNotNull(name)
            && name.length() >= MIN_NAME_LENGTH
            && name.length() < MAX_NAME_LENGTH;
  }

  private boolean isDescriptionValid(String description) {
    return isNotEmpty(description)
            && isNotNull(description)
            && description.length() >= MIN_DESCRIPTION_LENGTH;
  }

  private boolean isNotEmpty(String str) {
    return !Objects.equals(str, "");
  }

  private boolean isNotNull(String str) {
    return str != null;
  }
}
