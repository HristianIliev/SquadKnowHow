package squadknowhow.utils.validators.base;

public interface IValidator<T> {
  boolean isValid(T object);
}
