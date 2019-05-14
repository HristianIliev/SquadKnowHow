package squadknowhow.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import squadknowhow.dbmodels.Advice;
import squadknowhow.dbmodels.Auction;
import squadknowhow.dbmodels.Bid;
import squadknowhow.dbmodels.CalendarEvent;
import squadknowhow.dbmodels.City;
import squadknowhow.dbmodels.Company;
import squadknowhow.dbmodels.Group;
import squadknowhow.dbmodels.GroupShort;
import squadknowhow.dbmodels.Interest;
import squadknowhow.dbmodels.Language;
import squadknowhow.dbmodels.Message;
import squadknowhow.dbmodels.Note;
import squadknowhow.dbmodels.Notification;
import squadknowhow.dbmodels.Project;
import squadknowhow.dbmodels.ProjectShort;
import squadknowhow.dbmodels.Question;
import squadknowhow.dbmodels.Review;
import squadknowhow.dbmodels.Skill;
import squadknowhow.dbmodels.TodoListEntry;
import squadknowhow.dbmodels.Update;
import squadknowhow.dbmodels.User;
import squadknowhow.dbmodels.UserCategory;
import squadknowhow.dbmodels.UserShort;
import squadknowhow.dbmodels.Visit;
import squadknowhow.dbmodels.WantedMember;

import java.util.Properties;

public class HibernateUtils {
  private static final String REMOTE_CONNECTION_URL = System.getenv("REMOTE_CONNECTION_URL");
  private static final String REMOTE_CONNECTION_PASSWORD = System.getenv("REMOTE_CONNECTION_PASSWORD");
  private static final String REMOTE_CONNECTION_USERNAME = System.getenv("REMOTE_CONNECTION_USERNAME");

  private static SessionFactory sessionFactory;

  static {
    try {
      Properties prop = new Properties();
      if (REMOTE_CONNECTION_URL != null) {
        //      AppHarbor Cloud MySQL Database
        prop.setProperty("hibernate.connection.url", REMOTE_CONNECTION_URL);
        prop.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        prop.setProperty("hibernate.connection.password", REMOTE_CONNECTION_PASSWORD);
        prop.setProperty("hibernate.connection.username", REMOTE_CONNECTION_USERNAME);
        prop.setProperty("hibernate.default_schema", "db374c8f445c7e4c5fb4e8a918012f492e");
        prop.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5DBDialect");
        prop.setProperty("hibernate.connection.CharSet", "utf8");
        prop.setProperty("hibernate.connection.characterEncoding", "utf8");
        prop.setProperty("hibernate.connection.useUnicode", "true");
      } else {
        //      Local MySQL Server
        prop.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/squadknowhow");
        prop.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        prop.setProperty("hibernate.connection.password", "root");
        prop.setProperty("hibernate.connection.username", "root");
        prop.setProperty("hibernate.default_schema", "squadknowhow");
        prop.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        prop.setProperty("hibernate.connection.CharSet", "utf8");
        prop.setProperty("hibernate.connection.characterEncoding", "utf8");
        prop.setProperty("hibernate.connection.useUnicode", "true");
      }

      sessionFactory = new Configuration()
              .addPackage("squadknowhow")
              .addProperties(prop)
              .addAnnotatedClass(City.class)
              .addAnnotatedClass(Interest.class)
              .addAnnotatedClass(Company.class)
              .addAnnotatedClass(Skill.class)
              .addAnnotatedClass(User.class)
              .addAnnotatedClass(UserShort.class)
              .addAnnotatedClass(UserCategory.class)
              .addAnnotatedClass(Project.class)
              .addAnnotatedClass(ProjectShort.class)
              .addAnnotatedClass(Group.class)
              .addAnnotatedClass(GroupShort.class)
              .addAnnotatedClass(Message.class)
              .addAnnotatedClass(Notification.class)
              .addAnnotatedClass(Advice.class)
              .addAnnotatedClass(Question.class)
              .addAnnotatedClass(TodoListEntry.class)
              .addAnnotatedClass(CalendarEvent.class)
              .addAnnotatedClass(Auction.class)
              .addAnnotatedClass(Bid.class)
              .addAnnotatedClass(Language.class)
              .addAnnotatedClass(Visit.class)
              .addAnnotatedClass(Note.class)
              .addAnnotatedClass(Review.class)
              .addAnnotatedClass(Update.class)
              .addAnnotatedClass(WantedMember.class)
              .buildSessionFactory();
    } catch (Exception ex) {
      System.out.println("----------------------");
      System.out.println(ex);
    }
  }

  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }
}
