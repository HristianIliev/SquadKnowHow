package squadknowhow.utils.validators;

import squadknowhow.utils.validators.base.IValidator;

public class IdValidator implements IValidator<Integer> {
  private static final int MIN_ID_VALUE = 1;

  @Override
  public boolean isValid(Integer id) {
    return id >= MIN_ID_VALUE;
  }
}
