package squadknowhow.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;
import squadknowhow.contracts.IProjectsService;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.*;
import squadknowhow.request.models.CreateProject;
import squadknowhow.request.models.DeleteProjectInfo;
import squadknowhow.request.models.EditedProject;
import squadknowhow.request.models.Parameter;
import squadknowhow.request.models.SentAdvice;
import squadknowhow.request.models.SentMessage;
import squadknowhow.request.models.SentQuestion;
import squadknowhow.response.models.*;
import squadknowhow.utils.validators.*;
import squadknowhow.utils.validators.base.IValidator;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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
  @Mock
  private IRepository<TodoListEntry> todoListEntryRepositoryMock;
  @Mock
  private IRepository<Visit> visitsRepositoryMock;
  @Mock
  private IRepository<Review> reviewsRepositoryMock;
  @Mock
  private IRepository<Update> updatesRepositoryMock;
  @Mock
  private IRepository<Skill> skillsRepositoryMock;
  @Mock
  private IRepository<Language> languagesRepositoryMock;
  @Mock
  private IRepository<WantedMember> wantedMembersRepositoryMock;

  private IValidator<Integer> idValidator;
  private IValidator<EditedProject> editedProjectValidator;
  private IValidator<CreateProject> projectValidator;
  private DbProjectsService sut;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Before
  public void setUpValidators() {
    this.idValidator = new IdValidator();
    this.editedProjectValidator = new EditedProjectValidator();
    this.projectValidator = new ProjectValidator();
    this.sut = new DbProjectsService(this.projectsRepositoryMock,
            this.projectsShortRepository,
            this.messagesRepositoryMock,
            this.usersRepositoryMock,
            this.notificationsRepositoryMock,
            this.userCategoryiesRepository,
            this.advicesRepositoryMock,
            this.questionsRepositoryMock,
            this.idValidator,
            this.editedProjectValidator,
            this.projectValidator,
            this.todoListEntryRepositoryMock,
            this.visitsRepositoryMock,
            this.reviewsRepositoryMock,
            this.updatesRepositoryMock,
            this.skillsRepositoryMock,
            this.languagesRepositoryMock,
            this.citiesRepositoryMock,
            this.wantedMembersRepositoryMock);
  }

  @Test
  public void addVisit_whenProjectIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int projectId = 0;

    sut.addVisit(projectId);
  }

  @Test
  public void addVisit_whenProjectIdIsValid_shouldReturnResponseSuccessfulWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    int projectId = 1;
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(new Project());
    when(this.projectsRepositoryMock.update(isA(Project.class))).thenReturn(null);

    ResponseSuccessful actual = sut.addVisit(projectId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectsOfUser_whenUserIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("UserId is not valid");
    int userId = 0;

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

    sut.sendRejectMessage(recipientId, projectName, messageToDeleteId, creatorId, false);
  }

  @Test
  public void sendRejectMessage_whenProjectNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project name is empty");
    int recipientId = 1;
    String projectName = "";
    int messageToDeleteId = 1;
    int creatorId = 1;

    sut.sendRejectMessage(recipientId, projectName, messageToDeleteId, creatorId, false);
  }

  @Test
  public void sendRejectMessage_whenMessageToDeleteIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("MessageToDeleteId is not valid");
    int recipientId = 1;
    String projectName = "valid";
    int messageToDeleteId = 0;
    int creatorId = 1;

    sut.sendRejectMessage(recipientId, projectName, messageToDeleteId, creatorId, false);
  }

  @Test
  public void sendRejectMessage_whenCreatorIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("CreatorId is not valid");
    int recipientId = 1;
    String projectName = "valid";
    int messageToDeleteId = 1;
    int creatorId = 0;

    sut.sendRejectMessage(recipientId, projectName, messageToDeleteId, creatorId, false);
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

    ResponseSuccessful actual = sut.sendRejectMessage(recipientId, projectName, messageToDeleteId, creatorId, false);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void sendRejectMessage_whenParametersAreValidAndIsInvite_shouldReturnResponseSuccessfullWithTrue() {
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

    ResponseSuccessful actual = sut.sendRejectMessage(recipientId, projectName, messageToDeleteId, creatorId, true);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void sendMessageToAllMembers_whenProjectIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int projectId = 0;
    SentMessage message = new SentMessage("valid");

    sut.sendMessageToAllMembers(projectId, message);
  }

  @Test
  public void sendMessageToAllMembers_whenMessageTopicIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Topic is empty");
    int projectId = 1;
    SentMessage message = new SentMessage("");

    sut.sendMessageToAllMembers(projectId, message);
  }

  @Test
  public void sendMessageToAllMembers_whenParametersAreValid_shouldReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    int projectId = 1;
    SentMessage message = new SentMessage("valid", "content");
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(new Project(1, new ArrayList<>()));
    when(this.usersRepositoryMock.getById(1)).thenReturn(new User());

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

    ResponseSuccessful actual = sut.sendMessageToAllMembers(projectId, message);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void sendAdvice_whenProjectIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project id is not valid");
    SentAdvice advice = new SentAdvice(0);

    sut.sendAdvice(advice);
  }

  @Test
  public void sendAdvice_whenTopicIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Topic is empty");
    SentAdvice advice = new SentAdvice(1, "");

    sut.sendAdvice(advice);
  }

  @Test
  public void sendAdvice_whenSenderIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Sender id is not valid");
    SentAdvice advice = new SentAdvice(1, "valid", 0);

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

    ResponseSuccessful actual = sut.sendAdvice(advice);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void sendQuestion_whenProjectIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project id is not valid");
    SentQuestion question = new SentQuestion(0);

    sut.sendQuestion(question);
  }

  @Test
  public void sendQuestion_whenSenderIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Sender id is not valid");
    SentQuestion question = new SentQuestion(1, 0);

    sut.sendQuestion(question);
  }

  @Test
  public void sendQuestion_whenTopicIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Topic is empty");
    SentQuestion question = new SentQuestion(1, 1, "");

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

    ResponseSuccessful actual = sut.sendQuestion(question);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void editProject_whenProjectIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project id is not valid");
    CreateProject project = new CreateProject(0);

    sut.editProject(project);
  }

  @Test
  public void editProject_whenProjectNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    CreateProject project = new CreateProject("", 1);

    sut.editProject(project);
  }

  @Test
  public void editProject_whenProjectNameIsLessThan5Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    CreateProject project = new CreateProject("1234", 1);

    sut.editProject(project);
  }

  @Test
  public void editProject_whenProjectDescriptionIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    CreateProject project = new CreateProject(1, "valid name", "");

    sut.editProject(project);
  }

  @Test
  public void editProject_whenProjectDescriptionIsLessThan150Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    CreateProject project = new CreateProject(1, "valid name", "1231");
    sut.editProject(project);
  }

  @Test
  public void editProject_whenProjectGoal1IsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    CreateProject project = new CreateProject(1, "valid name", "Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmklcmasdmklcmklasdmcklsadmsdlamclkmsadlkcmsdklaklcasdmacmklmasasdmklasdklmcmklasmcklmasdklcmasmkldcmlkasdklmc123456789", "");

    sut.editProject(project);
  }

  @Test
  public void editProject_whenParametersAreValid_shouldReturnResponseSuccessfull() {
    ResponseProjectId expected = new ResponseProjectId(1);
    CreateProject project = new CreateProject(1, "valid name", "Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmklcmasdmklcmklasdmcklsadmsdlamclkmsadlkcmsdklaklcasdmacmklmasasdmklasdklmcmklasmcklmasdklcmasmkldcmlkasdklmc123456789", "goal 1");
    project.setParameters(new ArrayList<>());
    project.setWantedMembers(new ArrayList<>());
    when(this.projectsRepositoryMock.getById(1)).thenReturn(new Project());
    when(this.projectsRepositoryMock.update(isA(Project.class))).thenReturn(null);
    when(this.projectsRepositoryMock.getById(isA(Integer.class))).thenAnswer(invocation -> {
      int id = invocation.getArgument(0);
      if (id == 1) {
        return new Project();
      }

      return null;
    });
    when(this.projectsRepositoryMock.update(isA(Project.class))).thenAnswer(invocation -> {
      Project entity = invocation.getArgument(0);
      entity.setId(1);
      return entity;
    });

    ResponseProjectId actual = sut.editProject(project);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectByName_whenProjectNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectName is empty");
    String projectName = "";

    sut.getProjectByName(projectName);
  }

  @Test
  public void getProjectByName_whenProjectNameIsValid_shouldReturnProject() {
    String projectName = "valid";
    List<Project> projects = new ArrayList<>();
    projects.add(new Project(projectName));
    when(this.projectsRepositoryMock.getAll()).thenReturn(projects);

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

    sut.sendMessageForApproval(projectId, newMemberId, creatorId);
  }

  @Test
  public void sendMessageForApproval_whenNewMemberIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewMemberId is not valid");
    int projectId = 1;
    int newMemberId = 0;
    int creatorId = 1;

    sut.sendMessageForApproval(projectId, newMemberId, creatorId);
  }

  @Test
  public void sendMessageForApproval_whenCreatorIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("CreatorId is not valid");
    int projectId = 1;
    int newMemberId = 1;
    int creatorId = 0;

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
    project.setTelephone("3421");
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(project);
    when(this.usersRepositoryMock.getById(newMemberId)).thenReturn(new User("firstName", "lastName", new UserCategory("Injener")));
    when(this.usersRepositoryMock.getById(creatorId)).thenReturn(new User("firstName", "lastName", 1));

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

    sut.addProjectMember(projectName, newMemberId, messageId, false);
  }

  @Test
  public void addProjectMember_whenNewMemberIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewMemberId is not valid");
    String projectName = "valid";
    int newMemberId = 0;
    int messageId = 1;

    sut.addProjectMember(projectName, newMemberId, messageId, false);
  }

  @Test
  public void addProjectMember_whenMessageIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("MessageId is not valid");
    String projectName = "valid";
    int newMemberId = 1;
    int messageId = 0;

    sut.addProjectMember(projectName, newMemberId, messageId, false);
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
    project.setId(1);
    List<WantedMember> wantedMembers = new ArrayList<>();
    WantedMember wantedMember = new WantedMember();
    wantedMember.setUserCategory(new UserCategory("Програмист"));
    wantedMembers.add(wantedMember);
    project.setWantedMembers(wantedMembers);
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
    List<Project> favouriteProjects = new ArrayList<>();
    favouriteProjects.add(project);
    user.setFavoriteProjects(favouriteProjects);
    when(this.projectsRepositoryMock.getAll()).thenReturn(projects);
    when(this.usersRepositoryMock.getById(newMemberId)).thenReturn(user);
    when(this.messagesRepositoryMock.getById(messageId)).thenReturn(new Message());
    when(this.messagesRepositoryMock.delete(isA(Message.class))).thenReturn(null);
    when(this.projectsRepositoryMock.update(isA(Project.class))).thenReturn(null);
    when(this.usersRepositoryMock.getById(2)).thenReturn(new User());
    when(this.messagesRepositoryMock.create(isA(Message.class))).thenReturn(null);
    when(this.notificationsRepositoryMock.create(isA(Notification.class))).thenReturn(null);

    ResponseSuccessful actual = sut.addProjectMember(projectName, newMemberId, messageId, false);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void addProjectMember_whenParametersAreValidAndIsInvite_shouldReturnResponseSuccessfullWithTrue() {
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
    user.setFavoriteProjects(new ArrayList<>());
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

    ResponseSuccessful actual = sut.addProjectMember(projectName, newMemberId, messageId, true);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void removeProjectMember_whenProjectIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int projectId = 0;
    int memberId = 1;

    sut.removeProjectMember(projectId, memberId);
  }

  @Test
  public void removeProjectMember_whenMemberIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("MemberId is not valid");
    int projectId = 1;
    int memberId = 0;

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

    ResponseSuccessful actual = sut.removeProjectMember(projectId, memberId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createProject_whenCreatorIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("CreatorId is not valid");
    int creatorId = 0;
    CreateProject project = new CreateProject();

    sut.createProject(project, creatorId);
  }

  @Test
  public void createProject_whenProjectIsNull_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    int creatorId = 1;
    CreateProject project = null;

    sut.createProject(project, creatorId);
  }

  @Test
  public void createProject_whenProjectNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    int creatorId = 1;
    CreateProject project = new CreateProject("");

    sut.createProject(project, creatorId);
  }

  @Test
  public void createProject_whenProjectNameIsLessThan5Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    int creatorId = 1;
    CreateProject project = new CreateProject("123");

    sut.createProject(project, creatorId);
  }

  @Test
  public void createProject_whenProjectDescriptionIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    int creatorId = 1;
    CreateProject project = new CreateProject("valid name", "");

    sut.createProject(project, creatorId);
  }

  @Test
  public void createProject_whenProjectDescriptionIsLessThan150Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    int creatorId = 1;
    CreateProject project = new CreateProject("valid name", "dsada");

    sut.createProject(project, creatorId);
  }

  @Test
  public void createProject_whenProjectGoal1IsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Project is not valid");
    int creatorId = 1;
    CreateProject project = new CreateProject("valid name", "Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmklcmasdmklcmklasdmcklsadmsdlamclkmsadlkcmsdklaklcasdmacmklmasasdmklasdklmcmklasmcklmasdklcmasmkldcmlkasdklmc123456789", "");

    sut.createProject(project, creatorId);
  }

  @Test
  public void createProject_whenParametersAreValid_shouldReturnResponseProjectIdWithTheIdOfTheCreatedProject() {
    int expectedProjectId = 1;
    ResponseProjectId expected = new ResponseProjectId(expectedProjectId);
    int creatorId = 1;
    CreateProject project = new CreateProject("valid name", "Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmklcmasdmklcmklasdmcklsadmsdlamclkmsadlkcmsdklaklcasdmacmklmasasdmklasdklmcmklasmcklmasdklcmasmkldcmlkasdklmc123456789", "valid goal 1", "", new ArrayList<>());
    project.setParameters(new ArrayList<>());
    when(this.usersRepositoryMock.getById(creatorId)).thenReturn(new User());
    when(this.projectsRepositoryMock.create(isA(Project.class))).thenAnswer(invocation -> {
      Project entity = invocation.getArgument(0);
      entity.setId(expectedProjectId);
      return null;
    });

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
    CreateProject project = new CreateProject("valid name",
            "Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmklcmasdmklcmklasdmcklsadmsdlamclkmsadlkcmsdklaklcasdmacmklmasasdmklasdklmcmklasmcklmasdklcmasmkldcmlkasdklmc123456789",
            "valid goal 1",
            "name",
            projectNeeds);
    project.setParameters(new ArrayList<>());
    when(this.citiesRepositoryMock.getAll()).thenReturn(new ArrayList<>());
    when(this.userCategoryiesRepository.getAll()).thenReturn(new ArrayList<>());
    when(this.usersRepositoryMock.getById(creatorId)).thenReturn(new User());
    when(this.projectsRepositoryMock.create(isA(Project.class))).thenAnswer(invocation -> {
      Project entity = invocation.getArgument(0);
      entity.setId(expectedProjectId);
      return null;
    });

    ResponseProjectId actual = sut.createProject(project, creatorId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createProject_whenParametersAreValidAndProjectNeedsMoney_shouldSetCityAndProjectNeedsAndReturnResponseProjectIdWithTheIdOfTheCreatedProject() {
    int expectedProjectId = 1;
    ResponseProjectId expected = new ResponseProjectId(expectedProjectId);
    int creatorId = 1;
    List<UserCategory> projectNeeds = new ArrayList<>();
    projectNeeds.add(new UserCategory("programmer"));
    CreateProject project = new CreateProject("valid name",
            "Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmklcmasdmklcmklasdmcklsadmsdlamclkmsadlkcmsdklaklcasdmacmklmasasdmklasdklmcmklasmcklmasdklcmasmkldcmlkasdklmc123456789",
            "valid goal 1",
            "name",
            projectNeeds);
    project.setParameters(new ArrayList<>());
    project.setWantedMembers(new ArrayList<>());
    project.setNeedsMoney(true);
    when(this.citiesRepositoryMock.getAll()).thenReturn(new ArrayList<>());
    when(this.userCategoryiesRepository.getAll()).thenReturn(new ArrayList<>());
    when(this.usersRepositoryMock.getById(creatorId)).thenReturn(new User());
    when(this.projectsRepositoryMock.create(isA(Project.class))).thenAnswer(invocation -> {
      Project entity = invocation.getArgument(0);
      entity.setId(expectedProjectId);
      return entity;
    });

    ResponseProjectId actual = sut.createProject(project, creatorId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadPictures_whenIdIsLessThan1_shouldThrowInvalidParameterException() throws IOException {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");

    sut.uploadPictures(null, 0, "");
  }

  @Test
  public void uploadPictures_whenFileNameIsNotValid_shouldThrowInvalidParameterException() throws IOException {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("File name is not valid");

    sut.uploadPictures(null, 1, "10");
  }

  @Test
  public void uploadPictures_whenParametersAreValid_shouldReturnResponseUploadWithTrueAndSetProjectPicture1() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    String fileName = "1";
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());

    ResponseUpload actual = sut.uploadPictures(null, id, fileName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadPictures_whenParametersAreValid_shouldReturnResponseUploadWithTrueAndSetProjectPicture2() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    String fileName = "2";
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());

    ResponseUpload actual = sut.uploadPictures(null, id, fileName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadPictures_whenParametersAreValid_shouldReturnResponseUploadWithTrueAndSetProjectPicture3() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    String fileName = "3";
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());

    ResponseUpload actual = sut.uploadPictures(null, id, fileName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadPictures_whenParametersAreValid_shouldReturnResponseUploadWithTrueAndSetProjectPicture4() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    String fileName = "4";
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());

    ResponseUpload actual = sut.uploadPictures(null, id, fileName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadPictures_whenParametersAreValid_shouldReturnResponseUploadWithTrueAndSetProjectPicture5() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    String fileName = "5";
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());

    ResponseUpload actual = sut.uploadPictures(null, id, fileName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadPictures_whenParametersAreValid_shouldReturnResponseUploadWithTrueAndSetProjectPicture6() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    String fileName = "6";
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());

    ResponseUpload actual = sut.uploadPictures(null, id, fileName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadPictures_whenParametersAreValid_shouldReturnResponseUploadWithTrueAndSetProjectPicture7() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    String fileName = "7";
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());

    ResponseUpload actual = sut.uploadPictures(null, id, fileName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadPictures_whenParametersAreValid_shouldReturnResponseUploadWithTrueAndSetProjectPicture8() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    String fileName = "8";
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());

    ResponseUpload actual = sut.uploadPictures(null, id, fileName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadPictures_whenParametersAreValid_shouldReturnResponseUploadWithTrueAndSetProjectPicture9() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    String fileName = "9";
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());

    ResponseUpload actual = sut.uploadPictures(null, id, fileName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadImage_whenIdIsLessThan1_shoudThrowInvalidParameterException() throws IOException {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");

    sut.uploadImage(null, 0);
  }

  @Test
  public void uploadImage_whenParametersAreValid_shouldReturnResponseUpload() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project());

    ResponseUpload actual = sut.uploadImage(null, id);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void checkProjectName_whenNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Name is empty");
    String name = "";

    sut.checkProjectName(name);
  }

  @Test
  public void checkProjectName_whenProjectWithThatNameIsNotFound_shouldReturnResponseCheckProjectNameWith400() {
    ResponseCheckProjectName expected = new ResponseCheckProjectName(400);
    String name = "valid";
    List<Project> projects = new ArrayList<>();
    projects.add(new Project("valid"));
    when(this.projectsRepositoryMock.getAll()).thenReturn(projects);

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

    ResponseCheckProjectName actual = sut.checkProjectName(name);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectById_whenIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int id = 0;

    sut.getProjectById(id);
  }

  @Test
  public void getProjectById_whenIdIsValid_shouldReturnProjectWithTheSameId() {
    int expected = 1;
    when(this.projectsRepositoryMock.getById(expected)).thenReturn(new Project(1));

    Project actual = sut.getProjectById(expected);

    Assert.assertEquals(expected, actual.getId());
  }

  @Test
  public void deleteProject_whenProjectIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int projectId = 0;
    List<DeleteProjectInfo> deleteProjectInfos = new ArrayList<>();
    boolean isCompleted = false;

    sut.deleteProject(projectId, deleteProjectInfos, isCompleted);
  }

  @Test
  public void deleteProject_whenProjectIdIsValid_shouldReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    int projectId = 1;
    Project project = new Project();
    List<TodoListEntry> listEntries = new ArrayList<>();
    listEntries.add(new TodoListEntry());
    project.setListEntries(listEntries);
    List<Advice> advices = new ArrayList<>();
    advices.add(new Advice());
    project.setAdvices(advices);
    List<Question> questions = new ArrayList<>();
    questions.add(new Question());
    project.setQuestions(questions);
    List<DeleteProjectInfo> deleteProjectInfos = new ArrayList<>();
    boolean isCompleted = false;
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(project);
    when(this.projectsRepositoryMock.delete(isA(Project.class))).thenReturn(null);

    ResponseSuccessful actual = sut.deleteProject(projectId, deleteProjectInfos, isCompleted);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void deleteProject_whenProjectIdIsValidAndDeleteProjectInfosIsProvided_shouldReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    int projectId = 1;
    Project project = new Project();
    List<TodoListEntry> listEntries = new ArrayList<>();
    listEntries.add(new TodoListEntry());
    project.setListEntries(listEntries);
    List<Advice> advices = new ArrayList<>();
    advices.add(new Advice());
    project.setCover("/test");
    project.setPicture1("/test");
    project.setPicture2("/test");
    project.setPicture3("/test");
    project.setPicture4("/test");
    project.setPicture5("/test");
    project.setPicture6("/test");
    project.setAdvices(advices);
    project.setCreator(0);
    project.setUpdates(new ArrayList<>(Collections.singletonList(new Update())));
    project.setVisits(new ArrayList<>(Collections.singletonList(new Visit())));
    project.setWantedMembers(new ArrayList<>(Collections.singletonList(new WantedMember())));
    List<Question> questions = new ArrayList<>();
    questions.add(new Question());
    project.setQuestions(questions);
    List<DeleteProjectInfo> deleteProjectInfos = new ArrayList<>();
    DeleteProjectInfo dpi = new DeleteProjectInfo();
    dpi.setMemberId(4);
    dpi.setRecommended(true);
    User user = new User();
    user.setRecommendations(0);
    user.setCommunication(0);
    user.setInitiative(0);
    user.setLeadership(0);
    user.setInnovation(0);
    user.setResponsibility(0);
    user.setJobsPercent(0);
    user.setRating(0);
    List<Review> reviews = new ArrayList<>();
    Review review = new Review();
    review.setCommunication(0);
    review.setInitiative(0);
    review.setLeadership(0);
    review.setInnovation(0);
    review.setResponsibility(0);
    review.setActivities("fdsfs");
    review.setRating(0);
    review.setDescription("fsd");
    review.setCompleted(true);
    reviews.add(review);
    user.setReviews(reviews);
    deleteProjectInfos.add(dpi);
    boolean isCompleted = false;
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(project);
    when(this.projectsRepositoryMock.delete(isA(Project.class))).thenReturn(null);
    when(this.usersRepositoryMock.getById(4)).thenReturn(user);

    ResponseSuccessful actual = sut.deleteProject(projectId, deleteProjectInfos, isCompleted);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectsPages_whenOnlyCityIsEmpty_shouldReturnFilteredProjects() {
    int expectedNumberOfPages = 1;
    int expectedProjectsSize = 1;
    boolean isByMap = false;
    double latitude = 0.0;
    double longitude = 0.0;
    int radius = 0;
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
    ResponsePagination actual = sut.getProjectsPages(name, userCategory, city, isByMap, latitude, longitude, radius);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectsPages_whenAllParametersAreNotEmpty_shouldReturnFilteredProjects() {
    int expectedNumberOfPages = 1;
    int expectedProjectsSize = 1;
    boolean isByMap = false;
    double latitude = 0.0;
    double longitude = 0.0;
    int radius = 0;
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
    project.setCity("cityName");
    projects.add(project);
    when(this.projectsShortRepository.getAll()).thenReturn(projects);

    ResponsePagination actual = sut.getProjectsPages(name, userCategory, city, isByMap, latitude, longitude, radius);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectsPages_whenOnlyUserCategoryIsEmpty_shouldReturnFilteredProjects() {
    int expectedNumberOfPages = 1;
    int expectedProjectsSize = 1;
    boolean isByMap = false;
    double latitude = 0.0;
    double longitude = 0.0;
    int radius = 0;
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
    project.setCity("cityName");
    projects.add(project);
    when(this.projectsShortRepository.getAll()).thenReturn(projects);

    ResponsePagination actual = sut.getProjectsPages(name, userCategory, city, isByMap, latitude, longitude, radius);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectsPages_whenOnlyNameIsEmpty_shouldReturnFilteredProjects() {
    int expectedNumberOfPages = 1;
    int expectedProjectsSize = 1;
    boolean isByMap = false;
    double latitude = 0.0;
    double longitude = 0.0;
    int radius = 0;
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
    project.setCity("cityName");
    projects.add(project);
    when(this.projectsShortRepository.getAll()).thenReturn(projects);

    ResponsePagination actual = sut.getProjectsPages(name, userCategory, city, isByMap, latitude, longitude, radius);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectsPages_whenOnlyNameIsNotEmpty_shouldReturnFilteredProjects() {
    int expectedNumberOfPages = 1;
    int expectedProjectsSize = 1;
    boolean isByMap = false;
    double latitude = 0.0;
    double longitude = 0.0;
    int radius = 0;
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
    project.setCity("cityName");
    projects.add(project);
    when(this.projectsShortRepository.getAll()).thenReturn(projects);

    ResponsePagination actual = sut.getProjectsPages(name, userCategory, city, isByMap, latitude, longitude, radius);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectsPages_whenOnlyUserCategoryIsNotEmpty_shouldReturnFilteredProjects() {
    int expectedNumberOfPages = 1;
    int expectedProjectsSize = 1;
    boolean isByMap = false;
    double latitude = 0.0;
    double longitude = 0.0;
    int radius = 0;
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
    project.setCity("cityName");
    projects.add(project);
    when(this.projectsShortRepository.getAll()).thenReturn(projects);

    ResponsePagination actual = sut.getProjectsPages(name, userCategory, city, isByMap, latitude, longitude, radius);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectsPages_whenOnlyCityIsNotEmpty_shouldReturnFilteredProjects() {
    int expectedNumberOfPages = 1;
    int expectedProjectsSize = 1;
    boolean isByMap = false;
    double latitude = 0.0;
    double longitude = 0.0;
    int radius = 0;
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
    project.setCity("cityName");
    projects.add(project);
    when(this.projectsShortRepository.getAll()).thenReturn(projects);

    ResponsePagination actual = sut.getProjectsPages(name, userCategory, city, isByMap, latitude, longitude, radius);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectsPages_whenAllParametersAreEmpty_shouldReturnFilteredProjects() {
    int expectedNumberOfPages = 1;
    int expectedProjectsSize = 2;
    boolean isByMap = false;
    double latitude = 0.0;
    double longitude = 0.0;
    int radius = 0;
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
    project.setCity("cityName");
    projects.add(project);
    project.setName("projectName");
    projectNeeds.add(new UserCategory("programmer"));
    project.setProjectNeeds(projectNeeds);
    project.setCity("cityName");
    projects.add(project);
    when(this.projectsShortRepository.getAll()).thenReturn(projects);

    ResponsePagination actual = sut.getProjectsPages(name, userCategory, city, isByMap, latitude, longitude, radius);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjects_whenAllParametersAreValid_shouldReturnTheProjects() {
    List<ProjectShort> expected = new ArrayList<>();
    expected.add(new ProjectShort());
    boolean isByMap = false;
    double latitude = 0.0;
    double longitude = 0.0;
    int radius = 0;
    when(this.projectsShortRepository.getAll()).thenReturn(expected);
    int page = 1;
    String name = "";
    String userCategory = "";
    String city = "";

    List<ProjectShort> actual = sut.getProjects(page, name, userCategory, city, isByMap, latitude, longitude, radius);

    Assert.assertEquals(expected, actual);
  }


  @Test
  public void getProjects_whenIsByMapAndRadiusIs5KM_shouldReturnProjects() {
    List<ProjectShort> expected = new ArrayList<>();
    ProjectShort project = new ProjectShort();
    project.setLatitude(42.43986350);
    project.setLongitude(25.84530540);
    expected.add(project);
    boolean isByMap = true;
    double latitude = 42.43986350;
    double longitude = 25.84530540;
    int radius = 5000;
    int page = 1;
    String name = "";
    String userCategory = "";
    String city = "";
    when(this.projectsShortRepository.getAll()).thenReturn(expected);

    List<ProjectShort> actual = sut.getProjects(page, name, city, userCategory, isByMap, latitude, longitude, radius);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjects_whenIsByMapAndRadiusIs10KM_shouldReturnProjects() {
    List<ProjectShort> expected = new ArrayList<>();
    ProjectShort project = new ProjectShort();
    project.setLatitude(42.43986350);
    project.setLongitude(25.84530540);
    expected.add(project);
    boolean isByMap = true;
    double latitude = 42.43986350;
    double longitude = 25.84530540;
    int radius = 10000;
    int page = 1;
    String name = "";
    String userCategory = "";
    String city = "";
    when(this.projectsShortRepository.getAll()).thenReturn(expected);

    List<ProjectShort> actual = sut.getProjects(page, name, city, userCategory, isByMap, latitude, longitude, radius);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjects_whenIsByMapAndRadiusIs50KM_shouldReturnProjects() {
    List<ProjectShort> expected = new ArrayList<>();
    ProjectShort project = new ProjectShort();
    project.setLatitude(42.43986350);
    project.setLongitude(25.84530540);
    expected.add(project);
    boolean isByMap = true;
    double latitude = 42.43986350;
    double longitude = 25.84530540;
    int radius = 50000;
    int page = 1;
    String name = "";
    String userCategory = "";
    String city = "";
    when(this.projectsShortRepository.getAll()).thenReturn(expected);

    List<ProjectShort> actual = sut.getProjects(page, name, city, userCategory, isByMap, latitude, longitude, radius);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getAvailableProjectsForInvite_whenOwnerIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("OwnerId is not valid");
    int ownerId = 0;
    String skillset = "fdsaf";

    sut.getAvailableProjectsForInvite(ownerId, skillset);
  }

  @Test
  public void getAvailableProjectsForInvite_whenSkillsetIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Skillset is empty");
    int ownerId = 1;
    String skillset = "";

    sut.getAvailableProjectsForInvite(ownerId, skillset);
  }

  @Test
  public void getAvailableProjectsForInvite_whenParametersAreValid_shouldReturnTheCorrectProjects() {
    List<ProjectWithName> expected = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      ProjectWithName projectWithName = new ProjectWithName();
      projectWithName.setId(i);
      projectWithName.setName("" + i);
      expected.add(projectWithName);
    }
    int ownerId = 1;
    String skillset = "Програмист";
    List<Project> allProjects = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Project project = new Project();
      project.setId(i);
      project.setName("" + i);
      project.setCreator(ownerId);
      List<UserCategory> projectNeeds = new ArrayList<>();
      projectNeeds.add(new UserCategory(skillset));
      project.setProjectNeeds(projectNeeds);
      allProjects.add(project);
    }
    Project oneMore = new Project();
    oneMore.setId(2);
    oneMore.setCreator(2);
    oneMore.setProjectNeeds(new ArrayList<>());
    allProjects.add(oneMore);
    when(this.projectsRepositoryMock.getAll()).thenReturn(allProjects);

    List<ProjectWithName> actual = sut.getAvailableProjectsForInvite(ownerId, skillset);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void sendInviteMessage_whenProjectIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int projectId = 0;
    int recipient = 1;
    int sender = 1;

    sut.sendInviteMessage(projectId, recipient, sender);
  }

  @Test
  public void sendInviteMessage_whenNewMemberIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Recipient is not valid");
    int projectId = 1;
    int recipient = 0;
    int sender = 1;

    sut.sendInviteMessage(projectId, recipient, sender);
  }

  @Test
  public void sendInviteMessage_whenCreatorIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Sender is not valid");
    int projectId = 1;
    int recipient = 1;
    int sender = 0;

    sut.sendInviteMessage(projectId, recipient, sender);
  }

  @Test
  public void sendInviteMessage_whenParametersAreValid_shouldReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    int projectId = 1;
    int recipient = 2;
    int sender = 3;
    Project project = new Project("dsadsda");
    List<UserCategory> projectNeeds = new ArrayList<>();
    projectNeeds.add(new UserCategory("Injener"));
    project.setProjectNeeds(projectNeeds);
    project.setTelephone("3421");
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(project);
    when(this.usersRepositoryMock.getById(recipient)).thenReturn(new User("firstName", "lastName", new UserCategory("Injener")));
    when(this.usersRepositoryMock.getById(sender)).thenReturn(new User("firstName", "lastName", 1));

    ResponseSuccessful actual = sut.sendInviteMessage(projectId, recipient, sender);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void sendInviteMessage_whenProjectDoesntNeedThisTypeOfUser_shouldReturnResponseSuccessfulWithFalse() {
    ResponseSuccessful expected = new ResponseSuccessful(false);
    int projectId = 1;
    int recipient = 2;
    int sender = 3;
    Project project = new Project("dsadsda");
    List<UserCategory> projectNeeds = new ArrayList<>();
    projectNeeds.add(new UserCategory("Not Injener"));
    project.setProjectNeeds(projectNeeds);
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(project);
    when(this.usersRepositoryMock.getById(recipient)).thenReturn(new User("firstName", "lastName", new UserCategory("Injener")));

    ResponseSuccessful actual = sut.sendInviteMessage(projectId, recipient, sender);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void reportProject_whenCalled_shouldReturnTrue() {
    boolean actual = sut.reportProject("sample", 5);

    Assert.assertTrue(actual);
  }

  @Test
  public void addFavorite_whenUserIdIsNotValid_shouldThrowInvalidPrameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("UserId is not valid");
    int userId = 0;
    int projectId = 1;

    sut.addFavorite(userId, projectId);
  }

  @Test
  public void addFavorite_whenProjectIdIsNotValid_shouldThrowInvalidPrameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int userId = 1;
    int projectId = 0;

    sut.addFavorite(userId, projectId);
  }

  @Test
  public void addFavorite_whenParametersAreValid_shouldReturnTrue() {
    int userId = 1;
    int projectId = 1;
    User user = new User();
    user.setFavoriteProjects(new ArrayList<>());
    Project project = new Project();
    when(this.usersRepositoryMock.getById(userId)).thenReturn(user);
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(project);

    boolean actual = sut.addFavorite(userId, projectId);

    Assert.assertTrue(actual);
  }

  @Test
  public void removeFavorite_whenUserIdIsNotValid_shouldThrowInvalidPrameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("UserId is not valid");
    int userId = 0;
    int projectId = 1;

    sut.removeFavorite(userId, projectId);
  }

  @Test
  public void removeFavorite_whenProjectIdIsNotValid_shouldThrowInvalidPrameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int userId = 1;
    int projectId = 0;

    sut.removeFavorite(userId, projectId);
  }

  @Test
  public void removeFavorite_whenParametersAreValid_shouldReturnTrue() {
    int userId = 1;
    int projectId = 1;
    User user = new User();
    user.setFavoriteProjects(new ArrayList<>());
    Project project = new Project();
    when(this.usersRepositoryMock.getById(userId)).thenReturn(user);
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(project);

    boolean actual = sut.removeFavorite(userId, projectId);

    Assert.assertTrue(actual);
  }

  @Test
  public void isUserMember_whenCalledAndTheUserIsTheCreator_shouldReturnFalse() {
    User user = new User(2);
    Auction auction = new Auction();
    auction.setProjectId(2);
    Project project = new Project();
    project.setCreator(2);
    when(this.projectsRepositoryMock.getById(2)).thenReturn(project);

    boolean actual = sut.isUserMember(user, auction);

    Assert.assertFalse(actual);
  }

  @Test
  public void isUserMmeber_whenCalledAndUserIsMember_shouldReturnTrue() {
    User user = new User(2);
    Auction auction = new Auction();
    auction.setProjectId(2);
    Project project = new Project();
    project.setCreator(3);
    List<User> members = new ArrayList<>();
    members.add(user);
    project.setMembers(members);
    when(this.projectsRepositoryMock.getById(2)).thenReturn(project);

    boolean actual = sut.isUserMember(user, auction);

    Assert.assertTrue(actual);
  }

  @Test
  public void isUserMember_whenCalledAndUserIsNotMember_shouldReturnFalse() {
    User user = new User(2);
    Auction auction = new Auction();
    auction.setProjectId(2);
    Project project = new Project();
    project.setCreator(3);
    List<User> members = new ArrayList<>();
    project.setMembers(members);
    when(this.projectsRepositoryMock.getById(2)).thenReturn(project);

    boolean actual = sut.isUserMember(user, auction);

    Assert.assertFalse(actual);
  }

  @Test
  public void getWorkingOnProject_whenProjectIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int projectId = 0;

    sut.getWorkingOnProject(projectId);
  }

  @Test
  public void getWorkigOnProject_whenParametersAreValid_shouldReturnTheSize() {
    int expected = 15;
    int projectId = 1;
    Project project = new Project();
    List<User> members = new ArrayList<>();
    for (int i = 0; i < expected; i++) {
      members.add(new User());
    }
    project.setMembers(members);
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(project);

    int actual = sut.getWorkingOnProject(projectId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getDescription_whenProjectIdIsNotValid_shouldThrowInvalidParameterExceptio() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int projectId = 0;

    sut.getDescription(projectId);
  }

  @Test
  public void getDescription_whenParametersAreValid_shouldReturnTheDescription() {
    String expected = "test";
    int projectId = 1;
    Project project = new Project();
    project.setDescription(expected);
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(project);

    String actual = sut.getDescription(projectId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getChartData_whenProjectIdIsNotValid_shouldThrowInvalidParameterException() throws ParseException {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int projectId = 0;

    sut.getChartData(projectId, Calendar.getInstance());
  }

  @Test
  public void getChartData_whenProjectIdIsValid_shouldReturnResponseVisitsWithValuesAndDates() throws ParseException {
    List<String> dates = new ArrayList<>();
    dates.add("12/18/2000 15:30");
    dates.add("12/17/2000 15:30");
    dates.add("12/16/2000 15:30");
    dates.add("12/15/2000 15:30");
    dates.add("12/14/2000 15:30");
    dates.add("12/13/2000 15:30");
    dates.add("12/12/2000 15:30");
    List<Integer> values = new ArrayList<>();
    values.add(3);
    values.add(2);
    values.add(1);
    values.add(0);
    values.add(1);
    values.add(2);
    values.add(1);
    ResponseVisits expected = new ResponseVisits(dates, values);
    int projectId = 1;
    Project project = new Project();
    List<Visit> visits = new ArrayList<>();
    visits.add(new Visit("12/13/2000 15:30"));
    visits.add(new Visit("12/12/2000 15:30"));
    visits.add(new Visit("12/13/2000 15:30"));
    visits.add(new Visit("12/14/2000 15:30"));
    visits.add(new Visit("12/16/2000 15:30"));
    visits.add(new Visit("12/17/2000 15:30"));
    visits.add(new Visit("12/17/2000 15:30"));
    visits.add(new Visit("12/18/2000 15:30"));
    visits.add(new Visit("12/18/2000 15:30"));
    visits.add(new Visit("12/18/2000 15:30"));
    project.setVisits(visits);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    Date date = sdf.parse("12/18/2000 15:30");
    Calendar visitDate = Calendar.getInstance();
    visitDate.setTime(date);
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(project);

    ResponseVisits actual = sut.getChartData(projectId, visitDate);

    Assert.assertEquals(expected.getDates(), actual.getDates());
    Assert.assertEquals(expected.getVisits(), actual.getVisits());
  }

  @Test
  public void getCoordinates_whenCalled_shouldReturnCoordinates() {
    List<Coordinate> expected = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      Coordinate coordinate = new Coordinate();
      coordinate.setTitle("Test");
      coordinate.setLongitude(1.00);
      coordinate.setLongitude(1.00);
      expected.add(coordinate);
    }
    List<Project> projects = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      Project project = new Project();
      project.setLatitude(1.00);
      project.setLongitude(1.00);
      project.setName("Test");
      projects.add(project);
    }
    when(this.projectsRepositoryMock.getAll()).thenReturn(projects);

    List<Coordinate> actual = sut.getCoordinates();

    Assert.assertEquals(expected.size(), actual.size());
  }

  @Test
  public void addPowerpointEmbedCode_whenParametersAreValid_shouldReturnTrueAndSetThePowerpointEmbedCode() {
    int id = 1;
    String link = "TEST";
    Project project = new Project();
    when(this.projectsRepositoryMock.getById(isA(Integer.class))).thenAnswer(invocation -> {
      return project;
    });
    when(this.projectsRepositoryMock.update(isA(Project.class))).thenAnswer(invocation -> {
      Project entity = invocation.getArgument(0);
      entity.setPowerpointEmbedCode(link);
      return entity;
    });

    boolean actual = sut.addPowerpointEmbedCode(id, link);

    Assert.assertTrue(actual);
    Assert.assertEquals(link, project.getPowerpointEmbedCode());
  }

  @Test
  public void addPowerpointEmbedCode_whenProjectIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int id = 0;
    String link = "TEST";
    Project project = new Project();

    boolean actual = sut.addPowerpointEmbedCode(id, link);
  }

  @Test
  public void getEmbedCode_whenParametersAreValid_shouldReturnTheEmbedCode() {
    String expected = "TEST";
    int id = 1;
    Project project = new Project();
    project.setPowerpointEmbedCode(expected);
    when(this.projectsRepositoryMock.getById(id)).thenReturn(project);

    String actual = sut.getEmbedCode(id);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getEmbedCode_whenProjectIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int id = 0;

    String actual = sut.getEmbedCode(id);
  }

  @Test
  public void getFavouriteProjects_whenParametersAreValid_shouldReturnTopFiveProjects() {
    List<User> users = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      List<Project> favourite = new ArrayList<>();
      Project project = new Project();
      project.setTopProject(false);
      project.setId(i);
      favourite.add(project);
      User user = new User();
      user.setFavoriteProjects(favourite);
      users.add(user);
    }
    User another = users.get(0);
    List<Project> addHere = another.getFavoriteProjects();
    Project newProject = new Project();
    newProject.setTopProject(true);
    newProject.setId(0);
    another.setFavoriteProjects(addHere);
    users.add(another);
    List<Project> expected = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Project project = new Project();
      project.setTopProject(true);
      project.setId(i);
      expected.add(project);
    }
    when(this.usersRepositoryMock.getAll()).thenReturn(users);
    when(this.projectsRepositoryMock.getById(isA(Integer.class))).thenAnswer(invocation -> {
      int id = invocation.getArgument(0);
      switch (id) {
        case 0:
          return users.get(0).getFavoriteProjects().get(0);
        case 1:
          return users.get(1).getFavoriteProjects().get(0);
        case 2:
          return users.get(2).getFavoriteProjects().get(0);
        case 3:
          return users.get(3).getFavoriteProjects().get(0);
        case 4:
          return users.get(4).getFavoriteProjects().get(0);
      }

      return null;
    });
    when(this.projectsRepositoryMock.update(isA(Project.class))).thenAnswer(invocation -> {
      Project entity = invocation.getArgument(0);
      entity.setTopProject(true);
      return entity;
    });

    List<Project> actual = sut.getFavouriteProjects();

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createNewUpdate_whenParametersAreValidAndDateIsValid_shouldReturnTheNewUpdate() {
    int projectId = 1;
    String title = "Test";
    String content = "Test";
    String date = "01/01/2000 20:50";
    String type = "green";
    Update expected = new Update();
    expected.setTitle(title);
    expected.setContent(content);
    expected.setDate(date);
    expected.setType(type);
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(new Project());
    when(this.updatesRepositoryMock.create(isA(Update.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Update actual = sut.createNewUpdate(projectId,
            title,
            content,
            date,
            type);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createNewUpdate_whenParametersAreValidAndDateIsNotValid_shouldReturnTheNewUpdate() {
    int projectId = 1;
    String title = "Test";
    String content = "Test";
    String date = "random";
    String type = "green";
    Update expected = new Update();
    expected.setId(0);
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(new Project());
    when(this.updatesRepositoryMock.create(isA(Update.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Update actual = sut.createNewUpdate(projectId,
            title,
            content,
            date,
            type);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getWantedMember_whenIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int id = 0;

    sut.getWantedMember(id);
  }

  @Test
  public void getWantedMember_whenParametersAreValid_shouldReturnTheWantedMmeber() {
    int id = 1;
    WantedMember expected = new WantedMember();
    expected.setId(1);
    when(this.wantedMembersRepositoryMock.getById(id)).thenReturn(expected);

    WantedMember actual = sut.getWantedMember(id);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void editProject_whenParametersAreValid_shouldEditProjectNeedsAndReturnResponseProjectIdWithProjectToEditId() {
    ResponseProjectId expected = new ResponseProjectId(1);
    int id = 1;
    CreateProject project = new CreateProject(1, "valid name", "Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmklcmasdmklcmklasdmcklsadmsdlamclkmsadlkcmsdklaklcasdmacmklmasasdmklasdklmcmklasmcklmasdklcmasmkldcmlkasdklmc123456789", "goal 1");
    project.setId(id);
    project.setParameters(new ArrayList<>());
    project.setWantedMembers(new ArrayList<>());
    List<UserCategory> projectNeeds = new ArrayList<>();
    UserCategory userCategory = new UserCategory();
    userCategory.setName("TEST");
    projectNeeds.add(userCategory);
    project.setProjectNeeds(projectNeeds);
    when(this.userCategoryiesRepository.getAll()).thenReturn(projectNeeds);
    when(this.projectsRepositoryMock.getById(id)).thenReturn(new Project(id));

    ResponseProjectId actual = sut.editProject(project);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createWantedMembers_whenCalled_shouldReturnCorrectListWithWantedMembers() {
    List<WantedMember> expected = new ArrayList<>();
    Project project = new Project();
    project.setWantedMembers(new ArrayList<>(
            Arrays.asList(new WantedMember(new UserCategory("Програмист")),
                    new WantedMember(new UserCategory("Дизайнер")),
                    new WantedMember(new UserCategory("Инженер")),
                    new WantedMember(new UserCategory("Писател")),
                    new WantedMember(new UserCategory("Учен")),
                    new WantedMember(new UserCategory("Музикант")),
                    new WantedMember(new UserCategory("Режисьор")),
                    new WantedMember(new UserCategory("Продуктов мениджър")),
                    new WantedMember(new UserCategory("Артист")))
    ));
    List<Skill> skills = new ArrayList<>();
    skills.add(new Skill(1, "TEST SKILL"));
    List<Language> languages = new ArrayList<>();
    languages.add(new Language(1, "TEST LANGUAGE"));
    WantedMember programmer = new WantedMember(1,
            project,
            new City(1, "TEST CITY"),
            "Бакалавър",
            skills,
            languages,
            new UserCategory(1, "Програмист"));
    WantedMember designer = new WantedMember(1,
            project,
            new City(1, "TEST CITY"),
            "Бакалавър",
            skills,
            languages,
            new UserCategory(2, "Дизайнер"));
    WantedMember engineer = new WantedMember(1,
            project,
            new City(1, "TEST CITY"),
            "Бакалавър",
            skills,
            languages,
            new UserCategory(3, "Инженер"));
    WantedMember writer = new WantedMember(1,
            project,
            new City(1, "TEST CITY"),
            "Бакалавър",
            skills,
            languages,
            new UserCategory(4, "Писател"));
    WantedMember scientist = new WantedMember(1,
            project,
            new City(1, "TEST CITY"),
            "Бакалавър",
            skills,
            languages,
            new UserCategory(5, "Учен"));
    WantedMember musician = new WantedMember(1,
            project,
            new City(1, "TEST CITY"),
            "Бакалавър",
            skills,
            languages,
            new UserCategory(6, "Музикант"));
    WantedMember filmmaker = new WantedMember(1,
            project,
            new City(1, "TEST CITY"),
            "Бакалавър",
            skills,
            languages,
            new UserCategory(7, "Режисьор"));
    WantedMember productManager = new WantedMember(1,
            project,
            new City(1, "TEST CITY"),
            "Бакалавър",
            skills,
            languages,
            new UserCategory(8, "Продуктов мениджър"));
    WantedMember artist = new WantedMember(1,
            project,
            new City(1, "TEST CITY"),
            "Бакалавър",
            skills,
            languages,
            new UserCategory(9, "Артист"));
    expected.add(programmer);
    expected.add(designer);
    expected.add(engineer);
    expected.add(writer);
    expected.add(scientist);
    expected.add(musician);
    expected.add(filmmaker);
    expected.add(productManager);
    expected.add(artist);
    List<Parameter> parameters = new ArrayList<>();
    Parameter parameter = new Parameter("programmer",
            "skills",
            new ArrayList<>(Collections.singletonList("TEST SKILL")));
    Parameter parameter1 = new Parameter("programmer",
            "languages",
            new ArrayList<>(Collections.singletonList("TEST LANGUAGE")));
    Parameter parameter2 = new Parameter("programmer",
            "city",
            new ArrayList<>(Collections.singletonList("TEST CITY")));
    Parameter parameter3 = new Parameter("programmer",
            "education",
            new ArrayList<>(Collections.singletonList("3")));
    Parameter parameter4 = new Parameter("designer",
            "skills",
            new ArrayList<>(Collections.singletonList("TEST SKILL")));
    Parameter parameter5 = new Parameter("designer",
            "languages",
            new ArrayList<>(Collections.singletonList("TEST LANGUAGE")));
    Parameter parameter6 = new Parameter("designer",
            "city",
            new ArrayList<>(Collections.singletonList("TEST CITY")));
    Parameter parameter7 = new Parameter("designer",
            "education",
            new ArrayList<>(Collections.singletonList("3")));
    Parameter parameter8 = new Parameter("engineer",
            "skills",
            new ArrayList<>(Collections.singletonList("TEST SKILL")));
    Parameter parameter9 = new Parameter("engineer",
            "languages",
            new ArrayList<>(Collections.singletonList("TEST LANGUAGE")));
    Parameter parameter10 = new Parameter("engineer",
            "city",
            new ArrayList<>(Collections.singletonList("TEST CITY")));
    Parameter parameter11 = new Parameter("engineer",
            "education",
            new ArrayList<>(Collections.singletonList("3")));
    Parameter parameter12 = new Parameter("writer",
            "skills",
            new ArrayList<>(Collections.singletonList("TEST SKILL")));
    Parameter parameter13 = new Parameter("writer",
            "languages",
            new ArrayList<>(Collections.singletonList("TEST LANGUAGE")));
    Parameter parameter14 = new Parameter("writer",
            "city",
            new ArrayList<>(Collections.singletonList("TEST CITY")));
    Parameter parameter15 = new Parameter("writer",
            "education",
            new ArrayList<>(Collections.singletonList("3")));
    Parameter parameter16 = new Parameter("scientist",
            "skills",
            new ArrayList<>(Collections.singletonList("TEST SKILL")));
    Parameter parameter17 = new Parameter("scientist",
            "languages",
            new ArrayList<>(Collections.singletonList("TEST LANGUAGE")));
    Parameter parameter18 = new Parameter("scientist",
            "city",
            new ArrayList<>(Collections.singletonList("TEST CITY")));
    Parameter parameter19 = new Parameter("scientist",
            "education",
            new ArrayList<>(Collections.singletonList("3")));
    Parameter parameter20 = new Parameter("musician",
            "skills",
            new ArrayList<>(Collections.singletonList("TEST SKILL")));
    Parameter parameter21 = new Parameter("musician",
            "languages",
            new ArrayList<>(Collections.singletonList("TEST LANGUAGE")));
    Parameter parameter22 = new Parameter("musician",
            "city",
            new ArrayList<>(Collections.singletonList("TEST CITY")));
    Parameter parameter23 = new Parameter("musician",
            "education",
            new ArrayList<>(Collections.singletonList("3")));
    Parameter parameter24 = new Parameter("filmmaker",
            "skills",
            new ArrayList<>(Collections.singletonList("TEST SKILL")));
    Parameter parameter25 = new Parameter("filmmaker",
            "languages",
            new ArrayList<>(Collections.singletonList("TEST LANGUAGE")));
    Parameter parameter26 = new Parameter("filmmaker",
            "city",
            new ArrayList<>(Collections.singletonList("TEST CITY")));
    Parameter parameter27 = new Parameter("filmmaker",
            "education",
            new ArrayList<>(Collections.singletonList("3")));
    Parameter parameter28 = new Parameter("productManager",
            "skills",
            new ArrayList<>(Collections.singletonList("TEST SKILL")));
    Parameter parameter29 = new Parameter("productManager",
            "languages",
            new ArrayList<>(Collections.singletonList("TEST LANGUAGE")));
    Parameter parameter30 = new Parameter("productManager",
            "city",
            new ArrayList<>(Collections.singletonList("TEST CITY")));
    Parameter parameter31 = new Parameter("productManager",
            "education",
            new ArrayList<>(Collections.singletonList("3")));
    Parameter parameter32 = new Parameter("artist",
            "skills",
            new ArrayList<>(Collections.singletonList("TEST SKILL")));
    Parameter parameter33 = new Parameter("artist",
            "languages",
            new ArrayList<>(Collections.singletonList("TEST LANGUAGE")));
    Parameter parameter34 = new Parameter("artist",
            "city",
            new ArrayList<>(Collections.singletonList("TEST CITY")));
    Parameter parameter35 = new Parameter("artist",
            "education",
            new ArrayList<>(Collections.singletonList("3")));
    parameters.add(parameter);
    parameters.add(parameter1);
    parameters.add(parameter2);
    parameters.add(parameter3);
    parameters.add(parameter4);
    parameters.add(parameter5);
    parameters.add(parameter6);
    parameters.add(parameter7);
    parameters.add(parameter8);
    parameters.add(parameter9);
    parameters.add(parameter10);
    parameters.add(parameter11);
    parameters.add(parameter12);
    parameters.add(parameter13);
    parameters.add(parameter14);
    parameters.add(parameter15);
    parameters.add(parameter16);
    parameters.add(parameter17);
    parameters.add(parameter18);
    parameters.add(parameter19);
    parameters.add(parameter20);
    parameters.add(parameter21);
    parameters.add(parameter22);
    parameters.add(parameter23);
    parameters.add(parameter24);
    parameters.add(parameter25);
    parameters.add(parameter26);
    parameters.add(parameter27);
    parameters.add(parameter28);
    parameters.add(parameter29);
    parameters.add(parameter30);
    parameters.add(parameter31);
    parameters.add(parameter32);
    parameters.add(parameter33);
    parameters.add(parameter34);
    parameters.add(parameter35);
    when(this.skillsRepositoryMock.getAll()).thenReturn(skills);
    when(this.languagesRepositoryMock.getAll()).thenReturn(languages);
    when(this.citiesRepositoryMock.getAll()).thenReturn(new ArrayList<>(Collections.singletonList(new City(1, "TEST CITY"))));
    when(this.userCategoryiesRepository.getAll()).thenReturn(new ArrayList<>(
            Arrays.asList(new UserCategory(1, "Програмист"),
                    new UserCategory(2, "Дизайнер"),
                    new UserCategory(3, "Инженер"),
                    new UserCategory(4, "Писател"),
                    new UserCategory(5, "Учен"),
                    new UserCategory(6, "Музикант"),
                    new UserCategory(7, "Режисьор"),
                    new UserCategory(8, "Продуктов мениджър"),
                    new UserCategory(9, "Артист"))
    ));
    when(this.wantedMembersRepositoryMock.create(isA(WantedMember.class))).thenAnswer(invocation -> {
      WantedMember entity = invocation.getArgument(0);
      entity.setId(1);
      return entity;
    });

    List<WantedMember> actual = sut.createWantedMembers(parameters, project);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void deleteUser_whenIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int id = 0;

    sut.deleteUser(id);
  }

  @Test
  public void deleteUser_whenParametersAreValid_shouldReturnTheDeletedUser() {
    int id = 1;
    User expected = new User();
    expected.setId(1);
    expected.setImage("test image");
    expected.setMessages(new ArrayList<>(Collections.singletonList(new Message())));
    expected.setNotifications(new ArrayList<>(Collections.singletonList(new Notification())));
    expected.setReviews(new ArrayList<>(Collections.singletonList(new Review())));
    expected.setMessages(new ArrayList<>(Collections.singletonList(new Message())));
    Project project = new Project();
    project.setId(1);
    project.setCover("test");
    project.setPicture1("test");
    project.setCreator(0);
    project.setMembers(new ArrayList<>(Collections.singletonList(new User(1))));
    Project project1 = new Project();
    project1.setCreator(1);
    project1.setId(2);
    project1.setCover("test");
    project1.setPicture1("test");
    expected.setProjects(new ArrayList<>(Arrays.asList(project, project1)));
    when(this.usersRepositoryMock.delete(isA(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(this.usersRepositoryMock.getById(isA(Integer.class))).thenReturn(expected);
    when(this.projectsRepositoryMock.getById(1)).thenReturn(project);
    when(this.projectsRepositoryMock.getById(2)).thenReturn(project1);

    User actual = sut.deleteUser(id);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getWantedMembers_whenIdIsNotValid_shouldThrowInvalidParameterException(){
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int id = 0;

    sut.getWantedMembers(id);
  }

  @Test
  public void getWantedMembers_whenParametersAreValid_shouldReturnWantedMembers(){
    int id = 1;
    List<WantedMember> expected = new ArrayList<>(Collections.singletonList(new WantedMember(1)));
    Project project = new Project();
    project.setWantedMembers(expected);
    when(this.projectsRepositoryMock.getById(id)).thenReturn(project);

    List<WantedMember> actual = sut.getWantedMembers(id);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectNeeds_whenIdIsNotValid_shouldThrowInvalidParameterException(){
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int id = 0;

    sut.getProjectNeeds(id);
  }

  @Test
  public void getProjectNeeds_whenParametersAreValid_shouldReturnProjectNeeds(){
    int id = 1;
    List<UserCategory> expected = new ArrayList<>(Collections.singletonList(new UserCategory("test")));
    Project project = new Project();
    project.setProjectNeeds(expected);
    when(this.projectsRepositoryMock.getById(id)).thenReturn(project);

    List<UserCategory> actual = sut.getProjectNeeds(id);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getProjectImages_whenIdIsNotValid_shouldThrowInvalidParameterException(){
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int projectId = 0;

    sut.getProjectImages(projectId);
  }

  @Test
  public void getProjectImages_whenParametersAreValid_shouldReturnListWIthFineUploaderImage(){
    int projectId = 1;
    List<FineUploaderImage> expected = new ArrayList<>();
    for (int i = 0; i < 10; i++){
      expected.add(new FineUploaderImage("/test",
              "dsfs",
              "/test",
              12345,
              "/api/deleteProjectFile",
              new FileUploaderParameters(projectId, i)));
    }
    Project project = new Project();
    project.setCover("/test");
    project.setPicture1("/test");
    project.setPicture2("/test");
    project.setPicture3("/test");
    project.setPicture4("/test");
    project.setPicture5("/test");
    project.setPicture6("/test");
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(project);

    List<FineUploaderImage> actual = sut.getProjectImages(projectId);
  }

  @Test
  public void deleteProjectFile_whenIdIsNotValid_shouldThrowInvalidParameterException(){
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int projectId = 0;

    sut.getProjectImages(projectId);
  }

  @Test
  public void deleteProjectFile_whenParametersAreValid_shouldReturnStatusCode200(){
    int projectId = 1;
    Project project = new Project();
    project.setCover("test");
    project.setPicture1("test");
    project.setPicture2("test");
    project.setPicture3("test");
    project.setPicture4("test");
    project.setPicture5("test");
    project.setPicture6("test");
    when(this.projectsRepositoryMock.getById(projectId)).thenReturn(project);

    int actual1 = sut.deleteProjectFile(projectId, 1);
    int actual2 = sut.deleteProjectFile(projectId, 2);
    int actual3 = sut.deleteProjectFile(projectId, 3);
    int actual4 = sut.deleteProjectFile(projectId, 4);
    int actual5 = sut.deleteProjectFile(projectId, 5);
    int actual6 = sut.deleteProjectFile(projectId, 6);
    int actual7 = sut.deleteProjectFile(projectId, 7);
    int actual8 = sut.deleteProjectFile(projectId, 8);
    int actual9 = sut.deleteProjectFile(projectId, 9);
    int actual10 = sut.deleteProjectFile(projectId, 112);

    Assert.assertEquals(actual1, 200);
    Assert.assertEquals(actual2, 200);
    Assert.assertEquals(actual3, 200);
    Assert.assertEquals(actual4, 200);
    Assert.assertEquals(actual5, 200);
    Assert.assertEquals(actual6, 200);
    Assert.assertEquals(actual7, 200);
    Assert.assertEquals(actual8, 200);
    Assert.assertEquals(actual9, 200);
    Assert.assertEquals(actual10, 404);
  }
}