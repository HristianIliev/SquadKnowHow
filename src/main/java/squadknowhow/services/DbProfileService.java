package squadknowhow.services;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import squadknowhow.contracts.IProfileService;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.City;
import squadknowhow.dbmodels.Interest;
import squadknowhow.dbmodels.Message;
import squadknowhow.dbmodels.Notification;
import squadknowhow.dbmodels.Skill;
import squadknowhow.dbmodels.User;
import squadknowhow.dbmodels.UserCategory;
import squadknowhow.dbmodels.UserShort;
import squadknowhow.request.models.EditedUser;
import squadknowhow.request.models.SentMessage;
import squadknowhow.response.models.ResponsePagination;
import squadknowhow.response.models.ResponseSuccessful;
import squadknowhow.utils.validators.base.IValidator;

@Service
public class DbProfileService implements IProfileService {
  private final IValidator<Integer> idValidator;
  private final IRepository<User> usersRepository;
  private final IRepository<Notification> notificationsRepository;
  private final IRepository<Message> messagesRepository;
  private final IRepository<Interest> interestsRepository;
  private final IRepository<Skill> skillsRepository;
  private final IRepository<City> citiesRepository;
  private final IRepository<UserCategory> userCategoriesRepository;
  private final IValidator<EditedUser> editedUserValidator;
  private final IRepository<UserShort> usersShortRepository;

  /**
   * Service for the business logic when dealing with profiles.
   *
   * @param usersRepository          Repository for the users.
   * @param usersShortRepository     Repository for the userShort.
   * @param notificationsRepository  Repository for the notifications.
   * @param messagesRepository       Repository for the messages.
   * @param interestsRepository      Repository for the interest.
   * @param skillsRepository         Repository for the skills.
   * @param citiesRepository         Repository for the cities.
   * @param userCategoriesRepository Repository for the UserCtegories.
   * @param idValidator              Validator for id parameters.
   * @param editedUserValidator      Validator for EditedUser parameters.
   */
  @Autowired
  public DbProfileService(IRepository<User> usersRepository,
                          IRepository<UserShort> usersShortRepository,
                          IRepository<Notification> notificationsRepository,
                          IRepository<Message> messagesRepository,
                          IRepository<Interest> interestsRepository,
                          IRepository<Skill> skillsRepository,
                          IRepository<City> citiesRepository,
                          IRepository<UserCategory> userCategoriesRepository,
                          IValidator<Integer> idValidator,
                          IValidator<EditedUser> editedUserValidator) {
    this.usersRepository = usersRepository;
    this.usersShortRepository = usersShortRepository;
    this.notificationsRepository = notificationsRepository;
    this.messagesRepository = messagesRepository;
    this.interestsRepository = interestsRepository;
    this.skillsRepository = skillsRepository;
    this.citiesRepository = citiesRepository;
    this.userCategoriesRepository = userCategoriesRepository;
    this.idValidator = idValidator;
    this.editedUserValidator = editedUserValidator;
  }

  @Override
  public ResponseSuccessful deleteNotification(int notificationId) {
    if (!this.idValidator.isValid(notificationId)) {
      throw new InvalidParameterException("NotificationId is not valid");
    }

    Notification entity = this.notificationsRepository.getById(notificationId);
    this.notificationsRepository.delete(entity);

    return new ResponseSuccessful(true);
  }

  @Override
  public boolean editProfile(EditedUser user) {
    if (!this.idValidator.isValid(user.getId())) {
      throw new InvalidParameterException("User id is not valid");
    } else if (!this.editedUserValidator.isValid(user)) {
      throw new InvalidParameterException("User is not valid");
    }

    User userToEdit = this.usersRepository.getById(user.getId());
    userToEdit.setFirstName(user.getFirstName());
    userToEdit.setLastName(user.getLastName());
    userToEdit.setDescription(user.getDescription());
    userToEdit.setEmail(user.getEmail());
    userToEdit.setPassword(user.getPassword());

    this.usersRepository.update(userToEdit);

    return true;
  }

  @Override
  public boolean tourCompleted(int id) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    User user = this.usersRepository.getById(id);
    user.setNeedsTour(false);
    this.usersRepository.update(user);

    return true;
  }

  @Override
  public ResponseSuccessful deleteMessage(int messageId) {
    if (!this.idValidator.isValid(messageId)) {
      throw new InvalidParameterException("MessageId is not valid");
    }

    Message entity = this.messagesRepository.getById(messageId);
    this.messagesRepository.delete(entity);

    return new ResponseSuccessful(true);
  }

  @Override
  public List<UserShort> getUsers(int page,
                                  String name,
                                  String userCategory,
                                  String city,
                                  String skills,
                                  String interests) {
    List<UserShort> users = this.usersShortRepository.getAll();
    users = this.buildWhereClauses(users,
            name,
            userCategory,
            city,
            skills,
            interests);

    int fromIndex = (page - 1) * 20;
    int toIndex = fromIndex + 20;

    if (toIndex > users.size()) {
      toIndex = users.size();
    }

    return users.subList(fromIndex, toIndex);
  }

  @Override
  public User getUserByEmail(String email) {
    return usersRepository.getAll()
            .stream()
            .filter(u -> u.getEmail().equals(email))
            .findFirst()
            .orElse(null);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = this.getUserByEmail(username);
    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
  }

  @Override
  public ResponsePagination getPeoplePages(String name,
                                           String userCategory,
                                           String city,
                                           String skills,
                                           String interests) {
    List<UserShort> users = this.usersShortRepository.getAll();
    users = this.buildWhereClauses(users, name, userCategory, city, skills, interests);

    int numberOfPages = (int) Math.ceil(users.size() / 20.0);

    return new ResponsePagination(numberOfPages, users.size());
  }

  private List<UserShort> buildWhereClauses(List<UserShort> users,
                                            String name,
                                            String userCategory,
                                            String city,
                                            String skills,
                                            String interests) {
    List<java.util.function.Predicate<UserShort>> predicates = new ArrayList<>();
    if (!Objects.equals(name, "")) {
      predicates.add((UserShort user) -> user.getFirstName().toLowerCase().contains(name.toLowerCase()));
    }

    if (!Objects.equals(userCategory, "")) {
      predicates.add((UserShort user) -> user.getSkillset().getName().equals(userCategory));
    }

    if (!Objects.equals(city, "")) {
      predicates.add((UserShort user) -> user.getCity() != null
              && user.getCity().getName().equals(city));
    }

    if (!Objects.equals(skills, "")) {
      if (skills.contains(", ")) {
        String[] separatedSkills = skills.split(", ");
        for (int i = 0; i < separatedSkills.length; i++) {
          int finalI = i;
          predicates.add((UserShort user) -> user.getSkills()
                  .stream()
                  .anyMatch(skill -> skill.getName().equals(separatedSkills[finalI])));
        }
      } else {
        predicates.add((UserShort user) -> user.getSkills()
                .stream()
                .anyMatch(skill -> skill.getName().equals(skills)));
      }
    }

    if (!Objects.equals(interests, "")) {
      if (skills.contains(", ")) {
        String[] separatedInterests = interests.split(", ");
        for (int i = 0; i < separatedInterests.length; i++) {
          int finalI = i;
          predicates.add((UserShort user) -> user.getInterests()
                  .stream()
                  .anyMatch(interest -> interest.getName().equals(separatedInterests[finalI])));
        }
      } else {
        predicates.add((UserShort user) -> user.getInterests()
                .stream()
                .anyMatch(interest -> interest.getName().equals(interests)));
      }
    }

    java.util.function.Predicate<UserShort> predicate = predicates.stream()
            .reduce(java.util.function.Predicate::and)
            .orElse(x -> true);

    return users.stream()
            .filter(predicate)
            .collect(Collectors.toList());
  }

  /**
   * Method for getting a certain Interest by name.
   *
   * @param interestName The interest name to filter by.
   * @return The found interest or null if not found.
   */
  @Override
  public Interest getInterest(String interestName) {
    return this.interestsRepository.getAll().stream()
            .filter(in -> in.getName().equals(interestName))
            .findFirst()
            .orElse(null);
  }

  /**
   * Method for getting a certain Skill by name.
   *
   * @param skillName The skill name to filter by.
   * @return The found skill or null if not found.
   */
  @Override
  public Skill getSkill(String skillName) {
    return this.skillsRepository.getAll().stream()
            .filter(sk -> sk.getName().equals(skillName))
            .findFirst()
            .orElse(null);
  }

  /**
   * Method for getting a certain City by name.
   *
   * @param cityName The city name to filter by.
   * @return The found city or null if not found.
   */
  @Override
  public City getCity(String cityName) {
    return this.citiesRepository.getAll().stream()
            .filter(cit -> cit.getName().equals(cityName))
            .findFirst()
            .orElse(null);
  }

  /**
   * Method for getting a certain UserCategory by name.
   *
   * @param userCategoryName The userCategory name to filter by.
   * @return The found userCategory or null if not found.
   */
  @Override
  public UserCategory getUserCategory(String userCategoryName) {
    return this.userCategoriesRepository.getAll().stream()
            .filter(uc -> uc.getName().equals(userCategoryName))
            .findFirst()
            .orElse(null);
  }

  @Override
  public ResponseSuccessful sendMessage(SentMessage message) {
    if (!this.idValidator.isValid(message.getSenderId())) {
      throw new InvalidParameterException("SenderId is not valid");
    } else if (!this.idValidator.isValid(message.getRecipientId())) {
      throw new InvalidParameterException("RecipientId is not valid");
    } else if (message.getTopic().isEmpty()) {
      throw new InvalidParameterException("Topic is empty");
    }

    User sender = this.getUserById(message.getSenderId());
    User recipient = this.getUserById(message.getRecipientId());

    Message messageToInsert = new Message();
    messageToInsert.setTopic(message.getTopic());
    messageToInsert.setContent(message.getContent());
    messageToInsert.setTimestamp(message.getTimestamp());
    messageToInsert.setSender(sender);
    messageToInsert.setRecipient(recipient);
    messageToInsert.setKind(message.getKind());

    this.messagesRepository.create(messageToInsert);

    Notification notificationToInsert = new Notification();
    notificationToInsert.setTimestamp(message.getTimestamp());
    notificationToInsert.setSender(sender);
    notificationToInsert.setRecipient(recipient);
    notificationToInsert.setKind("new-message");
    notificationToInsert.setContent(" ти изпрати съобщение.");

    this.notificationsRepository.create(notificationToInsert);

    return new ResponseSuccessful(true);
  }

  @Override
  public User getUserById(int id) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    return this.usersRepository.getById(id);
  }
}
