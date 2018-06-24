package squadknowhow.utils.validators;

import java.util.Objects;

import squadknowhow.dbmodels.Project;
import squadknowhow.utils.validators.base.IValidator;

public class ProjectValidator implements IValidator<Project> {
  private static final int MIN_NAME_LENGTH = 5;
  private static final int MIN_DESCRIPTION_LENGTH = 150;

  @Override
  public boolean isValid(Project project) {
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
