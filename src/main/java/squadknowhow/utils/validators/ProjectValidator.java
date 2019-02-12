package squadknowhow.utils.validators;

import squadknowhow.request.models.CreateProject;
import squadknowhow.utils.validators.base.IValidator;

import java.util.Objects;

public class ProjectValidator implements IValidator<CreateProject> {
  private static final int MIN_NAME_LENGTH = 5;
  private static final int MIN_DESCRIPTION_LENGTH = 150;

  @Override
  public boolean isValid(CreateProject project) {
    return project != null
            && isNameValid(project.getName())
            && isDescriptionValid(project.getDescription())
            && isNotEmptyOrNull(project.getGoal1());
  }

  private boolean isDescriptionValid(String description) {
    return isNotEmptyOrNull(description)
            && description.length() >= MIN_DESCRIPTION_LENGTH;
  }

  private boolean isNameValid(String name) {
    return isNotEmptyOrNull(name)
            && name.length() >= MIN_NAME_LENGTH;
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
