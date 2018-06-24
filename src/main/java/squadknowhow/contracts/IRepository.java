package squadknowhow.contracts;

import java.util.List;

public interface IRepository<T extends Model> {
  List<T> getAll();
  
  T getById(int id);
  
  T create(T entity);
  
  T delete(T entity);
  
  T update(T entity);
}
