package squadknowhow.services;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import squadknowhow.contracts.IProjectsService;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.Advice;
import squadknowhow.dbmodels.City;
import squadknowhow.dbmodels.Message;
import squadknowhow.dbmodels.Notification;
import squadknowhow.dbmodels.Project;
import squadknowhow.dbmodels.ProjectShort;
import squadknowhow.dbmodels.Question;
import squadknowhow.dbmodels.User;
import squadknowhow.dbmodels.UserCategory;
import squadknowhow.request.models.EditedProject;
import squadknowhow.request.models.SentAdvice;
import squadknowhow.request.models.SentMessage;
import squadknowhow.request.models.SentQuestion;
import squadknowhow.response.models.ResponseCheckProjectName;
import squadknowhow.response.models.ResponsePagination;
import squadknowhow.response.models.ResponseProjectId;
import squadknowhow.response.models.ResponseSuccessful;
import squadknowhow.response.models.ResponseUpload;
import squadknowhow.response.models.UserProjects;
import squadknowhow.utils.FileUtils;
import squadknowhow.utils.validators.base.IValidator;

@Service
public class DbProjectsService implements IProjectsService {
  private static final String ACCOUNT_SID = System.getenv("ACCOUNT_SID");
  private static final String AUTH_TOKEN = System.getenv("AUTH_TOKEN");
  private static final String FROM_NUMBER = System.getenv("FROM_NUMBER");
  private static final int PHONE_NUMBER_LENGTH = 13;

  private final IRepository<Question> questionsRepository;
  private final IValidator<Integer> idValidator;
  private final IRepository<Advice> advicesRepository;
  private final IRepository<Project> projectsRepository;
  private final IRepository<ProjectShort> projectsShortRepository;
  private final IRepository<Message> messagesRepository;
  private final IRepository<User> usersRepository;
  private final IRepository<Notification> notificationsRepository;
  private final IRepository<City> citiesRepository;
  private final IRepository<UserCategory> userCategoriesRepository;
  private final IValidator<EditedProject> editedProjectValidator;
  private final IValidator<Project> projectValidator;

  @Autowired
  public DbProjectsService(IRepository<Project> projectsRepository,
                           IRepository<ProjectShort> projectsShortRepository,
                           IRepository<Message> messagesRepository,
                           IRepository<User> usersRepository,
                           IRepository<Notification> notificationsRepository,
                           IRepository<City> citiesRepository,
                           IRepository<UserCategory> userCategoriesRepository,
                           IRepository<Advice> advicesRepository,
                           IRepository<Question> questionsRepository,
                           IValidator<Integer> idValidator,
                           IValidator<EditedProject> editedProjectValidator,
                           IValidator<Project> projectValidator) {
    this.projectsRepository = projectsRepository;
    this.projectsShortRepository = projectsShortRepository;
    this.messagesRepository = messagesRepository;
    this.usersRepository = usersRepository;
    this.notificationsRepository = notificationsRepository;
    this.citiesRepository = citiesRepository;
    this.userCategoriesRepository = userCategoriesRepository;
    this.advicesRepository = advicesRepository;
    this.questionsRepository = questionsRepository;
    this.idValidator = idValidator;
    this.editedProjectValidator = editedProjectValidator;
    this.projectValidator = projectValidator;
  }

  @Override
  public ResponseSuccessful addVisit(int projectId) {
    if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    }

    Project projectToUpdate = this.projectsRepository.getById(projectId);
    int visits = projectToUpdate.getVisits();
    projectToUpdate.setVisits(visits + 1);
    this.projectsRepository.update(projectToUpdate);

    return new ResponseSuccessful(true);
  }

  @Override
  public UserProjects getProjectsOfUser(int userId) {
    if (!this.idValidator.isValid(userId)) {
      throw new InvalidParameterException("UserId is not valid");
    }

    List<Project> createdProjects = this.projectsRepository.getAll().stream()
            .filter(project -> project.getCreator() == userId).collect(Collectors.toList());

    List<Project> memberOfProjects = this.projectsRepository.getAll().stream()
            .filter(project -> (project.getCreator() != userId)
                    && (project.getMembers().stream().anyMatch(member -> member.getId() == userId)))
            .collect(Collectors.toList());

    return new UserProjects(createdProjects, memberOfProjects);
  }

  @Override
  public ResponseSuccessful sendRejectMessage(int recipientId,
                                              String projectName,
                                              int messageToDeleteId,
                                              int creatorId) {
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

    Message messageToInsert = this.writeMessage(appliedProject.getName(),
            creatorOfProject,
            newMember,
            "rejectionMessage",
            "",
            "",
            "",
            "",
            "",
            "");

    Message messageToDelete = this.messagesRepository.getById(messageToDeleteId);
    this.messagesRepository.delete(messageToDelete);

    Notification notificationToInsert = this.writeNotification(appliedProject.getName(),
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
  public ResponseSuccessful sendMessageToAllMembers(int projectId, SentMessage message) {
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

        Notification notificationToInsert = this.writeNotification(project.getName(),
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
    Advice adviceToInsert = this.createAdvice(advice.getTopic(), advice.getContent(), sender, project);

    Notification notificationToInsert = this.writeNotification(project.getName(),
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

    Notification notificationToInsert = this.writeNotification(project.getName(),
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
  public ResponseSuccessful editProject(EditedProject project) {
    if (!this.idValidator.isValid(project.getId())) {
      throw new InvalidParameterException("Project id is not valid");
    } else if (!this.editedProjectValidator.isValid(project)) {
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
    this.projectsRepository.update(projectToEdit);

    return new ResponseSuccessful(true);
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
  public ResponseSuccessful sendMessageForApproval(int projectId, int newMemberId, int creatorId) {
    if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    } else if (!this.idValidator.isValid(newMemberId)) {
      throw new InvalidParameterException("NewMemberId is not valid");
    } else if (!this.idValidator.isValid(creatorId)) {
      throw new InvalidParameterException("CreatorId is not valid");
    }

    Project appliedProject = this.projectsRepository.getById(projectId);
    User newMember = this.usersRepository.getById(newMemberId);
    if (!this.doesProjectNeedThisTypeOfUser(appliedProject.getProjectNeeds(), newMember.getSkillset())) {
      return new ResponseSuccessful(false);
    }

    User creatorOfProject = this.usersRepository.getById(creatorId);

    String skillset = newMember.getSkillset().getName().substring(0, 1).toLowerCase() + newMember.getSkillset().getName().substring(1);
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

    Notification notificationToInsert = this.writeNotification(appliedProject.getName(),
            newMember,
            creatorOfProject,
            "approval-message",
            "",
            "");

    this.notificationsRepository.create(notificationToInsert);

    return new ResponseSuccessful(true);
  }

  @Override
  public ResponseSuccessful addProjectMember(String projectName, int newMemberId, int messageId) {
    if (projectName.isEmpty()) {
      throw new InvalidParameterException("ProjectName is not valid");
    } else if (!this.idValidator.isValid(newMemberId)) {
      throw new InvalidParameterException("NewMemberId is not valid");
    } else if (!this.idValidator.isValid(messageId)) {
      throw new InvalidParameterException("MessageId is not valid");
    }

    Project projectToUpdate = this.getProjectByName(projectName);
    User userToAdd = this.usersRepository.getById(newMemberId);

    List<User> members = projectToUpdate.getMembers();
    members.add(userToAdd);
    projectToUpdate.setMembers(members);

    for (int i = 0; i < projectToUpdate.getProjectNeeds().size(); i++) {
      if (projectToUpdate.getProjectNeeds().get(i).getName().equals(userToAdd.getSkillset().getName())) {
        projectToUpdate.getProjectNeeds().remove(i);
      }
    }

    Message messageToDelete = this.messagesRepository.getById(messageId);
    this.messagesRepository.delete(messageToDelete);

    this.projectsRepository.update(projectToUpdate);

    User creatorOfProject = this.usersRepository.getById(projectToUpdate.getCreator());

    Message messageToInsert = this.writeMessage(projectToUpdate.getName(),
            creatorOfProject,
            userToAdd,
            "approvedMessage",
            "",
            "",
            "",
            "",
            "",
            "");

    this.messagesRepository.create(messageToInsert);

    Notification notificationToInsert = this.writeNotification(projectToUpdate.getName(),
            creatorOfProject,
            userToAdd,
            "approved-new-member",
            "",
            "");

    this.notificationsRepository.create(notificationToInsert);

    this.call(projectToUpdate.getTelephone().substring(1));

    return new ResponseSuccessful(true);
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

    User creatorOfProject = this.usersRepository.getById(projectToUpdate.getCreator());
    User recipient = this.usersRepository.getById(memberId);

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

    Notification notificationToInsert = this.writeNotification(projectToUpdate.getName(),
            recipient,
            creatorOfProject,
            "project-leave",
            "",
            "");

    this.notificationsRepository.create(notificationToInsert);

    return new ResponseSuccessful(true);
  }

  @Override
  public ResponsePagination getProjectsPages(String name, String userCategory, String city) {
    List<ProjectShort> projects = this.projectsShortRepository.getAll();
    projects = this.buildWhereClauses(projects, name, userCategory, city);

    int numberOfPages = (int) Math.ceil(projects.size() / 20.0);

    return new ResponsePagination(numberOfPages, projects.size());
  }

  @Override
  public List<ProjectShort> getProjects(int page, String name, String userCategory, String city) {
    List<ProjectShort> projects = this.projectsShortRepository.getAll();
    projects = this.buildWhereClauses(projects, name, userCategory, city);

    int fromIndex = (page - 1) * 20;
    int toIndex = fromIndex + 20;

    if (toIndex > projects.size()) {
      toIndex = projects.size();
    }

    return projects.subList(fromIndex, toIndex);
  }

  @Override
  public ResponseProjectId createProject(Project project, int creatorId) {
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
    if (project.getCity().getName() != null && !Objects.equals(project.getCity().getName(), "")) {
      projectToInsert.setCity(this.getCity(project.getCity().getName()));
    }

    List<UserCategory> projectNeedsToInsert = new ArrayList<>();
    for (int i = 0; i < project.getProjectNeeds().size(); i++) {
      if (project.getProjectNeeds().get(i).getName() != null
              && !Objects.equals(project.getProjectNeeds().get(i).getName(), "")) {
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

    this.projectsRepository.create(projectToInsert);

    return new ResponseProjectId(projectToInsert.getId());
  }

  @Override
  public ResponseUpload uploadPictures(MultipartFile multipart,
                                       int id,
                                       String fileName) throws IOException {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    File file = FileUtils.convertToFile(multipart);
    byte[] bytesFile = FileUtils.toByteArray(file);

    Project project = this.getProjectById(id);

    switch (fileName) {
      case "1":
        project.setPicture1(bytesFile);
        break;
      case "2":
        project.setPicture2(bytesFile);
        break;
      case "3":
        project.setPicture3(bytesFile);
        break;
      case "4":
        project.setPicture4(bytesFile);
        break;
      case "5":
        project.setPicture5(bytesFile);
        break;
      case "6":
        project.setPicture6(bytesFile);
        break;
      case "7":
        project.setPicture7(bytesFile);
        break;
      case "8":
        project.setPicture8(bytesFile);
        break;
      case "9":
        project.setPicture9(bytesFile);
        break;
      default:
        throw new InvalidParameterException("File name is not valid");
    }

    this.projectsRepository.update(project);

    if (file != null) {
      file.delete();
    }

    return new ResponseUpload(true);
  }

  @Override
  public ResponseUpload uploadImage(MultipartFile multipart, int id) throws IOException {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    File file = FileUtils.convertToFile(multipart);
    byte[] bytesFile = FileUtils.toByteArray(file);

    Project project = this.getProjectById(id);

    project.setCover(bytesFile);
    this.projectsRepository.update(project);

    if (file != null) {
      file.delete();
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
      return new ResponseCheckProjectName(400);
    }

    return new ResponseCheckProjectName(200);
  }

  @Override
  public Project getProjectById(int id) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    return this.projectsRepository.getById(id);
  }

  @Override
  public ResponseSuccessful deleteProject(int projectId) {
    if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    }

    Project projectToDelete = this.getProjectById(projectId);

    this.projectsRepository.delete(projectToDelete);

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
      case "normal":
        result.setTopic(topic);
        result.setContent(content);
        result.setKind(kind);
        break;
      case "requestToJoin":
        result.setTopic("Заявка за включване към проект \"" + projectName + "\"");
        result.setContent("Здравей " + recipientFirstName + ", аз се казвам "
                + senderFirstName + " " + senderLastName
                + " и съм много заинтригуван от твоята идея "
                + "за проект и бих искал да участвам и да я доразвия като заема мястото на "
                + skillset + ".");
        result.setKind(kind);
        break;
      case "approvedMessage":
        result.setTopic("Приета заявка за проект \"" + projectName + "\"");
        result.setContent("Заявката ти за проект \""
                + projectName + "\" беше приета.");
        result.setKind("rejectionMessage");
        break;
      case "leftMessage":
        result.setTopic("Напускане на проект");
        result.setContent(senderFirstName + " "
                + senderLastName + " напусна проект \""
                + projectName + "\"");
        result.setKind("rejectionMessage");
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
        result.setContent(" не те одобри за проект \""
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
        result.setContent(senderFirstName + " "
                + senderLastName + ", ти изпрати съвет относно \""
                + projectName + "\".");
        result.setKind(kind);
        break;
      case "normal-question":
        result.setContent(senderFirstName + " "
                + senderLastName + ", те попита въпрос относно \""
                + projectName + "\".");
        result.setKind(kind);
        break;
      case "approval-message":
        result.setContent(" поиска да се включи в проект \""
                + projectName + "\".");
        result.setKind(kind);
        break;
      case "approved-new-member":
        result.setContent(" те одобри да участваш в проект \""
                + projectName + "\".");
        result.setKind(kind);
        break;
      case "project-leave":
        result.setContent(" напусна проект " + projectName + ".");
        result.setKind(kind);
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


  private Advice createAdvice(String topic, String content, User sender, Project project) {
    Advice result = new Advice();
    result.setTitle(topic);
    result.setContent(content);
    result.setSender(sender);
    result.setProject(project);
    result.setTimestamp(this.createTimestamp());

    return result;
  }

  private Question createQuestion(String topic, String content, User sender, Project project) {
    Question result = new Question();
    result.setTitle(topic);
    result.setContent(content);
    result.setSender(sender);
    result.setProject(project);
    result.setTimestamp(this.createTimestamp());

    return result;
  }

  private boolean doesProjectNeedThisTypeOfUser(List<UserCategory> projectNeeds, UserCategory skillset) {
    boolean result = false;
    for (UserCategory projectNeed : projectNeeds) {
      if (skillset.getName().equals(projectNeed.getName())) {
        result = true;
      }
    }

    return result;
  }

  private City getCity(String cityName) {
    return this.citiesRepository.getAll().stream()
            .filter(cit -> cit.getName().equals(cityName))
            .findFirst()
            .orElse(null);
  }

  private UserCategory getUserCategory(String userCategoryName) {
    return this.userCategoriesRepository.getAll().stream()
            .filter(uc -> uc.getName().equals(userCategoryName)).findFirst().orElse(null);
  }

  private List<ProjectShort> buildWhereClauses(List<ProjectShort> projects,
                                               String name,
                                               String userCategory,
                                               String city) {
    boolean shouldFilterByName = false;
    boolean shouldFilterByUserCategory = false;
    boolean shouldFilterByCity = false;
    if (!Objects.equals(name, "")) {
      shouldFilterByName = true;
    }

    if (!Objects.equals(userCategory, "")) {
      shouldFilterByUserCategory = true;
    }

    if (!Objects.equals(city, "")) {
      shouldFilterByCity = true;
    }

    if (shouldFilterByName && shouldFilterByUserCategory && shouldFilterByCity) {
      System.out.println("reached");

      return projects.stream()
              .filter(project -> project.getName().toLowerCase().contains(name.toLowerCase())
                      && project.getProjectNeeds()
                      .stream()
                      .anyMatch(projectNeed -> projectNeed.getName().equals(userCategory))
                      && project.getCity().getName().equals(city))
              .collect(Collectors.toList());
    } else if (shouldFilterByName && shouldFilterByUserCategory) {
      return projects.stream()
              .filter(project -> project.getName().toLowerCase().contains(name.toLowerCase())
                      && project.getProjectNeeds()
                      .stream()
                      .anyMatch(projectNeed -> projectNeed.getName().equals(userCategory)))
              .collect(Collectors.toList());
    } else if (shouldFilterByName && shouldFilterByCity) {
      return projects.stream()
              .filter(project -> project.getName().toLowerCase().contains(name.toLowerCase())
                      && project.getCity().getName().equals(city))
              .collect(Collectors.toList());
    } else if (shouldFilterByUserCategory && shouldFilterByCity) {
      return projects.stream()
              .filter(project -> project.getProjectNeeds()
                      .stream()
                      .anyMatch(projectNeed -> projectNeed.getName().equals(userCategory))
                      && project.getCity().getName().equals(city))
              .collect(Collectors.toList());
    } else if (shouldFilterByName) {
      return projects.stream()
              .filter(project -> project.getName().toLowerCase().contains(name.toLowerCase()))
              .collect(Collectors.toList());
    } else if (shouldFilterByUserCategory) {
      return projects.stream()
              .filter(project -> project.getProjectNeeds()
                      .stream()
                      .anyMatch(projectNeed -> projectNeed.getName().equals(userCategory)))
              .collect(Collectors.toList());
    } else if (shouldFilterByCity) {
      return projects.stream()
              .filter(project -> project.getCity().getName().equals(city))
              .collect(Collectors.toList());
    }

    return projects;
  }

  private void call(String phoneNumber) {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    String number = "+359" + phoneNumber;
    if (number.length() != PHONE_NUMBER_LENGTH) {
      return;
    }

    PhoneNumber to = new PhoneNumber(number);
    PhoneNumber from = new PhoneNumber(FROM_NUMBER);
    Call call = Call.creator(to, from, URI.create("https://handler.twilio.com/twiml/EH87a0cdd4d586175f4048a761641d5e49"))
            .create();

    System.out.println(call.getSid());
  }
}
