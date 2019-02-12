package squadknowhow.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import squadknowhow.contracts.IRepository;
import squadknowhow.contracts.Model;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class HibernateRepository<T extends Model> implements IRepository<T> {
  private final SessionFactory sessionFactory;
  private Class<T> entityClass;

  @Autowired
  public HibernateRepository(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public List<T> getAll() {
    Session session = sessionFactory.openSession();
    Transaction transaction = session.getTransaction();
    transaction.begin();

    CriteriaBuilder builder = session.getCriteriaBuilder();

    CriteriaQuery<T> criteriaQuery = builder.createQuery(this.getEntityClass());
    criteriaQuery.from(this.getEntityClass());

    List<T> entities = session.createQuery(criteriaQuery)
            .getResultList();

    transaction.commit();
    session.close();

    return entities;
  }

  @Override
  public T getById(int id) {
    Session session = sessionFactory.openSession();
    Transaction transaction = session.getTransaction();
    transaction.begin();

    T entity = session.get(this.getEntityClass(), id);

    transaction.commit();
    session.close();

    return entity;
  }

  @Override
  public T create(T entity) {
    Session session = sessionFactory.openSession();
    Transaction transaction = session.getTransaction();
    transaction.begin();

    session.save(entity);

    transaction.commit();
    session.close();

    return entity;
  }

  @Override
  public T delete(T entity) {
    Session session = sessionFactory.openSession();
    Transaction transaction = session.getTransaction();
    transaction.begin();

    session.remove(entity);

    transaction.commit();
    session.close();

    return entity;
  }

  @Override
  public T update(T entity) {
    Session session = sessionFactory.openSession();
    Transaction transaction = session.getTransaction();
    transaction.begin();

    session.update(entity);

    transaction.commit();
    session.close();

    return entity;
  }

  public Class<T> getEntityClass() {
    return this.entityClass;
  }

  public void setEntityClass(Class<T> entityClass) {
    this.entityClass = entityClass;
  }
}
