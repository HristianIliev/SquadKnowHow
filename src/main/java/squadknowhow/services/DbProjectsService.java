package squadknowhow.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import squadknowhow.contracts.IChatbotResponse;
import squadknowhow.contracts.IProjectsService;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.Advice;
import squadknowhow.dbmodels.Auction;
import squadknowhow.dbmodels.City;
import squadknowhow.dbmodels.Language;
import squadknowhow.dbmodels.Message;
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
import squadknowhow.dbmodels.Visit;
import squadknowhow.dbmodels.WantedMember;
import squadknowhow.request.models.CreateProject;
import squadknowhow.request.models.DeleteProjectInfo;
import squadknowhow.request.models.EditedProject;
import squadknowhow.request.models.Parameter;
import squadknowhow.request.models.SentAdvice;
import squadknowhow.request.models.SentMessage;
import squadknowhow.request.models.SentQuestion;
import squadknowhow.response.models.ChatbotConversation;
import squadknowhow.response.models.ChatbotMemory;
import squadknowhow.response.models.ChatbotReplyCarousel;
import squadknowhow.response.models.ChatbotReplyCarouselContent;
import squadknowhow.response.models.ChatbotReplyCarouselContentButton;
import squadknowhow.response.models.ChatbotReplyText;
import squadknowhow.response.models.ChatbotResponseCarousel;
import squadknowhow.response.models.ChatbotResponseText;
import squadknowhow.response.models.Coordinate;
import squadknowhow.response.models.FileUploaderParameters;
import squadknowhow.response.models.FineUploaderImage;
import squadknowhow.response.models.ProjectWithName;
import squadknowhow.response.models.ResponseCheckProjectName;
import squadknowhow.response.models.ResponsePagination;
import squadknowhow.response.models.ResponseProjectId;
import squadknowhow.response.models.ResponseSuccessful;
import squadknowhow.response.models.ResponseUpload;
import squadknowhow.response.models.ResponseVisits;
import squadknowhow.response.models.UserProjects;
import squadknowhow.utils.FileUtils;
import squadknowhow.utils.validators.base.IValidator;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URI;
import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DbProjectsService implements IProjectsService {
  private static final int STATUS_NOT_FOUND = 404;
  private static final int STATUS_BAD_REQUEST = 400;
  private static final int STATUS_OK = 200;
  private static final int PAGE_LENGTH = 12;
  private static final double PAGE_LENGTH_DOUBLE = 12.0;
  private static final String ACCOUNT_SID = System.getenv("ACCOUNT_SID");
  private static final String AUTH_TOKEN = System.getenv("AUTH_TOKEN");
  private static final String FROM_NUMBER = System.getenv("FROM_NUMBER");
  private static final int PHONE_NUMBER_LENGTH = 13;
  private static final String EMAIL_PASS = System.getenv("EMAIL_PASS");

  private final IRepository<Question> questionsRepository;
  private final IValidator<Integer> idValidator;
  private final IRepository<Advice> advicesRepository;
  private final IRepository<Project> projectsRepository;
  private final IRepository<ProjectShort> projectsShortRepository;
  private final IRepository<Message> messagesRepository;
  private final IRepository<User> usersRepository;
  private final IRepository<Notification> notificationsRepository;
  private final IRepository<UserCategory> userCategoriesRepository;
  private final IValidator<EditedProject> editedProjectValidator;
  private final IValidator<CreateProject> projectValidator;
  private final IRepository<TodoListEntry> todoListEntryRepository;
  private final IRepository<Visit> visitsRepository;
  private final IRepository<Review> reviewsRepository;
  private final IRepository<Update> updatesRepository;
  private final IRepository<Skill> skillsRepository;
  private final IRepository<Language> languagesRepository;
  private final IRepository<City> citiesRepository;
  private final IRepository<WantedMember> wantedMembersRepository;

  @Autowired
  public DbProjectsService(IRepository<Project> projectsRepository,
                           IRepository<ProjectShort> projectsShortRepository,
                           IRepository<Message> messagesRepository,
                           IRepository<User> usersRepository,
                           IRepository<Notification> notificationsRepository,
                           IRepository<UserCategory> userCategoriesRepository,
                           IRepository<Advice> advicesRepository,
                           IRepository<Question> questionsRepository,
                           IValidator<Integer> idValidator,
                           IValidator<EditedProject> editedProjectValidator,
                           IValidator<CreateProject> projectValidator,
                           IRepository<TodoListEntry> todoListEntryRepository,
                           IRepository<Visit> visitsRepository,
                           IRepository<Review> reviewsRepository,
                           IRepository<Update> updatesRepository,
                           IRepository<Skill> skillsRepository,
                           IRepository<Language> languagesRepository,
                           IRepository<City> citiesRepository,
                           IRepository<WantedMember> wantedMembersRepository) {
    this.projectsRepository = projectsRepository;
    this.projectsShortRepository = projectsShortRepository;
    this.messagesRepository = messagesRepository;
    this.usersRepository = usersRepository;
    this.notificationsRepository = notificationsRepository;
    this.userCategoriesRepository = userCategoriesRepository;
    this.advicesRepository = advicesRepository;
    this.questionsRepository = questionsRepository;
    this.idValidator = idValidator;
    this.editedProjectValidator = editedProjectValidator;
    this.projectValidator = projectValidator;
    this.todoListEntryRepository = todoListEntryRepository;
    this.visitsRepository = visitsRepository;
    this.reviewsRepository = reviewsRepository;
    this.updatesRepository = updatesRepository;
    this.skillsRepository = skillsRepository;
    this.languagesRepository = languagesRepository;
    this.citiesRepository = citiesRepository;
    this.wantedMembersRepository = wantedMembersRepository;
  }

  @Override
  public ResponseVisits getChartData(int projectId,
                                     Calendar calendar) throws ParseException {
    if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    }

    Project project = this.projectsRepository.getById(projectId);
    List<Visit> visits = project.getVisits();
    List<String> dates = new ArrayList<>();
    List<Integer> values = new ArrayList<>();
//    Calendar calendar = Calendar.getInstance();
    for (int i = 0; i < 7; i++) {
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
      values.add(0);
      Date dateToAdd = calendar.getTime();
      dates.add(sdf.format(dateToAdd));
      for (int j = 0; j < visits.size(); j++) {
        Date date = sdf.parse(visits.get(j).getDate());
        Calendar visitDate = Calendar.getInstance();
        visitDate.setTime(date);
        if (calendar.get(Calendar.DAY_OF_YEAR)
                == visitDate.get(Calendar.DAY_OF_YEAR)
                && calendar.get(Calendar.YEAR)
                == visitDate.get(Calendar.YEAR)) {
          values.set(i, values.get(i) + 1);
        }
      }

      calendar.add(Calendar.DAY_OF_YEAR, -1);
    }

    return new ResponseVisits(dates, values);
  }

  @Override
  public List<Coordinate> getCoordinates() {
    List<Project> projects = this.projectsRepository.getAll();
    List<Coordinate> coordinates = new ArrayList<>();
    for (int i = 0; i < projects.size(); i++) {
      coordinates.add(new Coordinate(projects.get(i).getLatitude(),
              projects.get(i).getLongitude(),
              projects.get(i).getName()));
    }

    return coordinates;
  }

  @Override
  public boolean addPowerpointEmbedCode(int projectId, String link) {
    if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    }

    Project project = this.projectsRepository.getById(projectId);
    project.setPowerpointEmbedCode(link);
    this.projectsRepository.update(project);

    return true;
  }

  @Override
  public String getEmbedCode(int projectId) {
    if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    }

    return this.projectsRepository.getById(projectId).getPowerpointEmbedCode();
  }

  @Override
  public List<Project> getFavouriteProjects() {
    HashMap<Integer, Integer> mapOfFavouriteProjects = new HashMap<>();
    List<User> users = this.usersRepository.getAll();
    for (User user
            : users) {
      for (Project project
              : user.getFavoriteProjects()) {
        if (!mapOfFavouriteProjects.containsKey(project.getId())) {
          mapOfFavouriteProjects.put(project.getId(), 1);
        } else {
          mapOfFavouriteProjects.put(project.getId(),
                  mapOfFavouriteProjects.get(project.getId()) + 1);
        }
      }
    }

    Set<Map.Entry<Integer, Integer>> entries =
            mapOfFavouriteProjects.entrySet();

    Comparator<Map.Entry<Integer, Integer>> valueComparator = (e1, e2) -> {
      Integer v1 = e2.getValue();
      Integer v2 = e1.getValue();
      return v1.compareTo(v2);
    };

    List<Map.Entry<Integer, Integer>> listOfEntries = new ArrayList<>(entries);
    listOfEntries.sort(valueComparator);

    List<Project> result = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Map.Entry<Integer, Integer> entry = listOfEntries.get(i);
      Project project = this.projectsRepository.getById(entry.getKey());
      if (!project.isTopProject()) {
        project.setTopProject(true);
        this.projectsRepository.update(project);
      }

      result.add(project);
    }

    return result;
  }

  @Override
  public Update createNewUpdate(int projectId,
                                String title,
                                String content,
                                String date,
                                String type) {

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    Date dateTest = null;
    try {
      dateTest = sdf.parse(date);
    } catch (ParseException e) {
      System.out.println("String is not valid date");
      return new Update(0);
    }

    Project project = this.projectsRepository.getById(projectId);

    return this.updatesRepository.create(new Update(title,
            content,
            date,
            type,
            project));
  }

  @Override
  public WantedMember getWantedMember(int id) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    return this.wantedMembersRepository.getById(id);
  }

  @Override
  public ResponseSuccessful addVisit(int projectId) {
    if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    }

    Project projectToUpdate = this.projectsRepository.getById(projectId);

    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    Date today = Calendar.getInstance().getTime();

    this.visitsRepository.create(new Visit(df.format(today), projectToUpdate));

    return new ResponseSuccessful(true);
  }

  @Override
  public UserProjects getProjectsOfUser(int userId) {
    if (!this.idValidator.isValid(userId)) {
      throw new InvalidParameterException("UserId is not valid");
    }

    List<Project> createdProjects = this.projectsRepository.getAll().stream()
            .filter(project -> project.getCreator() == userId)
            .collect(Collectors.toList());

    List<Project> memberOfProjects = this.projectsRepository.getAll().stream()
            .filter(project -> (project.getCreator() != userId)
                    && (project.getMembers().stream()
                    .anyMatch(member -> member.getId() == userId)))
            .collect(Collectors.toList());

    return new UserProjects(createdProjects, memberOfProjects);
  }

  @Override
  public ResponseSuccessful sendRejectMessage(int recipientId,
                                              String projectName,
                                              int messageToDeleteId,
                                              int creatorId,
                                              boolean isInvite) {
    if (!this.idValidator.isValid(recipientId)) {
      throw new InvalidParameterException("Recipient id is not valid");
    } else if (projectName.isEmpty()) {
      throw new InvalidParameterException("Project name is empty");
    } else if (!this.idValidator.isValid(messageToDeleteId)) {
      throw new InvalidParameterException("MessageToDeleteId is not valid");
    } else if (!this.idValidator.isValid(creatorId)) {
      throw new InvalidParameterException("CreatorId is not valid");
    }

    Project appliedProject = this.getProjectByName(projectName);
    User newMember = this.usersRepository.getById(recipientId);
    User creatorOfProject = this.usersRepository.getById(creatorId);

    Message messageToInsert;
    if (!isInvite) {
      messageToInsert = this.writeMessage(appliedProject.getName(),
              creatorOfProject,
              newMember,
              "rejectionMessage",
              "",
              "",
              "",
              "",
              "",
              "");
    } else {
      messageToInsert = this.writeMessage(appliedProject.getName(),
              creatorOfProject,
              newMember,
              "rejectInviteMessage",
              "",
              "",
              "",
              creatorOfProject.getFirstName(),
              creatorOfProject.getLastName(),
              "");
    }

    Message messageToDelete = this.messagesRepository
            .getById(messageToDeleteId);
    this.messagesRepository.delete(messageToDelete);

    Notification notificationToInsert = this
            .writeNotification(appliedProject.getName(),
                    creatorOfProject,
                    newMember,
                    "rejection-message",
                    "",
                    "");

    this.notificationsRepository.create(notificationToInsert);
    this.messagesRepository.create(messageToInsert);

    return new ResponseSuccessful(true);
  }

  @Override
  public ResponseSuccessful sendMessageToAllMembers(int projectId,
                                                    SentMessage message) {
    if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    } else if (message.getTopic().isEmpty()) {
      throw new InvalidParameterException("Topic is empty");
    }

    Project project = this.projectsRepository.getById(projectId);
    User projectCreator = this.usersRepository.getById(project.getCreator());
    for (int i = 0; i < project.getMembers().size(); i++) {
      User recipient = project.getMembers().get(i);

      if (recipient.getId() != projectCreator.getId()) {
        Message messageToInsert = this.writeMessage(project.getName(),
                projectCreator,
                recipient,
                "normal",
                message.getTopic(),
                message.getContent(),
                "",
                "",
                "",
                "");

        Notification notificationToInsert =
                this.writeNotification(project.getName(),
                        projectCreator,
                        recipient,
                        "normal",
                        projectCreator.getFirstName(),
                        projectCreator.getLastName());

        this.messagesRepository.create(messageToInsert);
        this.notificationsRepository.create(notificationToInsert);
      }
    }

    return new ResponseSuccessful(true);
  }

  @Override
  public ResponseSuccessful sendAdvice(SentAdvice advice) {
    if (!this.idValidator.isValid(advice.getProjectId())) {
      throw new InvalidParameterException("Project id is not valid");
    } else if (advice.getTopic().isEmpty()) {
      throw new InvalidParameterException("Topic is empty");
    } else if (!this.idValidator.isValid(advice.getSenderId())) {
      throw new InvalidParameterException("Sender id is not valid");
    }

    Project project = this.projectsRepository.getById(advice.getProjectId());
    User sender = this.usersRepository.getById(advice.getSenderId());
    Advice adviceToInsert = this.createAdvice(advice.getTopic(),
            advice.getContent(),
            sender,
            project);

    // Notification for the project owner
    Notification notificationToInsert =
            this.writeNotification(project.getName(),
                    sender,
                    this.usersRepository.getById(project.getCreator()),
                    "normal-advice",
                    sender.getFirstName(),
                    sender.getLastName());

    this.advicesRepository.create(adviceToInsert);
    this.notificationsRepository.create(notificationToInsert);

    return new ResponseSuccessful(true);
  }

  @Override
  public ResponseSuccessful sendQuestion(SentQuestion question) {
    if (!this.idValidator.isValid(question.getProjectId())) {
      throw new InvalidParameterException("Project id is not valid");
    } else if (!this.idValidator.isValid(question.getSenderId())) {
      throw new InvalidParameterException("Sender id is not valid");
    } else if (question.getTopic().isEmpty()) {
      throw new InvalidParameterException("Topic is empty");
    }

    Project project = this.projectsRepository.getById(question.getProjectId());
    User sender = this.usersRepository.getById(question.getSenderId());
    Question questionToInsert = this.createQuestion(question.getTopic(),
            question.getContent(),
            sender,
            project);

    // Notification for the project owner
    Notification notificationToInsert =
            this.writeNotification(project.getName(),
                    sender,
                    this.usersRepository.getById(project.getCreator()),
                    "normal-question",
                    sender.getFirstName(),
                    sender.getLastName());

    this.questionsRepository.create(questionToInsert);
    this.notificationsRepository.create(notificationToInsert);

    return new ResponseSuccessful(true);
  }

  @Override
  public ResponseProjectId editProject(CreateProject project) {
    if (!this.idValidator.isValid(project.getId())) {
      throw new InvalidParameterException("Project id is not valid");
    } else if (!this.projectValidator.isValid(project)) {
      throw new InvalidParameterException("Project is not valid");
    }

    Project projectToEdit = this.projectsRepository.getById(project.getId());
    projectToEdit.setName(project.getName());
    projectToEdit.setDescription(project.getDescription());
    projectToEdit.setGoal1(project.getGoal1());
    projectToEdit.setGoal2(project.getGoal2());
    projectToEdit.setGoal3(project.getGoal3());
    projectToEdit.setGithubPage(project.getGithubPage());
    projectToEdit.setYoutubeLink(project.getYoutubeLink());
    if (project.getProjectNeeds() != null) {
      List<UserCategory> projectNeedsToInsert = new ArrayList<>();
      for (int i = 0; i < project.getProjectNeeds().size(); i++) {
        if (project.getProjectNeeds().get(i).getName() != null
                && !Objects.equals(project.getProjectNeeds().get(i)
                .getName(), "")) {
          UserCategory projectNeedToInsert = this.getUserCategory(
                  project.getProjectNeeds().get(i).getName());
          projectNeedsToInsert.add(projectNeedToInsert);
        }
      }

      projectToEdit.setProjectNeeds(projectNeedsToInsert);
    }

    projectToEdit.setWantedMembers(this.createWantedMembers(
            project.getParameters(),
            projectToEdit));
    this.projectsRepository.update(projectToEdit);

    return new ResponseProjectId(projectToEdit.getId());
  }

  // Returns all projects that need a user with the specified skillset
  @Override
  public List<ProjectWithName> getAvailableProjectsForInvite(int ownerId,
                                                             String skillset) {
    System.out.println(skillset);
    if (!this.idValidator.isValid(ownerId)) {
      throw new InvalidParameterException("OwnerId is not valid");
    } else if (skillset.isEmpty()) {
      throw new InvalidParameterException("Skillset is empty");
    }

    List<Project> allProjects = this.projectsRepository.getAll();
    allProjects = allProjects.stream()
            .filter(project -> project.getCreator()
                    == ownerId && project.getProjectNeeds()
                    .stream()
                    .anyMatch(projectNeed -> projectNeed.getName()
                            .equals(skillset)))
            .collect(Collectors.toList());

    List<ProjectWithName> result = new ArrayList<>();
    for (Project allProject : allProjects) {
      result.add(new ProjectWithName(allProject.getId(), allProject.getName()));
    }

    return result;
  }

  @Override
  public Project getProjectByName(String projectName) {
    if (projectName.isEmpty()) {
      throw new InvalidParameterException("ProjectName is empty");
    }

    return this.projectsRepository.getAll().stream()
            .filter(pr -> pr.getName().equals(projectName))
            .findFirst()
            .orElse(null);
  }

  @Override
  public ResponseSuccessful sendMessageForApproval(int projectId,
                                                   int newMemberId,
                                                   int creatorId) {
    if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    } else if (!this.idValidator.isValid(newMemberId)) {
      throw new InvalidParameterException("NewMemberId is not valid");
    } else if (!this.idValidator.isValid(creatorId)) {
      throw new InvalidParameterException("CreatorId is not valid");
    }

    Project appliedProject = this.projectsRepository.getById(projectId);
    User newMember = this.usersRepository.getById(newMemberId);
    if (!this.doesProjectNeedThisTypeOfUser(appliedProject.getProjectNeeds(),
            newMember.getSkillset())) {
      return new ResponseSuccessful(false);
    }

    User creatorOfProject = this.usersRepository.getById(creatorId);

    String skillset = newMember.getSkillset().getName()
            .substring(0, 1).toLowerCase()
            + newMember.getSkillset().getName().substring(1);
    Message messageToInsert = this.writeMessage(appliedProject.getName(),
            newMember,
            creatorOfProject,
            "requestToJoin",
            "",
            "",
            creatorOfProject.getFirstName(),
            newMember.getFirstName(),
            newMember.getLastName(),
            skillset);
    this.messagesRepository.create(messageToInsert);

    Notification notificationToInsert =
            this.writeNotification(appliedProject.getName(),
                    newMember,
                    creatorOfProject,
                    "approval-message",
                    "",
                    "");

    this.notificationsRepository.create(notificationToInsert);

//    this.call(appliedProject.getTelephone().substring(1));

    return new ResponseSuccessful(true);
  }

  @Override
  public ResponseSuccessful sendInviteMessage(int projectId,
                                              int recipient,
                                              int sender) {
    if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    } else if (!this.idValidator.isValid(recipient)) {
      throw new InvalidParameterException("Recipient is not valid");
    } else if (!this.idValidator.isValid(sender)) {
      throw new InvalidParameterException("Sender is not valid");
    }

    Project appliedProject = this.projectsRepository.getById(projectId);
    User newMember = this.usersRepository.getById(recipient);
    if (!this.doesProjectNeedThisTypeOfUser(
            appliedProject.getProjectNeeds(),
            newMember.getSkillset())) {
      return new ResponseSuccessful(false);
    }

    User creatorOfProject = this.usersRepository.getById(sender);

    String skillset = newMember.getSkillset().getName()
            .substring(0, 1).toLowerCase()
            + newMember.getSkillset().getName().substring(1);
    Message messageToInsert = this.writeMessage(appliedProject.getName(),
            creatorOfProject,
            newMember,
            "inviteToJoin",
            "",
            "",
            newMember.getFirstName(),
            creatorOfProject.getFirstName(),
            creatorOfProject.getLastName(),
            skillset);
    this.messagesRepository.create(messageToInsert);

    Notification notificationToInsert =
            this.writeNotification(appliedProject.getName(),
                    creatorOfProject,
                    newMember,
                    "invite-message",
                    "",
                    "");

    this.notificationsRepository.create(notificationToInsert);

//    this.call(appliedProject.getTelephone().substring(1));

    return new ResponseSuccessful(true);
  }

  // Reports a project to the project owner by email
  @Override
  public boolean reportProject(String category, int id) {
    Properties props = new Properties();
    props.put("mail.smtp.host", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.auth", "true");
    Session session = Session
            .getInstance(props, new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        "hristian00i.dev@gmail.com", EMAIL_PASS);
              }
            });
    try {
      MimeMessage msg = new MimeMessage(session);
      String to = "hristian00i@abv.bg";
      InternetAddress[] address = InternetAddress.parse(to, true);
      msg.setRecipients(javax.mail.Message.RecipientType.TO, address);
      msg.setSubject("Докладване за проект");
      msg.setSentDate(new Date());
      msg.setText("Проект с идентификационен номер "
              + id + " беше сигнализаран "
              + "от потребител, "
              + "че нарушава следните"
              + " категории от правилата: " + category);
      msg.setHeader("XPriority", "1");
      Transport.send(msg);
      System.out.println("Mail has been sent successfully");
    } catch (MessagingException mex) {
      System.out.println("Unable to send an email" + mex);
    }

    return true;
  }

  @Override
  public boolean addFavorite(int userId, int projectId) {
    if (!this.idValidator.isValid(userId)) {
      throw new InvalidParameterException("UserId is not valid");
    } else if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    }

    User user = this.usersRepository.getById(userId);
    List<Project> favorites = user.getFavoriteProjects();
    favorites.add(this.getProjectById(projectId));

    user.setFavoriteProjects(favorites);

    this.usersRepository.update(user);

    return true;
  }

  @Override
  public boolean removeFavorite(int userId, int projectId) {
    if (!this.idValidator.isValid(userId)) {
      throw new InvalidParameterException("UserId is not valid");
    } else if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    }

    User user = this.usersRepository.getById(userId);
    List<Project> favorites = user.getFavoriteProjects();
    favorites.remove(this.getProjectById(projectId));

    user.setFavoriteProjects(favorites);

    this.usersRepository.update(user);

    return true;
  }

  @Override
  public boolean isUserMember(User user, Auction auction) {
    Project project = this.projectsRepository.getById(auction.getProjectId());
    if (user.getId() == project.getCreator()) {
      return false;
    }

    for (int i = 0; i < project.getMembers().size(); i++) {
      if (project.getMembers().get(i).getId() == user.getId()) {
        return true;
      }
    }

    return false;
  }

  // The number of people that are currently working on the project
  @Override
  public int getWorkingOnProject(int projectId) {
    if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    }

    return this.projectsRepository.getById(projectId).getMembers().size();
  }

  @Override
  public String getDescription(int projectId) {
    if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    }

    return this.projectsRepository.getById(projectId).getDescription();
  }

  @Override
  public ResponseSuccessful addProjectMember(String projectName,
                                             int newMemberId,
                                             int messageId,
                                             boolean isInvite) {
    if (projectName.isEmpty()) {
      throw new InvalidParameterException("ProjectName is not valid");
    } else if (!this.idValidator.isValid(newMemberId)) {
      throw new InvalidParameterException("NewMemberId is not valid");
    } else if (!this.idValidator.isValid(messageId)) {
      throw new InvalidParameterException("MessageId is not valid");
    }

    Project projectToUpdate = this.getProjectByName(projectName);
    User userToAdd = this.usersRepository.getById(newMemberId);

    this.removeIfFavorite(userToAdd, projectToUpdate);

    List<User> members = projectToUpdate.getMembers();
    members.add(userToAdd);
    projectToUpdate.setMembers(members);

    if (projectToUpdate.getProjectNeeds() != null) {
      for (int i = 0; i < projectToUpdate.getProjectNeeds().size(); i++) {
        if (projectToUpdate.getProjectNeeds().get(i).getName()
                .equals(userToAdd.getSkillset().getName())) {
          projectToUpdate.getProjectNeeds().remove(i);
          break;
        }
      }
    }

    if (projectToUpdate.getWantedMembers() != null) {
      for (int i = 0; i < projectToUpdate.getWantedMembers().size(); i++) {
        if (projectToUpdate.getWantedMembers().get(i)
                .getUserCategory().getName()
                .equals(userToAdd.getSkillset().getName())) {
          this.wantedMembersRepository.delete(
                  projectToUpdate.getWantedMembers().get(i));
          projectToUpdate.getWantedMembers().remove(i);
          break;
        }
      }
    }

    Message messageToDelete = this.messagesRepository.getById(messageId);
    this.messagesRepository.delete(messageToDelete);

    this.projectsRepository.update(projectToUpdate);

    User creatorOfProject = this.usersRepository
            .getById(projectToUpdate.getCreator());

    Message messageToInsert;
    if (!isInvite) {
      messageToInsert = this.writeMessage(projectToUpdate.getName(),
              creatorOfProject,
              userToAdd,
              "approvedMessage",
              "",
              "",
              "",
              "",
              "",
              "");
    } else {
      messageToInsert = this.writeMessage(projectToUpdate.getName(),
              userToAdd,
              creatorOfProject,
              "approvedMessageInvite",
              "",
              "",
              "",
              userToAdd.getFirstName(),
              userToAdd.getLastName(),
              "");
    }

    this.messagesRepository.create(messageToInsert);

    Notification notificationToInsert;
    if (!isInvite) {
      notificationToInsert = this.writeNotification(projectToUpdate.getName(),
              creatorOfProject,
              userToAdd,
              "approved-new-member",
              "",
              "");
    } else {
      notificationToInsert = this.writeNotification(projectToUpdate.getName(),
              userToAdd,
              creatorOfProject,
              "approved-new-member-invite",
              "",
              "");
    }

    this.notificationsRepository.create(notificationToInsert);

    return new ResponseSuccessful(true);
  }

  private void removeIfFavorite(User user, Project projectToUpdate) {
    for (int i = 0; i < user.getFavoriteProjects().size(); i++) {
      if (user.getFavoriteProjects().get(i).getId()
              == projectToUpdate.getId()) {
        user.getFavoriteProjects().remove(projectToUpdate);
      }
    }

    this.usersRepository.update(user);
  }

  @Override
  public ResponseSuccessful removeProjectMember(int projectId, int memberId) {
    if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    } else if (!this.idValidator.isValid(memberId)) {
      throw new InvalidParameterException("MemberId is not valid");
    }

    Project projectToUpdate = this.getProjectById(projectId);
    List<User> members = projectToUpdate.getMembers();
    for (int i = 0; i < members.size(); i++) {
      if (members.get(i).getId() == memberId) {
        members.remove(i);
      }
    }

    projectToUpdate.setMembers(members);

    this.projectsRepository.update(projectToUpdate);

    User creatorOfProject = this.usersRepository
            .getById(projectToUpdate.getCreator());
    User recipient = this.usersRepository.getById(memberId);

    // Notifies the removed user by message and notification
    Message messageToInsert = this.writeMessage(projectToUpdate.getName(),
            recipient,
            creatorOfProject,
            "leftMessage",
            "",
            "",
            "",
            recipient.getFirstName(),
            recipient.getLastName(),
            "");

    this.messagesRepository.create(messageToInsert);

    Notification notificationToInsert =
            this.writeNotification(projectToUpdate.getName(),
                    recipient,
                    creatorOfProject,
                    "project-leave",
                    "",
                    "");

    this.notificationsRepository.create(notificationToInsert);

    return new ResponseSuccessful(true);
  }

  @Override
  public ResponsePagination getProjectsPages(String name,
                                             String userCategory,
                                             String city,
                                             boolean isByMap,
                                             double latitude,
                                             double longitude,
                                             int radius) {
    List<ProjectShort> projects = this.projectsShortRepository.getAll();
    projects = this.buildWhereClauses(projects,
            name,
            userCategory,
            city,
            isByMap,
            latitude,
            longitude,
            radius);

    int numberOfPages = (int) Math.ceil(projects.size() / PAGE_LENGTH_DOUBLE);

    return new ResponsePagination(numberOfPages, projects.size());
  }

  @Override
  public List<ProjectShort> getProjects(int page,
                                        String name,
                                        String userCategory,
                                        String city,
                                        boolean isByMap,
                                        double latitude,
                                        double longitude,
                                        int radius) {
    List<ProjectShort> projects = this.projectsShortRepository.getAll();
    projects = this.buildWhereClauses(projects,
            name,
            userCategory,
            city,
            isByMap,
            latitude,
            longitude,
            radius);

    projects.sort(ProjectShort.TOP_PROJECTS_FIRST);

    int fromIndex = (page - 1) * PAGE_LENGTH;
    int toIndex = fromIndex + PAGE_LENGTH;

    if (toIndex > projects.size()) {
      toIndex = projects.size();
    }

    return projects.subList(fromIndex, toIndex);
  }

  @Override
  public ResponseProjectId createProject(CreateProject project, int creatorId) {
    if (!this.idValidator.isValid(creatorId)) {
      throw new InvalidParameterException("CreatorId is not valid");
    } else if (!this.projectValidator.isValid(project)) {
      throw new InvalidParameterException("Project is not valid");
    }

    Project projectToInsert = new Project();
    projectToInsert.setName(project.getName());
    projectToInsert.setDescription(project.getDescription());
    projectToInsert.setGoal1(project.getGoal1());
    projectToInsert.setGoal2(project.getGoal2());
    projectToInsert.setGoal3(project.getGoal3());
    projectToInsert.setGithubPage(project.getGithubPage());
    projectToInsert.setCity(project.getCity());

    List<UserCategory> projectNeedsToInsert = new ArrayList<>();
    for (int i = 0; i < project.getProjectNeeds().size(); i++) {
      if (project.getProjectNeeds().get(i).getName() != null
              && !Objects.equals(project
              .getProjectNeeds().get(i).getName(), "")) {
        UserCategory projectNeedToInsert = this.getUserCategory(
                project.getProjectNeeds().get(i).getName());
        projectNeedsToInsert.add(projectNeedToInsert);
      }
    }

    projectToInsert.setProjectNeeds(projectNeedsToInsert);
    List<User> membersToInsert = new ArrayList<>();
    User creator = this.usersRepository.getById(creatorId);
    membersToInsert.add(creator);
    projectToInsert.setMembers(membersToInsert);
    projectToInsert.setCreator(creatorId);
    projectToInsert.setTelephone(project.getTelephone());

    projectToInsert.setYoutubeLink(project.getYoutubeLink());

    if (!project.isNeedsMoney()) {
      projectToInsert.setNeedsMoney(false);
      projectToInsert.setNeededMoney(0.0);
    } else {
      projectToInsert.setNeedsMoney(true);
      projectToInsert.setNeededMoney(project.getNeededMoney());
    }

    projectToInsert.setReceivedMoney(0.0);
    projectToInsert.setHasReservedPlace(false);
    projectToInsert.setDaysTop(0);
    projectToInsert.setLatitude(project.getLatitude());
    projectToInsert.setLongitude(project.getLongitude());
    projectToInsert.setPowerpointEmbedCode("");
    projectToInsert.setTimesBacked(0);
    projectToInsert.setTopProject(false);

    Update projectLaunchedUpdate = new Update();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    Calendar calendar = Calendar.getInstance();
    Date now = calendar.getTime();
    String date = sdf.format(now);
    projectLaunchedUpdate.setDate(date);
    projectLaunchedUpdate.setContent("Project launched");
    projectLaunchedUpdate.setType("Initial update");
    projectLaunchedUpdate.setTitle("Project launched");
    projectLaunchedUpdate.setProject(projectToInsert);

    this.projectsRepository.create(projectToInsert);

    projectToInsert.setWantedMembers(
            this.createWantedMembers(project.getParameters(),
                    projectToInsert));

    this.projectsRepository.update(projectToInsert);

    this.updatesRepository.create(projectLaunchedUpdate);

    return new ResponseProjectId(projectToInsert.getId());
  }

  public List<WantedMember> createWantedMembers(List<Parameter> parameters,
                                                Project project) {
    System.out.println(project);
    List<WantedMember> result = new ArrayList<>();
    WantedMember programmer = null;
    WantedMember designer = null;
    WantedMember engineer = null;
    WantedMember writer = null;
    WantedMember scientist = null;
    WantedMember musician = null;
    WantedMember filmmaker = null;
    WantedMember productManager = null;
    WantedMember artist = null;

    //region Filling WantedMembers
    for (Parameter parameter
            : parameters) {
      if (parameter.getValues().size() != 0) {
        switch (parameter.getCategory()) {
          case "programmer":
            if (programmer == null) {
              programmer = new WantedMember();
            }

            switch (parameter.getParamCategory()) {
              case "skills":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  Skill skillToInsert = this.getSkill(
                          parameter.getValues().get(i));
                  List<Skill> skills = programmer.getSkills();
                  if (skills == null) {
                    skills = new ArrayList<>();
                  }

                  skills.add(skillToInsert);
                  programmer.setSkills(skills);
                }
                break;
              case "languages":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  Language languageToInsert = this.getLanguage(
                          parameter.getValues().get(i));
                  List<Language> languages = programmer.getLanguages();
                  if (languages == null) {
                    languages = new ArrayList<>();
                  }

                  languages.add(languageToInsert);
                  programmer.setLanguages(languages);
                }
                break;
              case "city":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  City cityToInsert = this.getCity(
                          parameter.getValues().get(i));
                  programmer.setCity(cityToInsert);
                }
                break;
              case "education":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  int educationToInsert = Integer.parseInt(
                          parameter.getValues().get(i));
                  switch (educationToInsert) {
                    case 1:
                      programmer.setEducation("В училище");
                      break;
                    case 2:
                      programmer.setEducation("Професионален"
                              + " бакалавър");
                      break;
                    case 3:
                      programmer.setEducation("Бакалавър");
                      break;
                    case 4:
                      programmer.setEducation("Магистър");
                      break;
                    case 5:
                      programmer.setEducation("Доктор");
                      break;
                  }
                }
                break;
            }

            break;
          case "designer":
            if (designer == null) {
              designer = new WantedMember();
            }

            switch (parameter.getParamCategory()) {
              case "skills":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  Skill skillToInsert = this.getSkill(
                          parameter.getValues().get(i));
                  List<Skill> skills = designer.getSkills();
                  if (skills == null) {
                    skills = new ArrayList<>();
                  }

                  skills.add(skillToInsert);
                  designer.setSkills(skills);
                }
                break;
              case "languages":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  Language languageToInsert = this.getLanguage(
                          parameter.getValues().get(i));
                  List<Language> languages = designer.getLanguages();
                  if (languages == null) {
                    languages = new ArrayList<>();
                  }

                  languages.add(languageToInsert);
                  designer.setLanguages(languages);
                }
                break;
              case "city":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  City cityToInsert = this.getCity(
                          parameter.getValues().get(i));
                  designer.setCity(cityToInsert);
                }
                break;
              case "education":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  int educationToInsert = Integer.parseInt(
                          parameter.getValues().get(i));
                  switch (educationToInsert) {
                    case 1:
                      designer.setEducation("В училище");
                      break;
                    case 2:
                      designer.setEducation("Професионален"
                              + " бакалавър");
                      break;
                    case 3:
                      designer.setEducation("Бакалавър");
                      break;
                    case 4:
                      designer.setEducation("Магистър");
                      break;
                    case 5:
                      designer.setEducation("Доктор");
                      break;
                  }
                }
                break;
            }

            break;
          case "engineer":
            if (engineer == null) {
              engineer = new WantedMember();
            }

            switch (parameter.getParamCategory()) {
              case "skills":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  Skill skillToInsert = this.getSkill(
                          parameter.getValues().get(i));
                  List<Skill> skills = engineer.getSkills();
                  if (skills == null) {
                    skills = new ArrayList<>();
                  }

                  skills.add(skillToInsert);
                  engineer.setSkills(skills);
                }
                break;
              case "languages":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  Language languageToInsert = this.getLanguage(
                          parameter.getValues().get(i));
                  List<Language> languages = engineer.getLanguages();
                  if (languages == null) {
                    languages = new ArrayList<>();
                  }

                  languages.add(languageToInsert);
                  engineer.setLanguages(languages);
                }
                break;
              case "city":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  City cityToInsert = this.getCity(
                          parameter.getValues().get(i));
                  engineer.setCity(cityToInsert);
                }
                break;
              case "education":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  int educationToInsert = Integer.parseInt(
                          parameter.getValues().get(i));
                  switch (educationToInsert) {
                    case 1:
                      engineer.setEducation("В училище");
                      break;
                    case 2:
                      engineer.setEducation("Професионален"
                              + " бакалавър");
                      break;
                    case 3:
                      engineer.setEducation("Бакалавър");
                      break;
                    case 4:
                      engineer.setEducation("Магистър");
                      break;
                    case 5:
                      engineer.setEducation("Доктор");
                      break;
                  }
                }
                break;
            }

            break;
          case "writer":
            if (writer == null) {
              writer = new WantedMember();
            }

            switch (parameter.getParamCategory()) {
              case "skills":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  Skill skillToInsert = this.getSkill(
                          parameter.getValues().get(i));
                  List<Skill> skills = writer.getSkills();
                  if (skills == null) {
                    skills = new ArrayList<>();
                  }

                  skills.add(skillToInsert);
                  writer.setSkills(skills);
                }
                break;
              case "languages":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  Language languageToInsert = this.getLanguage(
                          parameter.getValues().get(i));
                  List<Language> languages = writer.getLanguages();
                  if (languages == null) {
                    languages = new ArrayList<>();
                  }

                  languages.add(languageToInsert);
                  writer.setLanguages(languages);
                }
                break;
              case "city":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  City cityToInsert = this.getCity(
                          parameter.getValues().get(i));
                  writer.setCity(cityToInsert);
                }
                break;
              case "education":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  int educationToInsert = Integer.parseInt(
                          parameter.getValues().get(i));
                  switch (educationToInsert) {
                    case 1:
                      writer.setEducation("В училище");
                      break;
                    case 2:
                      writer.setEducation("Професионален"
                              + " бакалавър");
                      break;
                    case 3:
                      writer.setEducation("Бакалавър");
                      break;
                    case 4:
                      writer.setEducation("Магистър");
                      break;
                    case 5:
                      writer.setEducation("Доктор");
                      break;
                  }
                }
                break;
            }

            break;
          case "scientist":
            if (scientist == null) {
              scientist = new WantedMember();
            }

            switch (parameter.getParamCategory()) {
              case "skills":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  Skill skillToInsert = this.getSkill(
                          parameter.getValues().get(i));
                  List<Skill> skills = scientist.getSkills();
                  if (skills == null) {
                    skills = new ArrayList<>();
                  }

                  skills.add(skillToInsert);
                  scientist.setSkills(skills);
                }
                break;
              case "languages":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  Language languageToInsert = this.getLanguage(
                          parameter.getValues().get(i));
                  List<Language> languages = scientist.getLanguages();
                  if (languages == null) {
                    languages = new ArrayList<>();
                  }

                  languages.add(languageToInsert);
                  scientist.setLanguages(languages);
                }
                break;
              case "city":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  City cityToInsert = this.getCity(
                          parameter.getValues().get(i));
                  scientist.setCity(cityToInsert);
                }
                break;
              case "education":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  int educationToInsert = Integer.parseInt(
                          parameter.getValues().get(i));
                  switch (educationToInsert) {
                    case 1:
                      scientist.setEducation("В училище");
                      break;
                    case 2:
                      scientist.setEducation("Професионален"
                              + " бакалавър");
                      break;
                    case 3:
                      scientist.setEducation("Бакалавър");
                      break;
                    case 4:
                      scientist.setEducation("Магистър");
                      break;
                    case 5:
                      scientist.setEducation("Доктор");
                      break;
                  }
                }
                break;
            }

            break;
          case "musician":
            if (musician == null) {
              musician = new WantedMember();
            }

            switch (parameter.getParamCategory()) {
              case "skills":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  Skill skillToInsert = this.getSkill(
                          parameter.getValues().get(i));
                  List<Skill> skills = musician.getSkills();
                  if (skills == null) {
                    skills = new ArrayList<>();
                  }

                  skills.add(skillToInsert);
                  musician.setSkills(skills);
                }
                break;
              case "languages":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  Language languageToInsert = this.getLanguage(
                          parameter.getValues().get(i));
                  List<Language> languages = musician.getLanguages();
                  if (languages == null) {
                    languages = new ArrayList<>();
                  }

                  languages.add(languageToInsert);
                  musician.setLanguages(languages);
                }
                break;
              case "city":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  City cityToInsert = this.getCity(
                          parameter.getValues().get(i));
                  musician.setCity(cityToInsert);
                }
                break;
              case "education":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  int educationToInsert = Integer.parseInt(
                          parameter.getValues().get(i));
                  switch (educationToInsert) {
                    case 1:
                      musician.setEducation("В училище");
                      break;
                    case 2:
                      musician.setEducation("Професионален"
                              + " бакалавър");
                      break;
                    case 3:
                      musician.setEducation("Бакалавър");
                      break;
                    case 4:
                      musician.setEducation("Магистър");
                      break;
                    case 5:
                      musician.setEducation("Доктор");
                      break;
                  }
                }
                break;
            }

            break;
          case "filmmaker":
            if (filmmaker == null) {
              filmmaker = new WantedMember();
            }

            switch (parameter.getParamCategory()) {
              case "skills":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  Skill skillToInsert = this.getSkill(
                          parameter.getValues().get(i));
                  List<Skill> skills = filmmaker.getSkills();
                  if (skills == null) {
                    skills = new ArrayList<>();
                  }

                  skills.add(skillToInsert);
                  filmmaker.setSkills(skills);
                }
                break;
              case "languages":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  Language languageToInsert = this.getLanguage(
                          parameter.getValues().get(i));
                  List<Language> languages = filmmaker.getLanguages();
                  if (languages == null) {
                    languages = new ArrayList<>();
                  }

                  languages.add(languageToInsert);
                  filmmaker.setLanguages(languages);
                }
                break;
              case "city":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  City cityToInsert = this.getCity(
                          parameter.getValues().get(i));
                  filmmaker.setCity(cityToInsert);
                }
                break;
              case "education":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  int educationToInsert = Integer.parseInt(
                          parameter.getValues().get(i));
                  switch (educationToInsert) {
                    case 1:
                      filmmaker.setEducation("В училище");
                      break;
                    case 2:
                      filmmaker.setEducation("Професионален "
                              + "бакалавър");
                      break;
                    case 3:
                      filmmaker.setEducation("Бакалавър");
                      break;
                    case 4:
                      filmmaker.setEducation("Магистър");
                      break;
                    case 5:
                      filmmaker.setEducation("Доктор");
                      break;
                  }
                }
                break;
            }

            break;
          case "productManager":
            if (productManager == null) {
              productManager = new WantedMember();
            }

            switch (parameter.getParamCategory()) {
              case "skills":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  Skill skillToInsert = this.getSkill(
                          parameter.getValues().get(i));
                  List<Skill> skills = productManager.getSkills();
                  if (skills == null) {
                    skills = new ArrayList<>();
                  }

                  skills.add(skillToInsert);
                  productManager.setSkills(skills);
                }
                break;
              case "languages":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  Language languageToInsert = this.getLanguage(
                          parameter.getValues().get(i));
                  List<Language> languages = productManager.getLanguages();
                  if (languages == null) {
                    languages = new ArrayList<>();
                  }

                  languages.add(languageToInsert);
                  productManager.setLanguages(languages);
                }
                break;
              case "city":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  City cityToInsert = this.getCity(
                          parameter.getValues().get(i));
                  productManager.setCity(cityToInsert);
                }
                break;
              case "education":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  int educationToInsert = Integer.parseInt(
                          parameter.getValues().get(i));
                  switch (educationToInsert) {
                    case 1:
                      productManager.setEducation("В училище");
                      break;
                    case 2:
                      productManager.setEducation("Професионален"
                              + " бакалавър");
                      break;
                    case 3:
                      productManager.setEducation("Бакалавър");
                      break;
                    case 4:
                      productManager.setEducation("Магистър");
                      break;
                    case 5:
                      productManager.setEducation("Доктор");
                      break;
                  }
                }
                break;
            }

            break;
          case "artist":
            if (artist == null) {
              artist = new WantedMember();
            }

            switch (parameter.getParamCategory()) {
              case "skills":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  Skill skillToInsert = this.getSkill(
                          parameter.getValues().get(i));
                  List<Skill> skills = artist.getSkills();
                  if (skills == null) {
                    skills = new ArrayList<>();
                  }

                  skills.add(skillToInsert);
                  artist.setSkills(skills);
                }
                break;
              case "languages":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  Language languageToInsert = this.getLanguage(
                          parameter.getValues().get(i));
                  List<Language> languages = artist.getLanguages();
                  if (languages == null) {
                    languages = new ArrayList<>();
                  }

                  languages.add(languageToInsert);
                  artist.setLanguages(languages);
                }
                break;
              case "city":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  City cityToInsert = this.getCity(
                          parameter.getValues().get(i));
                  artist.setCity(cityToInsert);
                }
                break;
              case "education":
                for (int i = 0; i < parameter.getValues().size(); i++) {
                  int educationToInsert = Integer.parseInt(
                          parameter.getValues().get(i));
                  switch (educationToInsert) {
                    case 1:
                      artist.setEducation("В училище");
                      break;
                    case 2:
                      artist.setEducation("Професионален"
                              + " бакалавър");
                      break;
                    case 3:
                      artist.setEducation("Бакалавър");
                      break;
                    case 4:
                      artist.setEducation("Магистър");
                      break;
                    case 5:
                      artist.setEducation("Доктор");
                      break;
                  }
                }
                break;
            }

            break;
          default:
            break;
        }

      }
    }
    //endregion

    //region Finalizing setup of WantedMembers
    if (programmer != null && !programmer.isEmpty()) {
      programmer.setUserCategory(this.getUserCategory("Програмист"));
      programmer.setProject(project);
      result.add(programmer);

      if (this.hasProjectWantedMember(project, "Програмист")) {
        this.removeWantedMember(project, "Програмист");
      }

      this.wantedMembersRepository.create(programmer);
    } else {
      this.removeWantedMember(project, "Програмист");
    }

    if (designer != null && !designer.isEmpty()) {
      designer.setUserCategory(this.getUserCategory("Дизайнер"));
      designer.setProject(project);
      result.add(designer);

      if (this.hasProjectWantedMember(project, "Дизайнер")) {
        this.removeWantedMember(project, "Дизайнер");
      }

      this.wantedMembersRepository.create(designer);
    } else {
      this.removeWantedMember(project, "Дизайнер");
    }

    if (engineer != null && !engineer.isEmpty()) {
      engineer.setUserCategory(this.getUserCategory("Инженер"));
      engineer.setProject(project);
      result.add(engineer);

      if (this.hasProjectWantedMember(project, "Инженер")) {
        this.removeWantedMember(project, "Инженер");
      }

      this.wantedMembersRepository.create(engineer);
    } else {
      this.removeWantedMember(project, "Инженер");
    }

    if (writer != null && !writer.isEmpty()) {
      writer.setUserCategory(this.getUserCategory("Писател"));
      writer.setProject(project);
      result.add(writer);

      if (this.hasProjectWantedMember(project, "Писател")) {
        this.removeWantedMember(project, "Писател");
      }

      this.wantedMembersRepository.create(writer);
    } else {
      this.removeWantedMember(project, "Писател");
    }

    if (scientist != null && !scientist.isEmpty()) {
      scientist.setUserCategory(this.getUserCategory("Учен"));
      scientist.setProject(project);
      result.add(scientist);

      if (this.hasProjectWantedMember(project, "Учен")) {
        this.removeWantedMember(project, "Учен");
      }

      this.wantedMembersRepository.create(scientist);
    } else {
      this.removeWantedMember(project, "Учен");
    }

    if (musician != null && !musician.isEmpty()) {
      musician.setUserCategory(this.getUserCategory("Музикант"));
      musician.setProject(project);
      result.add(musician);

      if (this.hasProjectWantedMember(project, "Музикант")) {
        this.removeWantedMember(project, "Музикант");
      }

      this.wantedMembersRepository.create(musician);
    } else {
      this.removeWantedMember(project, "Музикант");
    }

    if (filmmaker != null && !filmmaker.isEmpty()) {
      filmmaker.setUserCategory(this.getUserCategory("Режисьор"));
      filmmaker.setProject(project);
      result.add(filmmaker);

      if (this.hasProjectWantedMember(project, "Режисьор")) {
        this.removeWantedMember(project, "Режисьор");
      }

      this.wantedMembersRepository.create(filmmaker);
    } else {
      this.removeWantedMember(project, "Режисьор");
    }

    if (productManager != null && !productManager.isEmpty()) {
      productManager.setUserCategory(
              this.getUserCategory("Продуктов мениджър"));
      productManager.setProject(project);
      result.add(productManager);

      if (this.hasProjectWantedMember(project,
              "Продуктов мениджър")) {
        this.removeWantedMember(project, "Продуктов мениджър");
      }

      this.wantedMembersRepository.create(productManager);
    } else {
      this.removeWantedMember(project, "Продуктов мениджър");
    }

    if (artist != null && !artist.isEmpty()) {
      artist.setUserCategory(this.getUserCategory("Артист"));
      artist.setProject(project);
      result.add(artist);

      if (this.hasProjectWantedMember(project, "Артист")) {
        this.removeWantedMember(project, "Артист");
      }

      this.wantedMembersRepository.create(artist);
    } else {
      this.removeWantedMember(project, "Артист");
    }
    //endregion

    return result;
  }

  private boolean hasProjectWantedMember(Project project, String category) {
    for (WantedMember wantedMember
            : project.getWantedMembers()) {
      if (wantedMember.getUserCategory().getName().equals(category)) {
        return true;
      }
    }

    return false;
  }

  private WantedMember removeWantedMember(Project project, String category) {
    if (project.getWantedMembers() != null) {
      for (int i = 0; i < project.getWantedMembers().size(); i++) {
        if (project.getWantedMembers().get(i)
                .getUserCategory().getName().equals(category)) {
          return this.wantedMembersRepository
                  .delete(project.getWantedMembers().get(i));
        }
      }
    }

    return null;
  }

  private Skill getSkill(String skill) {
    return this.skillsRepository.getAll().stream()
            .filter(sk -> sk.getName().equals(skill))
            .findFirst()
            .orElse(null);
  }

  private Language getLanguage(String language) {
    return this.languagesRepository.getAll().stream()
            .filter(lang -> lang.getName().equals(language))
            .findFirst()
            .orElse(null);
  }

  private City getCity(String city) {
    return this.citiesRepository.getAll().stream()
            .filter(cit -> cit.getName().equals(city))
            .findFirst()
            .orElse(null);
  }

  @Override
  public ResponseUpload uploadPictures(MultipartFile multipart,
                                       int id,
                                       String fileName) throws IOException {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    String path = FileUtils.convertToFilepath(multipart);

    Project project = this.getProjectById(id);

    int indexOf = path.indexOf("/static");
    if (indexOf != -1) {
      path = path.substring(path.indexOf("/static"));
    }

    switch (fileName) {
      case "1":
        FileUtils.deleteFile(project.getPicture1());
        project.setPicture1(path);
        break;
      case "2":
        FileUtils.deleteFile(project.getPicture2());
        project.setPicture2(path);
        break;
      case "3":
        FileUtils.deleteFile(project.getPicture3());
        project.setPicture3(path);
        break;
      case "4":
        FileUtils.deleteFile(project.getPicture4());
        project.setPicture4(path);
        break;
      case "5":
        FileUtils.deleteFile(project.getPicture5());
        project.setPicture5(path);
        break;
      case "6":
        FileUtils.deleteFile(project.getPicture6());
        project.setPicture6(path);
        break;
      case "7":
        FileUtils.deleteFile(project.getPicture7());
        project.setPicture7(path);
        break;
      case "8":
        FileUtils.deleteFile(project.getPicture8());
        project.setPicture8(path);
        break;
      case "9":
        FileUtils.deleteFile(project.getPicture9());
        project.setPicture9(path);
        break;
      default:
        throw new InvalidParameterException("File name is not valid");
    }

    this.projectsRepository.update(project);

    return new ResponseUpload(true);
  }

  @Override
  public ResponseUpload uploadImage(MultipartFile multipart,
                                    int id) throws IOException {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    String path = FileUtils.convertToFilepath(multipart);
    int indexOf = path.indexOf("/static");
    if (indexOf != -1) {
      path = path.substring(path.indexOf("/static"));

      Project project = this.getProjectById(id);

      FileUtils.deleteFile(project.getCover());
      project.setCover(path);

      this.projectsRepository.update(project);
      return new ResponseUpload(false);
    }

    return new ResponseUpload(true);
  }

  @Override
  public ResponseCheckProjectName checkProjectName(String name) {
    if (name.isEmpty()) {
      throw new InvalidParameterException("Name is empty");
    }

    Project project = this.projectsRepository.getAll().stream()
            .filter(pr -> pr.getName().equals(name))
            .findFirst()
            .orElse(null);

    if (project != null) {
      return new ResponseCheckProjectName(STATUS_BAD_REQUEST);
    }

    return new ResponseCheckProjectName(STATUS_OK);
  }

  @Override
  public Project getProjectById(int id) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    return this.projectsRepository.getById(id);
  }

  @Override
  public User deleteUser(int id) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    User user = this.usersRepository.getById(id);
    if (user.getImage() != null
            && !user.getImage().isEmpty()
            && !user.getImage()
            .equals("/static/all-images/Portrait_Placeholder.png")) {
      FileUtils.deleteFile(user.getImage());
    }

    if (user.getMessages().size() != 0) {
      for (Message message
              : user.getMessages()) {
        this.messagesRepository.delete(message);
      }
    }

    if (user.getNotifications().size() != 0) {
      for (Notification notification
              : user.getNotifications()) {
        this.notificationsRepository.delete(notification);
      }
    }

    if (user.getReviews().size() != 0) {
      for (Review review
              : user.getReviews()) {
        this.reviewsRepository.delete(review);
      }
    }

    if (user.getProjects().size() != 0) {
      for (Project project
              : user.getProjects()) {
        if (project.getCreator() == user.getId()) {
          this.deleteProject(project.getId(), new ArrayList<>(), false);
        } else {
          for (int i = 0; i < project.getMembers().size(); i++) {
            if (project.getMembers().get(i).getId() == user.getId()) {
              project.getMembers().remove(i);
              this.projectsRepository.update(project);
              break;
            }
          }
        }
      }
    }

    return this.usersRepository.delete(user);
  }

  @Override
  public List<WantedMember> getWantedMembers(int id) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    return this.projectsRepository.getById(id).getWantedMembers();
  }

  @Override
  public List<UserCategory> getProjectNeeds(int id) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    return this.projectsRepository.getById(id).getProjectNeeds();
  }

  @Override
  public List<FineUploaderImage> getProjectImages(int projectId) {
    if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    }

    Project project = this.projectsRepository.getById(projectId);
    List<FineUploaderImage> result = new ArrayList<>();
    result.add(new FineUploaderImage(this.getNameOfFile(project.getCover()),
            generateUUID(),
            project.getCover(),
            FileUtils.getSizeOfFile(project.getCover()),
            "/api/deleteProjectFile",
            new FileUploaderParameters(projectId, 0)));

    result.add(new FineUploaderImage(this.getNameOfFile(project.getPicture1()),
            generateUUID(),
            project.getPicture1(),
            FileUtils.getSizeOfFile(project.getPicture1()),
            "/api/deleteProjectFile",
            new FileUploaderParameters(projectId, 1)));

    if (isNotNull(project.getPicture2())) {
      result.add(new FineUploaderImage(
              this.getNameOfFile(project.getPicture2()),
              generateUUID(),
              project.getPicture2(),
              FileUtils.getSizeOfFile(project.getPicture2()),
              "/api/deleteProjectFile",
              new FileUploaderParameters(projectId, 2)));
    }

    if (isNotNull(project.getPicture3())) {
      result.add(new FineUploaderImage(
              this.getNameOfFile(project.getPicture3()),
              generateUUID(),
              project.getPicture3(),
              FileUtils.getSizeOfFile(project.getPicture3()),
              "/api/deleteProjectFile",
              new FileUploaderParameters(projectId, 3)));
    }

    if (isNotNull(project.getPicture4())) {
      result.add(new FineUploaderImage(
              this.getNameOfFile(project.getPicture4()),
              generateUUID(),
              project.getPicture4(),
              FileUtils.getSizeOfFile(project.getPicture4()),
              "/api/deleteProjectFile",
              new FileUploaderParameters(projectId, 4)));
    }

    if (isNotNull(project.getPicture5())) {
      result.add(new FineUploaderImage(
              this.getNameOfFile(project.getPicture5()),
              generateUUID(),
              project.getPicture5(),
              FileUtils.getSizeOfFile(project.getPicture5()),
              "/api/deleteProjectFile",
              new FileUploaderParameters(projectId, 5)));
    }

    if (isNotNull(project.getPicture6())) {
      result.add(new FineUploaderImage(
              this.getNameOfFile(project.getPicture6()),
              generateUUID(),
              project.getPicture6(),
              FileUtils.getSizeOfFile(project.getPicture6()),
              "/api/deleteProjectFile",
              new FileUploaderParameters(projectId, 6)));
    }

    if (isNotNull(project.getPicture7())) {
      result.add(new FineUploaderImage(
              this.getNameOfFile(project.getPicture7()),
              generateUUID(),
              project.getPicture7(),
              FileUtils.getSizeOfFile(project.getPicture7()),
              "/api/deleteProjectFile",
              new FileUploaderParameters(projectId, 7)));
    }

    if (isNotNull(project.getPicture8())) {
      result.add(new FineUploaderImage(
              this.getNameOfFile(project.getPicture8()),
              generateUUID(),
              project.getPicture8(),
              FileUtils.getSizeOfFile(project.getPicture8()),
              "/api/deleteProjectFile",
              new FileUploaderParameters(projectId, 8)));
    }

    if (isNotNull(project.getPicture9())) {
      result.add(new FineUploaderImage(
              this.getNameOfFile(project.getPicture9()),
              generateUUID(),
              project.getPicture9(),
              FileUtils.getSizeOfFile(project.getPicture9()),
              "/api/deleteProjectFile",
              new FileUploaderParameters(projectId, 9)));
    }

    return result;
  }

  @Override
  public int deleteProjectFile(int projectId, int pictureNum) {
    if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    }

    Project project = this.projectsRepository.getById(projectId);
    switch (pictureNum) {
      case 1:
        FileUtils.deleteFile(project.getPicture1());
        project.setPicture1(null);
        break;
      case 2:
        FileUtils.deleteFile(project.getPicture2());
        project.setPicture2(null);
        break;
      case 3:
        FileUtils.deleteFile(project.getPicture3());
        project.setPicture3(null);
        break;
      case 4:
        FileUtils.deleteFile(project.getPicture4());
        project.setPicture4(null);
        break;
      case 5:
        FileUtils.deleteFile(project.getPicture5());
        project.setPicture5(null);
        break;
      case 6:
        FileUtils.deleteFile(project.getPicture6());
        project.setPicture6(null);
        break;
      case 7:
        FileUtils.deleteFile(project.getPicture7());
        project.setPicture7(null);
        break;
      case 8:
        FileUtils.deleteFile(project.getPicture8());
        project.setPicture8(null);
        break;
      case 9:
        FileUtils.deleteFile(project.getPicture9());
        project.setPicture9(null);
        break;
      default:
        return STATUS_NOT_FOUND;
    }

    this.projectsRepository.update(project);

    return STATUS_OK;
  }

  @Override
  public IChatbotResponse getPotentialCandidates(String projectName) {
    Project project = this.getProjectByName(projectName);
    List<User> candidates = this.getCandidates(project);
    List<ChatbotReplyCarouselContent> contents = new ArrayList<>();
    for (User candidate
            : candidates) {
      contents.add(new ChatbotReplyCarouselContent(
              candidate.getFirstName() + " " + candidate.getLastName(),
              candidate.getSkillset().getName(),
              candidate.getImage(),
              new ArrayList<>(Collections.singletonList(
                      new ChatbotReplyCarouselContentButton(
                              "Виж профил",
                              "web_url",
                              "https://squadknowhow.herokuapp.com/user/"
                                      + candidate.getId())))));
    }

    if (contents.size() != 0) {
      return new ChatbotResponseCarousel(
              new ArrayList<>(Collections.singletonList(
                      new ChatbotReplyCarousel("carousel", contents)
              )),
              new ChatbotConversation("en", new ChatbotMemory()));
    } else {
      return new ChatbotResponseText(new ArrayList<>(Collections.singletonList(
              new ChatbotReplyText("text",
                      "Sorry, I wasn't able to find any potential"
                              + " members at this moment."
                              + " Maybe come back later?")
      )),
              new ChatbotConversation("en", new ChatbotMemory()));
    }
  }

  private List<User> getCandidates(Project project) {
    List<User> users = this.usersRepository.getAll();
    List<java.util.function.Predicate<User>> predicates = new ArrayList<>();
    if (project.getWantedMembers().size() != 0) {
      for (WantedMember wantedMember
              : project.getWantedMembers()) {
        StringBuilder skills = new StringBuilder();
        for (Skill skill
                : wantedMember.getSkills()) {
          skills.append(skill.getName());
        }

        predicates.add((User user) -> user.getSkillset().getName()
                .equals(wantedMember.getUserCategory().getName())
                && user.getSkills()
                .stream()
                .anyMatch(skill1 -> skills.toString()
                        .contains(skill1.getName())));
      }
    } else if (project.getProjectNeeds().size() != 0) {
      for (UserCategory userCategory
              : project.getProjectNeeds()) {
        predicates.add((User user) -> user.getSkillset().getName()
                .equals(userCategory.getName()));
      }
    }

    if (predicates.size() == 0) {
      return new ArrayList<>();
    }

    java.util.function.Predicate<User> predicate = predicates.stream()
            .reduce(java.util.function.Predicate::or)
            .orElse(x -> true);

    List<java.util.function.Predicate<User>> finalPredicates =
            new ArrayList<>();
    finalPredicates.add(User::isActivated);
    finalPredicates.add(predicate);

    java.util.function.Predicate<User> finalPredicate = finalPredicates.stream()
            .reduce(java.util.function.Predicate::and)
            .orElse(x -> true);

    return users.stream()
            .filter(predicate)
            .collect(Collectors.toList());
  }

  private String getNameOfFile(String path) {
    int index = path.lastIndexOf("/") + 1;
    return path.substring(index);
  }

  private boolean isNotNull(String obj) {
    return obj != null;
  }

  private String generateUUID() {
    return UUID.randomUUID().toString();
  }

  @Override
  public ResponseSuccessful deleteProject(int projectId,
                                          List<DeleteProjectInfo>
                                                  deleteProjectInfos,
                                          boolean isCompleted) {
    if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    }

    Project projectToDelete = this.getProjectById(projectId);
    FileUtils.deleteFile(projectToDelete.getCover());
    FileUtils.deleteFile(projectToDelete.getPicture1());
    if (projectToDelete.getPicture2() != null
            && !projectToDelete.getPicture2().isEmpty()) {
      FileUtils.deleteFile(projectToDelete.getPicture2());
    }

    if (projectToDelete.getPicture3() != null
            && !projectToDelete.getPicture3().isEmpty()) {
      FileUtils.deleteFile(projectToDelete.getPicture3());
    }

    if (projectToDelete.getPicture4() != null
            && !projectToDelete.getPicture4().isEmpty()) {
      FileUtils.deleteFile(projectToDelete.getPicture4());
    }

    if (projectToDelete.getPicture5() != null
            && !projectToDelete.getPicture5().isEmpty()) {
      FileUtils.deleteFile(projectToDelete.getPicture5());
    }

    if (projectToDelete.getPicture6() != null
            && !projectToDelete.getPicture6().isEmpty()) {
      FileUtils.deleteFile(projectToDelete.getPicture6());
    }

    if (projectToDelete.getPicture7() != null
            && !projectToDelete.getPicture7().isEmpty()) {
      FileUtils.deleteFile(projectToDelete.getPicture7());
    }

    if (projectToDelete.getPicture8() != null
            && !projectToDelete.getPicture8().isEmpty()) {
      FileUtils.deleteFile(projectToDelete.getPicture8());
    }

    if (projectToDelete.getPicture9() != null
            && !projectToDelete.getPicture9().isEmpty()) {
      FileUtils.deleteFile(projectToDelete.getPicture9());
    }

    if (projectToDelete.getListEntries() != null) {
      for (int i = 0; i < projectToDelete.getListEntries().size(); i++) {
        this.todoListEntryRepository
                .delete(projectToDelete.getListEntries().get(i));
      }
    }

    if (projectToDelete.getAdvices() != null) {
      for (int i = 0; i < projectToDelete.getAdvices().size(); i++) {
        this.advicesRepository
                .delete(projectToDelete.getAdvices().get(i));
      }
    }

    if (projectToDelete.getUpdates() != null) {
      for (int i = 0; i < projectToDelete.getUpdates().size(); i++) {
        this.updatesRepository
                .delete(projectToDelete.getUpdates().get(i));
      }
    }

    if (projectToDelete.getQuestions() != null) {
      for (int i = 0; i < projectToDelete.getQuestions().size(); i++) {
        this.questionsRepository
                .delete(projectToDelete.getQuestions().get(i));
      }
    }

    if (projectToDelete.getVisits() != null) {
      for (int i = 0; i < projectToDelete.getVisits().size(); i++) {
        this.visitsRepository
                .delete(projectToDelete.getVisits().get(i));
      }
    }

    if (projectToDelete.getWantedMembers() != null) {
      for (int i = 0; i < projectToDelete.getWantedMembers().size(); i++) {
        this.wantedMembersRepository
                .delete(projectToDelete.getWantedMembers().get(i));
      }
    }

    this.projectsRepository.delete(projectToDelete);

    for (DeleteProjectInfo deleteProjectInfo : deleteProjectInfos) {
      User user = this.usersRepository.getById(deleteProjectInfo.getMemberId());
      int sumCommunication = 0;
      int sumIniative = 0;
      int sumLeadership = 0;
      int sumInnovation = 0;
      int sumResponsibility = 0;
      double sumRatings = 0.0;
      int countCompleted = 0;
      for (int j = 0; j < user.getReviews().size(); j++) {
        sumCommunication += user.getReviews().get(j).getCommunication();
        sumIniative += user.getReviews().get(j).getInitiative();
        sumLeadership += user.getReviews().get(j).getLeadership();
        sumInnovation += user.getReviews().get(j).getInnovation();
        sumResponsibility += user.getReviews().get(j).getResponsibility();
        sumRatings += user.getReviews().get(j).getRating();
        if (user.getReviews().get(j).isCompleted()) {
          countCompleted++;
        }
      }

      sumCommunication += deleteProjectInfo.getCommunication();
      sumIniative += deleteProjectInfo.getInitiative();
      sumLeadership += deleteProjectInfo.getLeadership();
      sumInnovation += deleteProjectInfo.getInnovation();
      sumResponsibility += deleteProjectInfo.getResponsibility();
      sumRatings += deleteProjectInfo.getRating();

      Review newReview = new Review();
      newReview.setCommunication(deleteProjectInfo.getCommunication());
      newReview.setInitiative(deleteProjectInfo.getInitiative());
      newReview.setLeadership(deleteProjectInfo.getLeadership());
      newReview.setInnovation(deleteProjectInfo.getInnovation());
      newReview.setResponsibility(deleteProjectInfo.getResponsibility());
      newReview.setDescription(deleteProjectInfo.getReview());
      newReview.setTitle(projectToDelete.getName());
      newReview.setWriter(this.usersRepository
              .getById(projectToDelete.getCreator()));
      newReview.setUser(user);
      newReview.setRating(deleteProjectInfo.getRating());
      newReview.setActivities(deleteProjectInfo.getActivities());
      newReview.setCompleted(isCompleted);

      this.reviewsRepository.create(newReview);

      user.setCommunication(sumCommunication / (user.getReviews().size() + 1));
      user.setInitiative(sumIniative / (user.getReviews().size() + 1));
      user.setLeadership(sumLeadership / (user.getReviews().size() + 1));
      user.setResponsibility(sumResponsibility
              / (user.getReviews().size() + 1));
      user.setInnovation(sumInnovation / (user.getReviews().size() + 1));
      user.setRating(sumRatings / (user.getReviews().size() + 1));
      user.setJobsPercent((int) (((double) countCompleted
              / (double) (user.getReviews().size() + 1)) * 100));

      if (deleteProjectInfo.isRecommended()) {
        user.setRecommendations(user.getRecommendations() + 1);
      }

      this.usersRepository.update(user);
    }

    return new ResponseSuccessful(true);
  }

  private Message writeMessage(String projectName,
                               User sender,
                               User recipient,
                               String kind,
                               String topic,
                               String content,
                               String recipientFirstName,
                               String senderFirstName,
                               String senderLastName,
                               String skillset) {
    Message result = new Message();
    result.setTimestamp(this.createTimestamp());
    result.setSender(sender);
    result.setRecipient(recipient);
    switch (kind) {
      case "rejectionMessage":
        result.setTopic("Отхвърлена заявка за проект \""
                + projectName + "\"");
        result.setContent("Заявката ти за проект \""
                + projectName + "\" беше отхвърлена.");
        result.setKind(kind);
        break;
      case "rejectInviteMessage":
        result.setTopic("Отхвърлена покана");
        result.setContent("Поканата ти към "
                + senderFirstName + " "
                + senderLastName + " за проект \""
                + projectName + "\" беше отхвърлена.");
        result.setKind("rejectionMessage");
        break;
      case "normal":
        result.setTopic(topic);
        result.setContent(content);
        result.setKind(kind);
        break;
      case "requestToJoin":
        result.setTopic("Заявка за включване"
                + " към проект \""
                + projectName + "\"");
        result.setContent("Здравей "
                + recipientFirstName + ", аз се казвам "
                + senderFirstName + " " + senderLastName
                + " и съм много"
                + " заинтригуван от твоята идея "
                + "за проект и бих искал да участвам"
                + " и да я доразвия"
                + " като заема мястото на "
                + skillset + ".");
        result.setKind(kind);
        break;
      case "inviteToJoin":
        result.setTopic("Покана за участие в проект \""
                + projectName + "\"");
        result.setContent("Ти беше поканена от "
                + senderFirstName + " " + senderLastName
                + ", създател на проекта " + projectName
                + ", да участваш в"
                + " неговата разработка"
                + " и да приемеш позицията на "
                + skillset
                + " в екипа на проекта. Приемаш ли?");
        result.setKind(kind);
        break;
      case "approvedMessage":
        result.setTopic("Приета заявка за проект \""
                + projectName + "\"");
        result.setContent("Заявката ти за проект \""
                + projectName + "\" беше приета.");
        result.setKind("rejectionMessage");
        break;
      case "approvedMessageInvite":
        result.setTopic("Приета покана за проект \""
                + projectName + "\"");
        result.setContent("Поканата ти за проект \""
                + projectName + "\" към "
                + senderFirstName + " "
                + senderLastName + " беше приета.");
        result.setKind("rejectionMessage");
        break;
      case "leftMessage":
        result.setTopic("Напускане на проект");
        result.setContent(senderFirstName + " "
                + senderLastName + " напусна проект \""
                + projectName + "\"");
        result.setKind("rejectionMessage");
        break;
      default:
        break;
    }

    return result;
  }

  private Notification writeNotification(String projectName,
                                         User sender,
                                         User recipient,
                                         String kind,
                                         String senderFirstName,
                                         String senderLastName) {
    Notification result = new Notification();
    result.setTimestamp(this.createTimestamp());
    result.setSender(sender);
    result.setRecipient(recipient);

    switch (kind) {
      case "rejection-message":
        result.setContent(" отхвърли "
                + "заявката ти за проект \""
                + projectName + "\".");
        result.setKind(kind);
        break;
      case "normal":
        result.setContent(senderFirstName + " "
                + senderLastName + ", създател на \""
                + projectName + "\" ти изпрати съобщение.");
        result.setKind(kind);
        break;
      case "normal-advice":
        result.setContent(" ти изпрати съвет относно \""
                + projectName + "\".");
        result.setKind(kind);
        break;
      case "normal-question":
        result.setContent(" те попита въпрос относно \""
                + projectName + "\".");
        result.setKind(kind);
        break;
      case "approval-message":
        result.setContent(" поиска да "
                + "се включи в проект \""
                + projectName + "\".");
        result.setKind(kind);
        break;
      case "invite-message":
        result.setContent(" те покани да"
                + " участваш в проект \""
                + projectName + "\".");
        result.setKind(kind);
        break;
      case "approved-new-member":
        result.setContent(" те одобри да"
                + " участваш в проект \""
                + projectName + "\".");
        result.setKind(kind);
        break;
      case "approved-new-member-invite":
        result.setContent(" прие твоята покана"
                + " за участие в проект \""
                + projectName + "\".");
        result.setKind(kind);
        break;
      case "project-leave":
        result.setContent(
                " напусна проект " + projectName + ".");
        result.setKind(kind);
        break;
      default:
        break;
    }

    return result;
  }

  private String createTimestamp() {
    int year = Calendar.getInstance().get(Calendar.YEAR);
    int month = Calendar.getInstance().get(Calendar.MONTH);
    int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    int minutes = Calendar.getInstance().get(Calendar.MINUTE);
    return year + "." + month + "." + day + " " + hour + ":" + minutes;
  }


  private Advice createAdvice(String topic,
                              String content,
                              User sender,
                              Project project) {
    Advice result = new Advice();
    result.setTitle(topic);
    result.setContent(content);
    result.setSender(sender);
    result.setProject(project);
    result.setTimestamp(this.createTimestamp());

    return result;
  }

  private Question createQuestion(String topic,
                                  String content,
                                  User sender,
                                  Project project) {
    Question result = new Question();
    result.setTitle(topic);
    result.setContent(content);
    result.setSender(sender);
    result.setProject(project);
    result.setTimestamp(this.createTimestamp());

    return result;
  }

  private boolean doesProjectNeedThisTypeOfUser(List<UserCategory> projectNeeds,
                                                UserCategory skillset) {
    boolean result = false;
    for (UserCategory projectNeed : projectNeeds) {
      if (skillset.getName().equals(projectNeed.getName())) {
        result = true;
      }
    }

    return result;
  }

  private UserCategory getUserCategory(String userCategoryName) {
    return this.userCategoriesRepository.getAll().stream()
            .filter(uc -> uc.getName().equals(userCategoryName))
            .findFirst()
            .orElse(null);
  }

  // Builds the filtering clauses.
  private List<ProjectShort> buildWhereClauses(List<ProjectShort> projects,
                                               String name,
                                               String userCategory,
                                               String city,
                                               boolean isByMap,
                                               double latitude,
                                               double longitude,
                                               int radius) {
    List<java.util.function.Predicate<ProjectShort>>
            predicates = new ArrayList<>();
    if (!Objects.equals(name, "")) {
      predicates.add((ProjectShort project) ->
              project.getName().toLowerCase().contains(name.toLowerCase()));
    }

    if (!Objects.equals(userCategory, "")
            && !Objects.equals(userCategory, "        "
            + "проекти търсещи:              ")) {
      predicates.add((ProjectShort project) -> project.getProjectNeeds()
              .stream()
              .anyMatch(projectNeed -> projectNeed.getName()
                      .equals(userCategory)));
    }

    if (!Objects.equals(city, "")) {
      predicates.add((ProjectShort project) ->
              project.getCity().toLowerCase().contains(city.toLowerCase()));
    }

    if (isByMap) {
      if (radius == 5000) {
        predicates.add((ProjectShort project) ->
                arePointsNear(project.getLatitude(),
                        project.getLongitude(),
                        latitude,
                        longitude,
                        5));
      } else if (radius == 10000) {
        predicates.add((ProjectShort project) ->
                arePointsNear(project.getLatitude(),
                        project.getLongitude(),
                        latitude,
                        longitude,
                        10));
      } else {
        predicates.add((ProjectShort project) ->
                arePointsNear(project.getLatitude(),
                        project.getLongitude(),
                        latitude,
                        longitude,
                        50));
      }
    }

    java.util.function.Predicate<ProjectShort> predicate = predicates.stream()
            .reduce(java.util.function.Predicate::and)
            .orElse(x -> true);

    return projects.stream()
            .filter(predicate)
            .collect(Collectors.toList());
  }

  // Helper method to see if a point is in the radius of another point.
  private boolean arePointsNear(double checkLatitude,
                                double checkLongitude,
                                double centerLatitude,
                                double centerLongitude,
                                int km) {
    double ky = 111.1111111111111;
    double kx = Math.cos(Math.PI * centerLatitude / 180.0) * ky;
    double dx = Math.abs(centerLongitude - checkLongitude) * kx;
    double dy = Math.abs(centerLatitude - checkLatitude) * ky;
    return Math.sqrt(dx * dx + dy * dy) <= km;
  }

  private void call(String phoneNumber) {
    if (phoneNumber.length() < 9) {
      return;
    }

    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    String number = "+" + phoneNumber;
    System.out.println("calling " + number);
    if (number.length() != PHONE_NUMBER_LENGTH) {
      return;
    }

    PhoneNumber to = new PhoneNumber(number);
    PhoneNumber from = new PhoneNumber(FROM_NUMBER);
    Call call = Call.creator(to,
            from,
            URI.create(
                    "https://handler.twilio.com/twiml/"
                            + "EH300e584a765d344f882e95edd28377de"))
            .create();

    System.out.println(call.getSid());
  }
}
