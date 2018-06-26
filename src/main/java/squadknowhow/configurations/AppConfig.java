package squadknowhow.configurations;

import com.authy.AuthyApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import squadknowhow.contracts.IRepository;

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
import squadknowhow.repositories.HibernateRepository;
import squadknowhow.request.models.EditedProject;
import squadknowhow.request.models.EditedUser;
import squadknowhow.request.models.ListEntry;
import squadknowhow.services.Settings;
import squadknowhow.utils.validators.CalendarEventValidator;
import squadknowhow.utils.validators.EditedProjectValidator;
import squadknowhow.utils.validators.EditedUserValidator;
import squadknowhow.utils.validators.EmailValidator;
import squadknowhow.utils.validators.GroupValidator;
import squadknowhow.utils.validators.IdValidator;
import squadknowhow.utils.validators.PasswordValidator;
import squadknowhow.utils.validators.ProjectValidator;
import squadknowhow.utils.validators.TodoListEntryValidator;
import squadknowhow.utils.validators.UserValidator;
import squadknowhow.utils.validators.base.IValidator;

@Configuration
public class AppConfig {
  @Bean
  @Autowired
  IValidator<CalendarEvent> provideCalendarEventValidator() {
    return new CalendarEventValidator();
  }

  @Bean
  @Autowired
  IValidator<ListEntry> provideTodoListEntryValidator() {
    return new TodoListEntryValidator();
  }

  @Bean
  @Autowired
  IValidator<User> provideUsersValidator(
          @Qualifier("PasswordValidator") IValidator<String> passwordValidator,
          @Qualifier("EmailValidator") IValidator<String> emailValidator) {
    return new UserValidator(passwordValidator, emailValidator);
  }

  @Bean
  @Autowired
  IValidator<Project> provideProjectsValidator() {
    return new ProjectValidator();
  }

  @Bean
  @Autowired
  IValidator<EditedProject> provideEditedProjectValidator() {
    return new EditedProjectValidator();
  }

  @Bean
  @Autowired
  IValidator<EditedUser> provideEditedUserValidator(
          @Qualifier("PasswordValidator") IValidator<String> passwordValidator,
          @Qualifier("EmailValidator") IValidator<String> emailValidator) {
    return new EditedUserValidator(passwordValidator, emailValidator);
  }

  @Bean("PasswordValidator")
  @Autowired
  IValidator<String> providePasswordValidator() {
    return new PasswordValidator();
  }

  @Bean("EmailValidator")
  @Autowired
  IValidator<String> provideEmailValidator() {
    return new EmailValidator();
  }

  @Bean
  @Autowired
  IValidator<Group> provideGroupValidator() {
    return new GroupValidator();
  }

  @Bean
  @Autowired
  IValidator<Integer> provideIdValidator() {
    return new IdValidator();
  }

  @Bean
  @Autowired
  IRepository<User> provideUsersGenericRepository() {
    HibernateRepository<User> repository = new HibernateRepository<>();
    repository.setEntityClass(User.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<UserShort> provideUsersShortGenericRepository() {
    HibernateRepository<UserShort> repository = new HibernateRepository<>();
    repository.setEntityClass(UserShort.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<TodoListEntry> provideTodoListEntriesGenericRepository() {
    HibernateRepository<TodoListEntry> repository = new HibernateRepository<>();
    repository.setEntityClass(TodoListEntry.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<CalendarEvent> provideCalendarEventsGenericRepository() {
    HibernateRepository<CalendarEvent> repository = new HibernateRepository<>();
    repository.setEntityClass(CalendarEvent.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Advice> provideAdvicesGenericRepository() {
    HibernateRepository<Advice> repository = new HibernateRepository<>();
    repository.setEntityClass(Advice.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Question> provideQuestionsGenericRepository() {
    HibernateRepository<Question> repository = new HibernateRepository<>();
    repository.setEntityClass(Question.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<City> provideCitiesGenericRepository() {
    HibernateRepository<City> repository = new HibernateRepository<>();
    repository.setEntityClass(City.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Company> provideCompaniesGenericRepository() {
    HibernateRepository<Company> repository = new HibernateRepository<>();
    repository.setEntityClass(Company.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Group> provideGroupsGenericRepository() {
    HibernateRepository<Group> repository = new HibernateRepository<>();
    repository.setEntityClass(Group.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Interest> provideInterestsGenericRepository() {
    HibernateRepository<Interest> repository = new HibernateRepository<>();
    repository.setEntityClass(Interest.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Message> provideMessagesGenericRepository() {
    HibernateRepository<Message> repository = new HibernateRepository<>();
    repository.setEntityClass(Message.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Notification> provideNotificationsGenericRepository() {
    HibernateRepository<Notification> repository = new HibernateRepository<>();
    repository.setEntityClass(Notification.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Project> provideProjectsGenericRepository() {
    HibernateRepository<Project> repository = new HibernateRepository<>();
    repository.setEntityClass(Project.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<ProjectShort> provideProjectsShortGenericRepository() {
    HibernateRepository<ProjectShort> repository = new HibernateRepository<>();
    repository.setEntityClass(ProjectShort.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Skill> provideSkillsGenericRepository() {
    HibernateRepository<Skill> repository = new HibernateRepository<>();
    repository.setEntityClass(Skill.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<UserCategory> provideUserCategoriesGenericRepository() {
    HibernateRepository<UserCategory> repository = new HibernateRepository<>();
    repository.setEntityClass(UserCategory.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<GroupShort> provideGroupsShortGenericRepository() {
    HibernateRepository<GroupShort> repository = new HibernateRepository<>();
    repository.setEntityClass(GroupShort.class);

    return repository;
  }

  @Autowired
  private Settings settings;

  @Bean
  public AuthyApiClient authyApiClient() {
    return new AuthyApiClient(settings.getAuthyId());
  }
}
