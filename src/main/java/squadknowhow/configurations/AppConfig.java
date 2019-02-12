package squadknowhow.configurations;

import com.authy.AuthyApiClient;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import squadknowhow.contracts.IRepository;
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
import squadknowhow.repositories.HibernateRepository;
import squadknowhow.request.models.CreateProject;
import squadknowhow.request.models.EditedProject;
import squadknowhow.request.models.EditedUser;
import squadknowhow.request.models.ListEntry;
import squadknowhow.services.Settings;
import squadknowhow.utils.HibernateUtils;
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
  IValidator<CreateProject> provideProjectsValidator() {
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
  IRepository<User> provideUsersGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<User> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(User.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<UserShort> provideUsersShortGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<UserShort> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(UserShort.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<TodoListEntry> provideTodoGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<TodoListEntry> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(TodoListEntry.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<CalendarEvent> provideCalendarEventsGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<CalendarEvent> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(CalendarEvent.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Advice> provideAdvicesGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<Advice> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(Advice.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<WantedMember> provideWantedMembersGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<WantedMember> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(WantedMember.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Question> provideQuestionsGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<Question> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(Question.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Update> provideUpdatesGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<Update> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(Update.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<City> provideCitiesGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<City> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(City.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Note> provideNotesGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<Note> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(Note.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Company> provideCompaniesGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<Company> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(Company.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Language> provideLanguagesGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<Language> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(Language.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Group> provideGroupsGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<Group> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(Group.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Interest> provideInterestsGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<Interest> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(Interest.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Message> provideMessagesGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<Message> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(Message.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Notification> provideNotificationsGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<Notification> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(Notification.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Project> provideProjectsGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<Project> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(Project.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Visit> provideVisitsGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<Visit> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(Visit.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<ProjectShort> provideProjectsShortGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<ProjectShort> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(ProjectShort.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Skill> provideSkillsGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<Skill> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(Skill.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<UserCategory> provideUserCategoriesGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<UserCategory> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(UserCategory.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<GroupShort> provideGroupsShortGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<GroupShort> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(GroupShort.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Auction> provideAuctionGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<Auction> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(Auction.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Bid> provideBidsGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<Bid> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(Bid.class);

    return repository;
  }

  @Bean
  @Autowired
  IRepository<Review> provideReviewsGenericRepository(
          SessionFactory sessionFactory) {
    HibernateRepository<Review> repository =
            new HibernateRepository<>(sessionFactory);
    repository.setEntityClass(Review.class);

    return repository;
  }

  @Bean
  @Autowired
  LogoutHandler provideLogoutHandler() {
    return new LogoutSuccessHandler();
  }

  @Bean
  @Autowired
  AuthenticationSuccessHandler provideAuthenticationSuccessHandler() {
    return new LoginSuccessHandler();
  }

  @Autowired
  private Settings settings;

  @Bean
  public AuthyApiClient authyApiClient() {
    return new AuthyApiClient(settings.getAuthyId());
  }

  @Bean
  SessionFactory provideSessionFactory() {
    return HibernateUtils.getSessionFactory();
  }
}
