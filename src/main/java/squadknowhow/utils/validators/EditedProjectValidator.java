package squadknowhow.utils.validators;

import java.util.Objects;

import squadknowhow.request.models.EditedProject;
import squadknowhow.utils.validators.base.IValidator;

public class EditedProjectValidator implements IValidator<EditedProject> {
  private static final int MIN_NAME_LENGTH = 5;
  private static final int MIN_DESCRIPTION_LENGTH = 150;

  @Override
  public boolean isValid(EditedProject editedProject) {
    return editedProject != null
            && isNameValid(editedProject.getName())
            && isDescriptionValid(editedProject.getDescription())
            && isNotEmpty(editedProject.getGoal1())
            && isNotNull(editedProject.getGoal1());
  }

  private boolean isDescriptionValid(String description) {
    return isNotEmpty(description)
            && isNotNull(description)
            && description.length() >= MIN_DESCRIPTION_LENGTH;
  }

  private boolean isNameValid(String name) {
    return isNotEmpty(name)
            && isNotNull(name)
            && name.length() >= MIN_NAME_LENGTH;
  }

  private boolean isNotEmpty(String str) {
    return !Objects.equals(str, "");
  }

  private boolean isNotNull(String str) {
    return str != null;
  }
}
