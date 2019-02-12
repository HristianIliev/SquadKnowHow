package squadknowhow.utils.validators;

import java.util.Objects;

import squadknowhow.request.models.ListEntry;
import squadknowhow.utils.validators.base.IValidator;

public class TodoListEntryValidator implements IValidator<ListEntry> {
  @Override
  public boolean isValid(ListEntry todoListEntry) {
    return todoListEntry != null
            && isNotEmptyOrNull(todoListEntry.getTitle());
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
