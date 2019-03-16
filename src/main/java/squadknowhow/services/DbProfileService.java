package squadknowhow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import squadknowhow.contracts.IProfileService;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.Auction;
import squadknowhow.dbmodels.City;
import squadknowhow.dbmodels.Interest;
import squadknowhow.dbmodels.Message;
import squadknowhow.dbmodels.Note;
import squadknowhow.dbmodels.Notification;
import squadknowhow.dbmodels.Skill;
import squadknowhow.dbmodels.User;
import squadknowhow.dbmodels.UserCategory;
import squadknowhow.dbmodels.UserShort;
import squadknowhow.request.models.PersonalInfo;
import squadknowhow.request.models.RequestBase64;
import squadknowhow.request.models.SentMessage;
import squadknowhow.response.models.ResponseCall;
import squadknowhow.response.models.ResponseCheckCall;
import squadknowhow.response.models.ResponsePagination;
import squadknowhow.response.models.ResponseSuccessful;
import squadknowhow.response.models.UserInformation;
import squadknowhow.utils.FileUtils;
import squadknowhow.utils.validators.base.IValidator;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class DbProfileService implements IProfileService {
  private static final int PAGE_LENGTH = 12;
  private static final double PAGE_LENGTH_DOUBLE = 12.0;
  private static final String EMAIL_PASS = System.getenv("EMAIL_PASS");

  private final IValidator<Integer> idValidator;
  private final IRepository<User> usersRepository;
  private final IRepository<Notification> notificationsRepository;
  private final IRepository<Message> messagesRepository;
  private final IRepository<Interest> interestsRepository;
  private final IRepository<Skill> skillsRepository;
  private final IRepository<City> citiesRepository;
  private final IRepository<UserCategory> userCategoriesRepository;
  private final IRepository<UserShort> usersShortRepository;
  private final IRepository<Note> notesRepository;

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
                          IRepository<Note> notesRepository) {
    this.usersRepository = usersRepository;
    this.usersShortRepository = usersShortRepository;
    this.notificationsRepository = notificationsRepository;
    this.messagesRepository = messagesRepository;
    this.interestsRepository = interestsRepository;
    this.skillsRepository = skillsRepository;
    this.citiesRepository = citiesRepository;
    this.userCategoriesRepository = userCategoriesRepository;
    this.idValidator = idValidator;
    this.notesRepository = notesRepository;
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

  // Notifies the system that the user has completed the first time tour.
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
                                  String languages,
                                  String sortBy) {
    List<UserShort> users = this.usersShortRepository.getAll();
    users = this.buildWhereClauses(users,
            name,
            userCategory,
            city,
            skills,
            languages);

    switch (sortBy) {
      case "dateCreationDesc":
        users.sort(UserShort.DATECREATED_DESC_COMPARATOR);
        break;
      case "ratingAsc":
        users.sort(UserShort.RATING_ASC_COMPARATOR);
        break;
      case "recommendationsAsc":
        users.sort(UserShort.RECOMMENDATIONS_ASC_COMPARATOR);
        break;
      default:
        users.sort(UserShort.DATECREATED_DESC_COMPARATOR);
        break;
    }

    // 12 entries in one page
    int fromIndex = (page - 1) * PAGE_LENGTH;
    int toIndex = fromIndex + PAGE_LENGTH;

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
    if (!user.isActivated()) {
      throw new UsernameNotFoundException("Account not activated");
    }

    return new org.springframework.security
            .core.userdetails.User(user.getEmail(),
            user.getPassword(),
            new ArrayList<>());
  }

  @Override
  public ResponsePagination getPeoplePages(String name,
                                           String userCategory,
                                           String city,
                                           String skills,
                                           String languages,
                                           String sortBy) {
    List<UserShort> users = this.usersShortRepository.getAll();
    users = this.buildWhereClauses(users,
            name,
            userCategory,
            city,
            skills,
            languages);

    switch (sortBy) {
      case "dateCreationDesc":
        users.sort(UserShort.DATECREATED_DESC_COMPARATOR);
        break;
      case "ratingAsc":
        users.sort(UserShort.RATING_ASC_COMPARATOR);
        break;
      case "recommendationsAsc":
        users.sort(UserShort.RECOMMENDATIONS_ASC_COMPARATOR);
        break;
      default:
        users.sort(UserShort.DATECREATED_DESC_COMPARATOR);
        break;
    }

    int numberOfPages = (int) Math.ceil(users.size() / PAGE_LENGTH_DOUBLE);

    return new ResponsePagination(numberOfPages, users.size());
  }

  @Override
  public Interest getInterest(String interestName) {
    return this.interestsRepository.getAll().stream()
            .filter(in -> in.getName().equals(interestName))
            .findFirst()
            .orElse(null);
  }

  @Override
  public UserInformation checkIfUserNeedsTour(int id) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    User user = this.usersRepository.getById(id);
    if (!user.isNeedsTour()) {
      return new UserInformation(false, user.getFirstName());
    }

    return new UserInformation(true, user.getFirstName());
  }

  @Override
  public boolean contact(String name,
                         String email,
                         String subject,
                         String message) {
    Properties props = new Properties();
    props.put("mail.smtp.host", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.auth", "true");
    Session session = Session
            .getInstance(props, new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("hristian00i.dev@gmail.com",
                        EMAIL_PASS);
              }
            });
    try {
      MimeMessage msg = new MimeMessage(session);
      String to = "hristian00i@abv.bg";
      InternetAddress[] address = InternetAddress.parse(to, true);
      msg.setRecipients(javax.mail.Message.RecipientType.TO, address);
      msg.setSubject(subject);
      msg.setSentDate(new Date());
      msg.setText("Потребител с име: " + name + " и email: "
              + email
              + " поиска да се свърже с вас."
              + " Неговото запитване е: "
              + message);
      msg.setHeader("XPriority", "1");
      Transport.send(msg);
      System.out.println("Mail has been sent successfully");
    } catch (MessagingException mex) {
      System.out.println("Unable to send an email" + mex);
    }

    return true;
  }

  @Override
  public void setOffline(String name) {
    User user = this.getUserByEmail(name);
    user.setOnline(false);

    this.usersRepository.update(user);
  }

  @Override
  public void setOnline(String name) {
    User user = this.getUserByEmail(name);
    user.setOnline(true);

    this.usersRepository.update(user);
  }

  @Override
  public String getUserName(int creatorId) {
    if (!this.idValidator.isValid(creatorId)) {
      throw new InvalidParameterException("CreatorId is not valid");
    }

    User user = this.getUserById(creatorId);
    return user.getFirstName() + " " + user.getLastName();
  }

  // Checks if a user is online. Used for the video calls.
  @Override
  public ResponseCall checkIfOnline(String emails,
                                    int senderId,
                                    boolean isAudio) {
    String[] tokens = emails.split(",");
    List<Integer> ids = new ArrayList<>();
    List<String> images = new ArrayList<>();
    List<User> users = new ArrayList<>();
    List<String> names = new ArrayList<>();
    for (String token : tokens) {
      token = token.trim();
      System.out.println(token);
      User user = this.getUserByEmail(token);
      if (user != null) {
        if (!user.getOnline()) {
          return new ResponseCall(false,
                  new ArrayList<>(),
                  new ArrayList<>(),
                  new ArrayList<>());
        } else {
          users.add(user);
          ids.add(user.getId());
          images.add(user.getImage());
          names.add(user.getFirstName() + " " + user.getLastName());
        }
      } else {
        if (!token.equals("")) {
          return new ResponseCall(false,
                  new ArrayList<>(),
                  new ArrayList<>(),
                  new ArrayList<>());
        }
      }
    }

    for (User user : users) {
      user.setSomeoneIsCalling(senderId);
      user.setOnlyAudio(isAudio);
      this.usersRepository.update(user);
    }

    return new ResponseCall(true, ids, images, names);
  }

  // The user has cancelled the call
  @Override
  public boolean noMoreCalling(String emails, int id) {
    String[] tokens = emails.split(",");
    User sender = this.usersRepository.getById(id);

    for (String token : tokens) {
      token = token.trim();
      System.out.println(token);
      User user = this.getUserByEmail(token);
      if (user != null) {
        Notification notificationToInsert = new Notification();
        notificationToInsert.setTimestamp(this.createTimestamp());
        notificationToInsert.setSender(sender);
        notificationToInsert.setRecipient(user);
        notificationToInsert.setKind("new-message");
        notificationToInsert.setContent(" ти е звънял.");

        this.notificationsRepository.create(notificationToInsert);

        user.setSomeoneIsCalling(0);
        this.usersRepository.update(user);
      }
    }

    return true;
  }

  @Override
  public Note createNote(Note note, int id, int projectId) {
    User user = this.usersRepository.getById(id);
    if (user.getNotes().stream()
            .anyMatch(n -> n.getName().equals(note.getName()))) {
      Note update = user.getNotes().stream()
              .filter(n -> n.getName().equals(note.getName()))
              .collect(Collectors.toList())
              .get(0);

      update.setDescription(note.getDescription());

      if (update.getDescription().equals("")) {
        return this.notesRepository.delete(update);
      }

      return this.notesRepository.update(update);
    } else {
      Note insert = new Note();
      insert.setDescription(note.getDescription());
      insert.setName(note.getName());
      insert.setUser(user);
      insert.setProjectId(projectId);
      return this.notesRepository.create(insert);
    }
  }

  @Override
  public List<Note> getNotes(int id) {
    return this.usersRepository.getById(id).getNotes();
  }

  @Override
  public Note deleteNote(int noteId) {
    return this.notesRepository.delete(this.notesRepository.getById(noteId));
  }

  @Override
  public boolean changePersonalInfo(int id, PersonalInfo personalInfo) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    } else if (personalInfo.getFirstName().length() < 2
            || personalInfo.getFirstName().length() > 16
            || personalInfo.getLastName().length() < 2
            || personalInfo.getLastName().length() > 16
            || personalInfo.getDescription().length() < 150) {
      throw new InvalidParameterException("PersonalInfo is not valid");
    }

    User user = this.usersRepository.getById(id);
    user.setFirstName(personalInfo.getFirstName());
    user.setLastName(personalInfo.getLastName());
    user.setDescription(personalInfo.getDescription());

    this.usersRepository.update(user);

    return true;
  }

  @Override
  public boolean changeImage(int id, RequestBase64 base64) throws IOException {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    String path = FileUtils.convertToFilepathFromBase64(base64.getBase64(), base64.getExtension());

    User user = this.usersRepository.getById(id);

    FileUtils.deleteFile(user.getImage());
    user.setImage(path);

    this.usersRepository.update(user);

    return true;
  }

  @Override
  public ResponseSuccessful deleteNotifications(int id) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    User user = this.usersRepository.getById(id);
    for (Notification notification
            : user.getNotifications()) {
      this.notificationsRepository.delete(notification);
    }

    return new ResponseSuccessful(true);
  }

  @Override
  public boolean stopGithubStatistics(int id) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    User user = this.usersRepository.getById(id);
    user.setShowGithubStatistics(false);
    this.usersRepository.update(user);

    return true;
  }

  @Override
  public boolean startGithubStatistics(int id) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    User user = this.usersRepository.getById(id);
    user.setShowGithubStatistics(true);
    this.usersRepository.update(user);

    return true;
  }

  // helper method for creating a timestamp with the date and the time.
  private String createTimestamp() {
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minutes = calendar.get(Calendar.MINUTE);
    return year + "." + month + "." + day + " " + hour + ":" + minutes;
  }

  // Checks if there is an ongoing call
  @Override
  public ResponseCheckCall checkIfThereIsACall(int id) {
    User user = this.getUserById(id);
    if (user.getSomeoneIsCalling() != 0) {
      User caller = this.getUserById(user.getSomeoneIsCalling());
      return new ResponseCheckCall(user.getSomeoneIsCalling(),
              caller.getImage(),
              caller.getFirstName() + " " + caller.getLastName(),
              caller.getCity().getName(),
              user.isOnlyAudio());
    } else {
      return new ResponseCheckCall(0, "", "", "", user.isOnlyAudio());
    }
  }

  // The user has declined the call
  @Override
  public boolean thereIsNoCall(int id) {
    User user = this.usersRepository.getById(id);
    user.setSomeoneIsCalling(0);
    this.usersRepository.update(user);

    return true;
  }

  @Override
  public Skill getSkill(String skillName) {
    return this.skillsRepository.getAll().stream()
            .filter(sk -> sk.getName().equals(skillName))
            .findFirst()
            .orElse(null);
  }

  @Override
  public City getCity(String cityName) {
    return this.citiesRepository.getAll().stream()
            .filter(cit -> cit.getName().equals(cityName))
            .findFirst()
            .orElse(null);
  }

  @Override
  public UserCategory getUserCategory(String userCategoryName) {
    return this.userCategoriesRepository.getAll().stream()
            .filter(uc -> uc.getName().equals(userCategoryName))
            .findFirst()
            .orElse(null);
  }

  // Sends a message to a user
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

    // Sends a message and a notification to the both users.
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

  // Builds the filtering clauses for the users.
  private List<UserShort> buildWhereClauses(List<UserShort> users,
                                            String name,
                                            String userCategory,
                                            String city,
                                            String skills,
                                            String languages) {
    List<java.util.function.Predicate<UserShort>> predicates =
            new ArrayList<>();
    System.out.println("Name is: '"
            + name + "' UserCategory is: '"
            + userCategory + "' city is: '"
            + city + "' skills is: '"
            + skills + "' languages is: '"
            + languages + "'");
    if (!Objects.equals(name, "")) {
      predicates.add((UserShort user) ->
              (user.getFirstName().toLowerCase()
                      + " " + user.getLastName().toLowerCase())
                      .contains(name.toLowerCase()));
    }

    if (!Objects.equals(userCategory, "")
            && !Objects.equals(userCategory,
            "      търси за:            ")) {
      predicates.add((UserShort user) -> user.getSkillset().getName()
              .equals(userCategory));
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
          if (!separatedSkills[finalI].isEmpty()) {
            predicates.add((UserShort user) -> user.getSkills()
                    .stream()
                    .anyMatch(skill -> skill.getName()
                            .equals(separatedSkills[finalI])));
          }
        }
      } else {
        predicates.add((UserShort user) -> user.getSkills()
                .stream()
                .anyMatch(skill -> skill.getName().equals(skills)));
      }
    }

    if (!Objects.equals(languages, "")) {
      if (languages.contains(", ")) {
        String[] separatedLanguages = languages.split(", ");
        for (int i = 0; i < separatedLanguages.length; i++) {
          int finalI = i;
          if (!separatedLanguages[finalI].isEmpty()) {
            System.out.println(separatedLanguages[finalI]);
            predicates.add((UserShort user) -> user.getLanguages()
                    .stream()
                    .anyMatch(language -> language.getName()
                            .equals(separatedLanguages[finalI])));
          }
        }
      } else {
        predicates.add((UserShort user) -> user.getLanguages()
                .stream()
                .anyMatch(language -> language.getName().equals(languages)));
      }
    }

    predicates.add(UserShort::isActivated);

    java.util.function.Predicate<UserShort> predicate = predicates.stream()
            .reduce(java.util.function.Predicate::and)
            .orElse(x -> true);

    return users.stream()
            .filter(predicate)
            .collect(Collectors.toList());
  }
}
