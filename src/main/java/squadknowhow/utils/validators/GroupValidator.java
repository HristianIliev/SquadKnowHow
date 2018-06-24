package squadknowhow.utils.validators;

import java.util.Objects;

import squadknowhow.dbmodels.Group;
import squadknowhow.utils.validators.base.IValidator;

public class GroupValidator implements IValidator<Group> {
  private static final int MIN_NAME_LENGTH = 5;
  private static final int MIN_DESCRIPTION_LENGTH = 150;

  @Override
  public boolean isValid(Group group) {
    return group != null
            && isNameValid(group.getName())
            && isDescriptionValid(group.getDescription())
            && isGroupTypeValid(group.getGroupType());
  }

  private boolean isGroupTypeValid(String groupType) {
    return isNotEmpty(groupType)
            && isNotNull(groupType);
  }

  private boolean isNotEmpty(String str) {
    return !Objects.equals(str, "");
  }

  private boolean isNotNull(String str) {
    return str != null;
  }

  /**
   * Method that checks if the name of the group is valid.
   * @param name the group name
   * @return if the group name is vaid.
   */
  public boolean isNameValid(String name) {
    return isNotEmpty(name)
            && isNotNull(name)
            && name.length() >= MIN_NAME_LENGTH;
  }

  private boolean isDescriptionValid(String description) {
    return isNotEmpty(description)
            && isNotNull(description)
            && description.length() >= MIN_DESCRIPTION_LENGTH;
  }
}
