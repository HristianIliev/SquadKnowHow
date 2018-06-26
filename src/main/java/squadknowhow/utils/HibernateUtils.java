package squadknowhow.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import squadknowhow.dbmodels.Advice;
import squadknowhow.dbmodels.CalendarEvent;
import squadknowhow.dbmodels.City;
import squadknowhow.dbmodels.Company;
import squadknowhow.dbmodels.Group;
import squadknowhow.dbmodels.GroupShort;
import squadknowhow.dbmodels.Interest;
import squadknowhow.dbmodels.Message;
import squadknowhow.dbmodels.Notification;
import squadknowhow.dbmodels.Project;
import squadknowhow.dbmodels.ProjectShort;
import squadknowhow.dbmodels.Question;
import squadknowhow.dbmodels.Skill;
import squadknowhow.dbmodels.TodoListEntry;
import squadknowhow.dbmodels.User;
import squadknowhow.dbmodels.UserCategory;
import squadknowhow.dbmodels.UserShort;

public class HibernateUtils {
  private static SessionFactory sessionFactory;

  static {
    try {
      Configuration configuration = new Configuration().configure();

      configuration.addAnnotatedClass(City.class);
      configuration.addAnnotatedClass(Interest.class);
      configuration.addAnnotatedClass(Company.class);
      configuration.addAnnotatedClass(Skill.class);
      configuration.addAnnotatedClass(User.class);
      configuration.addAnnotatedClass(UserShort.class);
      configuration.addAnnotatedClass(UserCategory.class);
      configuration.addAnnotatedClass(Project.class);
      configuration.addAnnotatedClass(ProjectShort.class);
      configuration.addAnnotatedClass(Group.class);
      configuration.addAnnotatedClass(GroupShort.class);
      configuration.addAnnotatedClass(Message.class);
      configuration.addAnnotatedClass(Notification.class);
      configuration.addAnnotatedClass(Advice.class);
      configuration.addAnnotatedClass(Question.class);
      configuration.addAnnotatedClass(TodoListEntry.class);
      configuration.addAnnotatedClass(CalendarEvent.class);

      StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
              .applySettings(
                      configuration.getProperties())
              .build();

      sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    } catch (Exception ex) {
      System.out.println("----------------------");
      System.out.println(ex);
    }
  }

  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }
}
