package squadknowhow.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.omg.CORBA.DynAnyPackage.Invalid;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.*;
import squadknowhow.request.models.EditedProject;
import squadknowhow.request.models.SentAdvice;
import squadknowhow.request.models.SentMessage;
import squadknowhow.request.models.SentQuestion;
import squadknowhow.response.models.*;
import squadknowhow.utils.validators.*;
import squadknowhow.utils.validators.base.IValidator;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

public class DbProjectsServiceTests {
  @Mock
  private IRepository<Project> projectsRepositoryMock;
  @Mock
  private IRepository<ProjectShort> projectsShortRepository;
  @Mock
  private IRepository<Message> messagesRepositoryMock;
  @Mock
  private IRepository<User> usersRepositoryMock;
  @Mock
  private IRepository<Notification> notificationsRepositoryMock;
  @Mock
  private IRepository<City> citiesRepositoryMock;
  @Mock
  private IRepository<UserCategory> userCategoryiesRepository;
  @Mock
  private IRepository<Advice> advicesRepositoryMock;
  @Mock
  private IRepository<Question> questionsRepositoryMock;

  private IValidator<Integer> idValidator;
  private IValidator<EditedProject> editedProjectValidator;
  private IValidator<Project> projectValidator;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Before
  public void setUpValidators() {
    this.idValidator = new IdValidator();
    this.editedProjectValidator = new EditedProjectValidator();
    this.projectValidator = new ProjectValidator();
  }

  @Test
  public void addVisit_whenProjectIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int projectId = 0;
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.addVisit(projectId);
  }

  @Test
  public void addVisit_whenProjectIdIsValid_shouldReturnResponseSuccessfulWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    int projectId = 1;
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(new Project());
    when(this.projectsRepositoryMock.update(isA(Project.class))).thenReturn(null);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseSuccessful actual = sut.addVisit(projectId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectsOfUser_whenUserIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("UserId is not valid");
    int userId = 0;
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.getProjectsOfUser(userId);
  }

  @Test
  public void getProjectsOfUser_whenUserIdIsValid_shouldReturnUserProjects() {
    int expectedLengthOfCreatedProjects = 1;
    int expectedLengthOfMemberOfProjects = 2;
    int userId = 1;
    List<User> members = new ArrayList<>();
    members.add(new User(1));
    members.add(new User(2));
    List<Project> projects = new ArrayList<>();
    projects.add(new Project(3, members));
    projects.add(new Project(4, members));
    projects.add(new Project(1, members));
    when(this.projectsRepositoryMock.getAll()).thenReturn(projects);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    UserProjects actual = sut.getProjectsOfUser(userId);

    Assert.assertEquals(expectedLengthOfCreatedProjects, actual.getCreatedProjects().size());
    Assert.assertEquals(expectedLengthOfMemberOfProjects, actual.getMemberInProjects().size());
  }

  @Test
  public void sendRejectMessage_whenRecipientIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Recipient id is not valid");
    int recipientId = 0;
    String projectName = "valid";
    int messageToDeleteId = 1;
    int creatorId = 1;
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.sendRejectMessage(recipientId, projectName, messageToDeleteId, creatorId);
  }

  @Test
  public void sendRejectMessage_whenProjectNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project name is empty");
    int recipientId = 1;
    String projectName = "";
    int messageToDeleteId = 1;
    int creatorId = 1;
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.sendRejectMessage(recipientId, projectName, messageToDeleteId, creatorId);
  }

  @Test
  public void sendRejectMessage_whenMessageToDeleteIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("MessageToDeleteId is not valid");
    int recipientId = 1;
    String projectName = "valid";
    int messageToDeleteId = 0;
    int creatorId = 1;
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.sendRejectMessage(recipientId, projectName, messageToDeleteId, creatorId);
  }

  @Test
  public void sendRejectMessage_whenCreatorIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("CreatorId is not valid");
    int recipientId = 1;
    String projectName = "valid";
    int messageToDeleteId = 1;
    int creatorId = 0;
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.sendRejectMessage(recipientId, projectName, messageToDeleteId, creatorId);
  }

  @Test
  public void sendRejectMessage_whenParametersAreValid_shouldReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    int recipientId = 1;
    String projectName = "valid";
    int messageToDeleteId = 1;
    int creatorId = 2;
    List<Project> projects = new ArrayList<>();
    projects.add(new Project(projectName));
    when(this.projectsRepositoryMock.getAll()).thenReturn(projects);
    when(this.usersRepositoryMock.getById(recipientId)).thenReturn(new User());
    when(this.usersRepositoryMock.getById(creatorId)).thenReturn(new User());
    when(this.messagesRepositoryMock.getById(messageToDeleteId)).thenReturn(new Message());
    when(this.messagesRepositoryMock.delete(isA(Message.class))).thenReturn(null);
    when(this.notificationsRepositoryMock.create(isA(Notification.class))).thenReturn(null);
    when(this.messagesRepositoryMock.create(isA(Message.class))).thenReturn(null);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseSuccessful actual = sut.sendRejectMessage(recipientId, projectName, messageToDeleteId, creatorId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void sendMessageToAllMembers_whenProjectIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int projectId = 0;
    SentMessage message = new SentMessage("valid");
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.sendMessageToAllMembers(projectId, message);
  }

  @Test
  public void sendMessageToAllMembers_whenMessageTopicIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Topic is empty");
    int projectId = 1;
    SentMessage message = new SentMessage("");
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.sendMessageToAllMembers(projectId, message);
  }

  @Test
  public void sendMessageToAllMembers_whenParametersAreValid_shouldReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    int projectId = 1;
    SentMessage message = new SentMessage("valid", "content");
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(new Project(1, new ArrayList<>()));
    when(this.usersRepositoryMock.getById(1)).thenReturn(new User());
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseSuccessful actual = sut.sendMessageToAllMembers(projectId, message);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void sendMessageToAllMembers_whenParametersAreValid_shouldSendMessagesToAllMembersReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    int projectId = 1;
    SentMessage message = new SentMessage("valid", "content");
    List<User> projectMembers = new ArrayList<>();
    projectMembers.add(new User(2));
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(new Project(1, projectMembers));
    when(this.usersRepositoryMock.getById(1)).thenReturn(new User());
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseSuccessful actual = sut.sendMessageToAllMembers(projectId, message);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void sendAdvice_whenProjectIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project id is not valid");
    SentAdvice advice = new SentAdvice(0);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.sendAdvice(advice);
  }

  @Test
  public void sendAdvice_whenTopicIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Topic is empty");
    SentAdvice advice = new SentAdvice(1, "");
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.sendAdvice(advice);
  }

  @Test
  public void sendAdvice_whenSenderIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Sender id is not valid");
    SentAdvice advice = new SentAdvice(1, "valid", 0);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.sendAdvice(advice);
  }

  @Test
  public void sendAdvice_whenParametersAreValid_shouldReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    SentAdvice advice = new SentAdvice(1, "valid", 2);
    when(this.projectsRepositoryMock.getById(1)).thenReturn(new Project(1, "valid name"));
    when(this.usersRepositoryMock.getById(2)).thenReturn(new User("first name", "last name", 1));
    when(this.usersRepositoryMock.getById(1)).thenReturn(new User());
    when(this.advicesRepositoryMock.create(isA(Advice.class))).thenReturn(null);
    when(this.notificationsRepositoryMock.create(isA(Notification.class))).thenReturn(null);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseSuccessful actual = sut.sendAdvice(advice);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void sendQuestion_whenProjectIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project id is not valid");
    SentQuestion question = new SentQuestion(0);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.sendQuestion(question);
  }

  @Test
  public void sendQuestion_whenSenderIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Sender id is not valid");
    SentQuestion question = new SentQuestion(1, 0);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.sendQuestion(question);
  }

  @Test
  public void sendQuestion_whenTopicIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Topic is empty");
    SentQuestion question = new SentQuestion(1, 1, "");
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.sendQuestion(question);
  }

  @Test
  public void sendQuestion_whenParametersAreValid_shouldReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    SentQuestion question = new SentQuestion(1, 2, "valid");
    when(this.projectsRepositoryMock.getById(1)).thenReturn(new Project(1, "valid name"));
    when(this.usersRepositoryMock.getById(2)).thenReturn(new User("firstName", "lastName", 1));
    when(this.usersRepositoryMock.getById(1)).thenReturn(new User());
    when(this.questionsRepositoryMock.create(isA(Question.class))).thenReturn(null);
    when(this.notificationsRepositoryMock.create(isA(Notification.class))).thenReturn(null);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseSuccessful actual = sut.sendQuestion(question);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void editProject_whenProjectIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project id is not valid");
    EditedProject project = new EditedProject(0);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.editProject(project);
  }

  @Test
  public void editProject_whenProjectNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    EditedProject project = new EditedProject(1, "");
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.editProject(project);
  }

  @Test
  public void editProject_whenProjectNameIsLessThan5Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    EditedProject project = new EditedProject(1, "1234");
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.editProject(project);
  }

  @Test
  public void editProject_whenProjectDescriptionIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    EditedProject project = new EditedProject(1, "valid name", "");
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.editProject(project);
  }

  @Test
  public void editProject_whenProjectDescriptionIsLessThan150Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    EditedProject project = new EditedProject(1, "valid name", "1231");
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.editProject(project);
  }

  @Test
  public void editProject_whenProjectGoal1IsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    EditedProject project = new EditedProject(1, "valid name", "Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmklcmasdmklcmklasdmcklsadmsdlamclkmsadlkcmsdklaklcasdmacmklmasasdmklasdklmcmklasmcklmasdklcmasmkldcmlkasdklmc123456789", "");
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.editProject(project);
  }

  @Test
  public void editProject_whenParametersAreValid_shouldReturnResponseSuccessfull() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    EditedProject project = new EditedProject(1, "valid name", "Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmklcmasdmklcmklasdmcklsadmsdlamclkmsadlkcmsdklaklcasdmacmklmasasdmklasdklmcmklasmcklmasdklcmasmkldcmlkasdklmc123456789", "goal 1");
    when(this.projectsRepositoryMock.getById(1)).thenReturn(new Project());
    when(this.projectsRepositoryMock.update(isA(Project.class))).thenReturn(null);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseSuccessful actual = sut.editProject(project);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectByName_whenProjectNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectName is empty");
    String projectName = "";
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.getProjectByName(projectName);
  }

  @Test
  public void getProjectByName_whenProjectNameIsValid_shouldReturnProject() {
    String projectName = "valid";
    List<Project> projects = new ArrayList<>();
    projects.add(new Project(projectName));
    when(this.projectsRepositoryMock.getAll()).thenReturn(projects);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    Project actual = sut.getProjectByName(projectName);

    Assert.assertEquals(projectName, actual.getName());
  }

  @Test
  public void sendMessageForApproval_whenProjectIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int projectId = 0;
    int newMemberId = 1;
    int creatorId = 1;
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.sendMessageForApproval(projectId, newMemberId, creatorId);
  }

  @Test
  public void sendMessageForApproval_whenNewMemberIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewMemberId is not valid");
    int projectId = 1;
    int newMemberId = 0;
    int creatorId = 1;
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.sendMessageForApproval(projectId, newMemberId, creatorId);
  }

  @Test
  public void sendMessageForApproval_whenCreatorIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("CreatorId is not valid");
    int projectId = 1;
    int newMemberId = 1;
    int creatorId = 0;
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.sendMessageForApproval(projectId, newMemberId, creatorId);
  }

  @Test
  public void sendMessageForApproval_whenParametersAreValid_shouldReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    int projectId = 1;
    int newMemberId = 2;
    int creatorId = 3;
    Project project = new Project("dsadsda");
    List<UserCategory> projectNeeds = new ArrayList<>();
    projectNeeds.add(new UserCategory("Injener"));
    project.setProjectNeeds(projectNeeds);
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(project);
    when(this.usersRepositoryMock.getById(newMemberId)).thenReturn(new User("firstName", "lastName", new UserCategory("Injener")));
    when(this.usersRepositoryMock.getById(creatorId)).thenReturn(new User("firstName", "lastName", 1));
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseSuccessful actual = sut.sendMessageForApproval(projectId, newMemberId, creatorId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void sendMessageForApproval_whenProjectDoesntNeedThisTypeOfUser_shouldReturnResponseSuccessfulWithFalse() {
    ResponseSuccessful expected = new ResponseSuccessful(false);
    int projectId = 1;
    int newMemberId = 2;
    int creatorId = 3;
    Project project = new Project("dsadsda");
    List<UserCategory> projectNeeds = new ArrayList<>();
    projectNeeds.add(new UserCategory("Not Injener"));
    project.setProjectNeeds(projectNeeds);
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(project);
    when(this.usersRepositoryMock.getById(newMemberId)).thenReturn(new User("firstName", "lastName", new UserCategory("Injener")));
    when(this.usersRepositoryMock.getById(creatorId)).thenReturn(new User("firstName", "lastName", 1));
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseSuccessful actual = sut.sendMessageForApproval(projectId, newMemberId, creatorId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void addProjectMember_whenProjectNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectName is not valid");
    String projectName = "";
    int newMemberId = 1;
    int messageId = 1;
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.addProjectMember(projectName, newMemberId, messageId);
  }

  @Test
  public void addProjectMember_whenNewMemberIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewMemberId is not valid");
    String projectName = "valid";
    int newMemberId = 0;
    int messageId = 1;
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.addProjectMember(projectName, newMemberId, messageId);
  }

  @Test
  public void addProjectMember_whenMessageIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("MessageId is not valid");
    String projectName = "valid";
    int newMemberId = 1;
    int messageId = 0;
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.addProjectMember(projectName, newMemberId, messageId);
  }

  @Test
  public void addProjectMember_whenParametersAreValid_shouldReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    String projectName = "valid name";
    int newMemberId = 1;
    int messageId = 1;
    List<Project> projects = new ArrayList<>();
    Project project = new Project(2, new ArrayList<>(), projectName);
    project.setTelephone("dsfas");
    List<UserCategory> projectNeeds = new ArrayList<>();
    UserCategory userCategory = new UserCategory();
    userCategory.setName("Програмист");
    projectNeeds.add(userCategory);
    project.setProjectNeeds(projectNeeds);
    projects.add(project);
    User user = new User();
    UserCategory skillset = new UserCategory();
    skillset.setName("Програмист");
    user.setSkillset(skillset);
    when(this.projectsRepositoryMock.getAll()).thenReturn(projects);
    when(this.usersRepositoryMock.getById(newMemberId)).thenReturn(user);
    when(this.messagesRepositoryMock.getById(messageId)).thenReturn(new Message());
    when(this.messagesRepositoryMock.delete(isA(Message.class))).thenReturn(null);
    when(this.projectsRepositoryMock.update(isA(Project.class))).thenReturn(null);
    when(this.usersRepositoryMock.getById(2)).thenReturn(new User());
    when(this.messagesRepositoryMock.create(isA(Message.class))).thenReturn(null);
    when(this.notificationsRepositoryMock.create(isA(Notification.class))).thenReturn(null);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseSuccessful actual = sut.addProjectMember(projectName, newMemberId, messageId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void removeProjectMember_whenProjectIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int projectId = 0;
    int memberId = 1;
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.removeProjectMember(projectId, memberId);
  }

  @Test
  public void removeProjectMember_whenMemberIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("MemberId is not valid");
    int projectId = 1;
    int memberId = 0;
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.removeProjectMember(projectId, memberId);
  }

  @Test
  public void removeProjectMember_whenParametersAreValid_shouldReturnResponseSuccessfull() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    int projectId = 1;
    int memberId = 2;
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(new Project(1, new ArrayList<>(), "valid name"));
    when(this.projectsRepositoryMock.update(isA(Project.class))).thenReturn(null);
    when(this.usersRepositoryMock.getById(1)).thenReturn(new User());
    when(this.usersRepositoryMock.getById(memberId)).thenReturn(new User("firstName", "lastName", 1));
    when(this.messagesRepositoryMock.create(isA(Message.class))).thenReturn(null);
    when(this.notificationsRepositoryMock.create(isA(Notification.class))).thenReturn(null);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseSuccessful actual = sut.removeProjectMember(projectId, memberId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void removeProjectMember_whenParametersAreValid_shouldRemoveMemberAndReturnResponseSuccessfull() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    int projectId = 1;
    int memberId = 2;
    List<User> projectMembers = new ArrayList<>();
    projectMembers.add(new User(2));
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(new Project(1, projectMembers, "valid name"));
    when(this.projectsRepositoryMock.update(isA(Project.class))).thenReturn(null);
    when(this.usersRepositoryMock.getById(1)).thenReturn(new User());
    when(this.usersRepositoryMock.getById(memberId)).thenReturn(new User("firstName", "lastName", 1));
    when(this.messagesRepositoryMock.create(isA(Message.class))).thenReturn(null);
    when(this.notificationsRepositoryMock.create(isA(Notification.class))).thenReturn(null);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseSuccessful actual = sut.removeProjectMember(projectId, memberId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createProject_whenCreatorIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("CreatorId is not valid");
    int creatorId = 0;
    Project project = new Project();
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.createProject(project, creatorId);
  }

  @Test
  public void createProject_whenProjectIsNull_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    int creatorId = 1;
    Project project = null;
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.createProject(project, creatorId);
  }

  @Test
  public void createProject_whenProjectNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    int creatorId = 1;
    Project project = new Project("");
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.createProject(project, creatorId);
  }

  @Test
  public void createProject_whenProjectNameIsLessThan5Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    int creatorId = 1;
    Project project = new Project("123");
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.createProject(project, creatorId);
  }

  @Test
  public void createProject_whenProjectDescriptionIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    int creatorId = 1;
    Project project = new Project("valid name", "");
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.createProject(project, creatorId);
  }

  @Test
  public void createProject_whenProjectDescriptionIsLessThan150Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    int creatorId = 1;
    Project project = new Project("valid name", "dsada");
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.createProject(project, creatorId);
  }

  @Test
  public void createProject_whenProjectGoal1IsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    int creatorId = 1;
    Project project = new Project("valid name", "Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmklcmasdmklcmklasdmcklsadmsdlamclkmsadlkcmsdklaklcasdmacmklmasasdmklasdklmcmklasmcklmasdklcmasmkldcmlkasdklmc123456789", "");
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.createProject(project, creatorId);
  }

  @Test
  public void createProject_whenParametersAreValid_shouldReturnResponseProjectIdWithTheIdOfTheCreatedProject() {
    int expectedProjectId = 1;
    ResponseProjectId expected = new ResponseProjectId(expectedProjectId);
    int creatorId = 1;
    Project project = new Project("valid name", "Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmklcmasdmklcmklasdmcklsadmsdlamclkmsadlkcmsdklaklcasdmacmklmasasdmklasdklmcmklasmcklmasdklcmasmkldcmlkasdklmc123456789", "valid goal 1", new City(), new ArrayList<>());
    when(this.usersRepositoryMock.getById(creatorId)).thenReturn(new User());
    this.projectsRepositoryMock = new IRepository<Project>() {
      @Override
      public List<Project> getAll() {
        return null;
      }

      @Override
      public Project getById(int id) {
        return null;
      }

      @Override
      public Project create(Project entity) {
        entity.setId(expectedProjectId);
        return null;
      }

      @Override
      public Project delete(Project entity) {
        return null;
      }

      @Override
      public Project update(Project entity) {
        return null;
      }
    };
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseProjectId actual = sut.createProject(project, creatorId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createProject_whenParametersAreValid_shouldSetCityAndProjectNeedsAndReturnResponseProjectIdWithTheIdOfTheCreatedProject() {
    int expectedProjectId = 1;
    ResponseProjectId expected = new ResponseProjectId(expectedProjectId);
    int creatorId = 1;
    List<UserCategory> projectNeeds = new ArrayList<>();
    projectNeeds.add(new UserCategory("programmer"));
    Project project = new Project("valid name",
            "Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmklcmasdmklcmklasdmcklsadmsdlamclkmsadlkcmsdklaklcasdmacmklmasasdmklasdklmcmklasmcklmasdklcmasmkldcmlkasdklmc123456789",
            "valid goal 1",
            new City("name"),
            projectNeeds);
    when(this.citiesRepositoryMock.getAll()).thenReturn(new ArrayList<>());
    when(this.userCategoryiesRepository.getAll()).thenReturn(new ArrayList<>());
    when(this.usersRepositoryMock.getById(creatorId)).thenReturn(new User());
    this.projectsRepositoryMock = new IRepository<Project>() {
      @Override
      public List<Project> getAll() {
        return null;
      }

      @Override
      public Project getById(int id) {
        return null;
      }

      @Override
      public Project create(Project entity) {
        entity.setId(expectedProjectId);
        return null;
      }

      @Override
      public Project delete(Project entity) {
        return null;
      }

      @Override
      public Project update(Project entity) {
        return null;
      }
    };
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseProjectId actual = sut.createProject(project, creatorId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadPictures_whenIdIsLessThan1_shouldThrowInvalidParameterException() throws IOException {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.uploadPictures(null, 0, "");
  }

  @Test
  public void uploadPictures_whenFileNameIsNotValid_shouldThrowInvalidParameterException() throws IOException {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("File name is not valid");
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.uploadPictures(null, 1, "10");
  }
  @Test
  public void uploadPictures_whenParametersAreValid_shouldReturnResponseUploadWithTrueAndSetProjectPicture1() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    String fileName = "1";
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseUpload actual = sut.uploadPictures(null, id, fileName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadPictures_whenParametersAreValid_shouldReturnResponseUploadWithTrueAndSetProjectPicture2() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    String fileName = "2";
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseUpload actual = sut.uploadPictures(null, id, fileName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadPictures_whenParametersAreValid_shouldReturnResponseUploadWithTrueAndSetProjectPicture3() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    String fileName = "3";
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseUpload actual = sut.uploadPictures(null, id, fileName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadPictures_whenParametersAreValid_shouldReturnResponseUploadWithTrueAndSetProjectPicture4() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    String fileName = "4";
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseUpload actual = sut.uploadPictures(null, id, fileName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadPictures_whenParametersAreValid_shouldReturnResponseUploadWithTrueAndSetProjectPicture5() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    String fileName = "5";
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseUpload actual = sut.uploadPictures(null, id, fileName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadPictures_whenParametersAreValid_shouldReturnResponseUploadWithTrueAndSetProjectPicture6() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    String fileName = "6";
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseUpload actual = sut.uploadPictures(null, id, fileName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadPictures_whenParametersAreValid_shouldReturnResponseUploadWithTrueAndSetProjectPicture7() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    String fileName = "7";
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseUpload actual = sut.uploadPictures(null, id, fileName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadPictures_whenParametersAreValid_shouldReturnResponseUploadWithTrueAndSetProjectPicture8() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    String fileName = "8";
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseUpload actual = sut.uploadPictures(null, id, fileName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadPictures_whenParametersAreValid_shouldReturnResponseUploadWithTrueAndSetProjectPicture9() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    String fileName = "9";
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseUpload actual = sut.uploadPictures(null, id, fileName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadImage_whenIdIsLessThan1_shoudThrowInvalidParameterException() throws IOException {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.uploadImage(null, 0);
  }

  @Test
  public void uploadImage_whenParametersAreValid_shouldReturnResponseUpload() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseUpload actual = sut.uploadImage(null, id);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void checkProjectName_whenNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Name is empty");
    String name = "";
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.checkProjectName(name);
  }

  @Test
  public void checkProjectName_whenProjectWithThatNameIsNotFound_shouldReturnResponseCheckProjectNameWith400() {
    ResponseCheckProjectName expected = new ResponseCheckProjectName(400);
    String name = "valid";
    List<Project> projects = new ArrayList<>();
    projects.add(new Project("valid"));
    when(this.projectsRepositoryMock.getAll()).thenReturn(projects);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseCheckProjectName actual = sut.checkProjectName(name);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void checkProjectName_whenProjectWithThatNameIsFound_shouldReturnResponseCheckProjectNameWith200() {
    ResponseCheckProjectName expected = new ResponseCheckProjectName(200);
    String name = "valid";
    List<Project> projects = new ArrayList<>();
    projects.add(new Project("not valid"));
    when(this.projectsRepositoryMock.getAll()).thenReturn(projects);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseCheckProjectName actual = sut.checkProjectName(name);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectById_whenIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int id = 0;
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.getProjectById(id);
  }

  @Test
  public void getProjectById_whenIdIsValid_shouldReturnProjectWithTheSameId() {
    int expected = 1;
    when(this.projectsRepositoryMock.getById(expected)).thenReturn(new Project(1));
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    Project actual = sut.getProjectById(expected);

    Assert.assertEquals(expected, actual.getId());
  }

  @Test
  public void deleteProject_whenProjectIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int projectId = 0;
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    sut.deleteProject(projectId);
  }

  @Test
  public void deleteProject_whenProjectIdIsValid_shouldReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    int projectId = 1;
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(new Project());
    when(this.projectsRepositoryMock.delete(isA(Project.class))).thenReturn(null);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponseSuccessful actual = sut.deleteProject(projectId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectsPages_whenOnlyCityIsEmpty_shouldReturnFilteredProjects() {
    int expectedNumberOfPages = 1;
    int expectedProjectsSize = 1;
    ResponsePagination expected = new ResponsePagination(expectedNumberOfPages, expectedProjectsSize);
    String name = "projectName";
    String userCategory = "programmer";
    String city = "";
    List<ProjectShort> projects = new ArrayList<>();
    ProjectShort project = new ProjectShort();
    project.setName("projectName");
    List<UserCategory> projectNeeds = new ArrayList();
    projectNeeds.add(new UserCategory("programmer"));
    project.setProjectNeeds(projectNeeds);
    projects.add(project);
    when(this.projectsShortRepository.getAll()).thenReturn(projects);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponsePagination actual = sut.getProjectsPages(name, userCategory, city);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectsPages_whenAllParametersAreNotEmpty_shouldReturnFilteredProjects() {
    int expectedNumberOfPages = 1;
    int expectedProjectsSize = 1;
    ResponsePagination expected = new ResponsePagination(expectedNumberOfPages, expectedProjectsSize);
    String name = "projectName";
    String userCategory = "programmer";
    String city = "cityName";
    List<ProjectShort> projects = new ArrayList<>();
    ProjectShort project = new ProjectShort();
    project.setName("projectName");
    List<UserCategory> projectNeeds = new ArrayList();
    projectNeeds.add(new UserCategory("programmer"));
    project.setProjectNeeds(projectNeeds);
    project.setCity(new City("cityName"));
    projects.add(project);
    when(this.projectsShortRepository.getAll()).thenReturn(projects);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponsePagination actual = sut.getProjectsPages(name, userCategory, city);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectsPages_whenOnlyUserCategoryIsEmpty_shouldReturnFilteredProjects() {
    int expectedNumberOfPages = 1;
    int expectedProjectsSize = 1;
    ResponsePagination expected = new ResponsePagination(expectedNumberOfPages, expectedProjectsSize);
    String name = "projectName";
    String userCategory = "";
    String city = "cityName";
    List<ProjectShort> projects = new ArrayList<>();
    ProjectShort project = new ProjectShort();
    project.setName("projectName");
    List<UserCategory> projectNeeds = new ArrayList();
    projectNeeds.add(new UserCategory("programmer"));
    project.setProjectNeeds(projectNeeds);
    project.setCity(new City("cityName"));
    projects.add(project);
    when(this.projectsShortRepository.getAll()).thenReturn(projects);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponsePagination actual = sut.getProjectsPages(name, userCategory, city);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectsPages_whenOnlyNameIsEmpty_shouldReturnFilteredProjects() {
    int expectedNumberOfPages = 1;
    int expectedProjectsSize = 1;
    ResponsePagination expected = new ResponsePagination(expectedNumberOfPages, expectedProjectsSize);
    String name = "";
    String userCategory = "programmer";
    String city = "cityName";
    List<ProjectShort> projects = new ArrayList<>();
    ProjectShort project = new ProjectShort();
    project.setName("projectName");
    List<UserCategory> projectNeeds = new ArrayList();
    projectNeeds.add(new UserCategory("programmer"));
    project.setProjectNeeds(projectNeeds);
    project.setCity(new City("cityName"));
    projects.add(project);
    when(this.projectsShortRepository.getAll()).thenReturn(projects);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponsePagination actual = sut.getProjectsPages(name, userCategory, city);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectsPages_whenOnlyNameIsNotEmpty_shouldReturnFilteredProjects() {
    int expectedNumberOfPages = 1;
    int expectedProjectsSize = 1;
    ResponsePagination expected = new ResponsePagination(expectedNumberOfPages, expectedProjectsSize);
    String name = "projectName";
    String userCategory = "";
    String city = "";
    List<ProjectShort> projects = new ArrayList<>();
    ProjectShort project = new ProjectShort();
    project.setName("projectName");
    List<UserCategory> projectNeeds = new ArrayList();
    projectNeeds.add(new UserCategory("programmer"));
    project.setProjectNeeds(projectNeeds);
    project.setCity(new City("cityName"));
    projects.add(project);
    when(this.projectsShortRepository.getAll()).thenReturn(projects);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponsePagination actual = sut.getProjectsPages(name, userCategory, city);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectsPages_whenOnlyUserCategoryIsNotEmpty_shouldReturnFilteredProjects() {
    int expectedNumberOfPages = 1;
    int expectedProjectsSize = 1;
    ResponsePagination expected = new ResponsePagination(expectedNumberOfPages, expectedProjectsSize);
    String name = "";
    String userCategory = "programmer";
    String city = "";
    List<ProjectShort> projects = new ArrayList<>();
    ProjectShort project = new ProjectShort();
    project.setName("projectName");
    List<UserCategory> projectNeeds = new ArrayList();
    projectNeeds.add(new UserCategory("programmer"));
    project.setProjectNeeds(projectNeeds);
    project.setCity(new City("cityName"));
    projects.add(project);
    when(this.projectsShortRepository.getAll()).thenReturn(projects);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponsePagination actual = sut.getProjectsPages(name, userCategory, city);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectsPages_whenOnlyCityIsNotEmpty_shouldReturnFilteredProjects() {
    int expectedNumberOfPages = 1;
    int expectedProjectsSize = 1;
    ResponsePagination expected = new ResponsePagination(expectedNumberOfPages, expectedProjectsSize);
    String name = "";
    String userCategory = "";
    String city = "cityName";
    List<ProjectShort> projects = new ArrayList<>();
    ProjectShort project = new ProjectShort();
    project.setName("projectName");
    List<UserCategory> projectNeeds = new ArrayList();
    projectNeeds.add(new UserCategory("programmer"));
    project.setProjectNeeds(projectNeeds);
    project.setCity(new City("cityName"));
    projects.add(project);
    when(this.projectsShortRepository.getAll()).thenReturn(projects);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponsePagination actual = sut.getProjectsPages(name, userCategory, city);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectsPages_whenAllParametersAreEmpty_shouldReturnFilteredProjects() {
    int expectedNumberOfPages = 1;
    int expectedProjectsSize = 2;
    ResponsePagination expected = new ResponsePagination(expectedNumberOfPages, expectedProjectsSize);
    String name = "";
    String userCategory = "";
    String city = "";
    List<ProjectShort> projects = new ArrayList<>();
    ProjectShort project = new ProjectShort();
    project.setName("projectName");
    List<UserCategory> projectNeeds = new ArrayList();
    projectNeeds.add(new UserCategory("programmer"));
    project.setProjectNeeds(projectNeeds);
    project.setCity(new City("cityName"));
    projects.add(project);
    ProjectShort project2 = new ProjectShort();
    project.setName("projectName");
    List<UserCategory> projectNeeds2 = new ArrayList();
    projectNeeds.add(new UserCategory("programmer"));
    project.setProjectNeeds(projectNeeds);
    project.setCity(new City("cityName"));
    projects.add(project);
    when(this.projectsShortRepository.getAll()).thenReturn(projects);
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    ResponsePagination actual = sut.getProjectsPages(name, userCategory, city);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjects_whenAllParametersAreValid_shouldReturnTheProjects() {
    List<ProjectShort> expected = new ArrayList<>();
    expected.add(new ProjectShort());
    when(this.projectsShortRepository.getAll()).thenReturn(expected);
    int page = 1;
    String name = "";
    String userCategory = "";
    String city = "";
    DbProjectsService sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator);

    List<ProjectShort> actual = sut.getProjects(page, name, userCategory, city);
  }


}