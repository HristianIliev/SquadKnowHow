package squadknowhow.utils.validators;

import java.util.Objects;

import squadknowhow.dbmodels.CalendarEvent;

import squadknowhow.utils.validators.base.IValidator;

public class CalendarEventValidator implements IValidator<CalendarEvent> {
  @Override
  public boolean isValid(CalendarEvent event) {
    return event != null
            && isNotEmptyOrNull(event.getTitle())
            && isNotEmptyOrNull(event.getStart());
  }

  private boolean isNotEmpty(String str) {
    return !Objects.equals(str, "");
  }

  private boolean isNotNull(String str) {
    return str != null;
  }

  private boolean isNotEmptyOrNull(String str) {
    return isNotEmpty(str) && isNotNull(str);
  }

}
