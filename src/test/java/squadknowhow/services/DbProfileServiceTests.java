package squadknowhow.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.core.userdetails.UserDetails;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.*;
import squadknowhow.request.models.EditedUser;
import squadknowhow.request.models.SentMessage;
import squadknowhow.response.models.ResponsePagination;
import squadknowhow.response.models.ResponseSuccessful;
import squadknowhow.utils.validators.EditedUserValidator;
import squadknowhow.utils.validators.EmailValidator;
import squadknowhow.utils.validators.IdValidator;
import squadknowhow.utils.validators.PasswordValidator;
import squadknowhow.utils.validators.base.IValidator;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

public class DbProfileServiceTests {
  @Mock
  private IRepository<User> usersRepositoryMock;
  @Mock
  private IRepository<UserShort> usersShortRepository;
  @Mock
  private IRepository<Notification> notificationsRepositoryMock;
  @Mock
  private IRepository<Message> messagesRepositoryMock;
  @Mock
  private IRepository<Interest> interestsRepositoryMock;
  @Mock
  private IRepository<Skill> skillsRepositoryMock;
  @Mock
  private IRepository<City> citiesRepositoryMock;
  @Mock
  private IRepository<UserCategory> userCategoriesRepositoryMock;

  private IValidator<Integer> idValidator;
  private IValidator<EditedUser> editedUserValidator;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Before
  public void setUpValidators() {
    this.idValidator = new IdValidator();
    this.editedUserValidator = new EditedUserValidator(new PasswordValidator(),
            new EmailValidator());
  }

  @Test
  public void deleteNotification_whenNotificationIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NotificationId is not valid");
    int notificationId = 0;
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    sut.deleteNotification(notificationId);
  }

  @Test
  public void deleteNotification_whenNotificationIdIsValid_shouldReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    int notificationId = 1;
    when(this.notificationsRepositoryMock.getById(notificationId)).thenReturn(new Notification());
    when(this.notificationsRepositoryMock.delete(isA(Notification.class))).thenReturn(null);
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    ResponseSuccessful actual = sut.deleteNotification(notificationId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void editProfile_whenUserIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("User id is not valid");
    EditedUser user = new EditedUser(0);
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    sut.editProfile(user);
  }

  @Test
  public void editProfile_whenFirstNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("User is not valid");
    EditedUser user = new EditedUser(1, "");
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    sut.editProfile(user);
  }

  @Test
  public void editProfile_whenFirstNameIsLessThan2Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("User is not valid");
    EditedUser user = new EditedUser(1, "1");
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    sut.editProfile(user);
  }

  @Test
  public void editProfile_whenFirstNameIsMoreThan16Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("User is not valid");
    EditedUser user = new EditedUser(1, "123456675487657437346757567");
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    sut.editProfile(user);
  }

  @Test
  public void editProfile_whenLastNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("User is not valid");
    EditedUser user = new EditedUser(1, "Valid firstName", "");
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    sut.editProfile(user);
  }

  @Test
  public void editProfile_whenLastNameIsLessThan2Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("User is not valid");
    EditedUser user = new EditedUser(1, "Valid firstName", "1");
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    sut.editProfile(user);
  }

  @Test
  public void editProfile_whenLastNameIsMoreThan16Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("User is not valid");
    EditedUser user = new EditedUser(1, "Valid firstName", "1287348912734871238047128904780");
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    sut.editProfile(user);
  }

  @Test
  public void editProfile_whenDescriptionIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("User is not valid");
    EditedUser user = new EditedUser(1, "Valid firstName", "Valid lastName", "");
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    sut.editProfile(user);
  }

  @Test
  public void editProfile_WhenDescriptionIsLessThan25Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("User is not valid");
    EditedUser user = new EditedUser(1, "Valid firstName", "ValidfirstName", "1");
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    sut.editProfile(user);
  }

  @Test
  public void editProfile_whenUserIsValid_shouldReturnTrue() {
    EditedUser user = new EditedUser(1, "Valid firstName", "ValidfirstName", "Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmk", "emailtopass@abv.bg", "1234567890");
    when(this.usersRepositoryMock.getById(1)).thenReturn(new User());
    when(this.usersRepositoryMock.update(isA(User.class))).thenReturn(null);
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    boolean actual = sut.editProfile(user);

    Assert.assertTrue(actual);
  }

  @Test
  public void deleteMessage_whenMessageIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("MessageId is not valid");
    int messageId = 0;
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    sut.deleteMessage(messageId);
  }

  @Test
  public void deleteMessage_whenMessageIdIsValid_shouldReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    int messageId = 1;
    when(this.messagesRepositoryMock.getById(messageId)).thenReturn(new Message());
    when(this.messagesRepositoryMock.delete(isA(Message.class))).thenReturn(null);
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    ResponseSuccessful actual = sut.deleteMessage(messageId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void sendMessage_whenMessageSenderIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("SenderId is not valid");
    SentMessage message = new SentMessage(0);
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    sut.sendMessage(message);
  }

  @Test
  public void sendMessage_whenMessageRecipientIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("RecipientId is not valid");
    SentMessage message = new SentMessage(1, 0);
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    sut.sendMessage(message);
  }

  @Test
  public void sendMessage_whenMessageTopicIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Topic is empty");
    SentMessage message = new SentMessage(1, 1, "");
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    sut.sendMessage(message);
  }

  @Test
  public void sendMessage_whenMessageIsValid_shouldReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    SentMessage message = new SentMessage(1, 1, "ValidTopic");
    when(this.usersRepositoryMock.getById(1)).thenReturn(new User());
    when(this.messagesRepositoryMock.create(isA(Message.class))).thenReturn(null);
    when(this.notificationsRepositoryMock.create(isA(Notification.class))).thenReturn(null);
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    ResponseSuccessful actual = sut.sendMessage(message);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getUserById_whenIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int id = 0;
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    sut.getUserById(id);
  }

  @Test
  public void getUserById_whenIdIsValid_shouldReturnUser() {
    int id = 1;
    when(this.usersRepositoryMock.getById(id)).thenReturn(new User(id));
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    User actual = sut.getUserById(id);

    Assert.assertEquals(id, actual.getId());
  }

  @Test
  public void getInterest_withValidParameters_shouldReturnTheInterest() {
    String interestName = "name";
    Interest expected = new Interest(interestName);
    List<Interest> interests = new ArrayList<>();
    interests.add(new Interest(interestName));
    when(this.interestsRepositoryMock.getAll()).thenReturn(interests);
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    Interest actual = sut.getInterest(interestName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getSkill_withValidParameters_shouldReturnTheSkill() {
    String skillName = "name";
    Skill expected = new Skill(skillName);
    List<Skill> skills = new ArrayList<>();
    skills.add(new Skill(skillName));
    when(this.skillsRepositoryMock.getAll()).thenReturn(skills);
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    Skill actual = sut.getSkill(skillName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getCity_withValidParameters_shouldReturnTheCity() {
    String cityName = "name";
    City expected = new City(cityName);
    List<City> cities = new ArrayList<>();
    cities.add(new City(cityName));
    when(this.citiesRepositoryMock.getAll()).thenReturn(cities);
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    City actual = sut.getCity(cityName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getUserCategory_withValidParameters_shouldReturnTheUserCategory() {
    String userCategoryName = "name";
    UserCategory expected = new UserCategory(userCategoryName);
    List<UserCategory> userCategories = new ArrayList<>();
    userCategories.add(new UserCategory(userCategoryName));
    when(this.userCategoriesRepositoryMock.getAll()).thenReturn(userCategories);
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    UserCategory actual = sut.getUserCategory(userCategoryName);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getUsers_whenAllParametersAreValidAndEntriesAreLessThan20_shouldRetrunTheUsers() {
    int page = 1;
    String name = "name";
    String userCategory = "programmer";
    String city = "city";
    String skills = "skills";
    String interests = "interests";
    List<UserShort> expected = new ArrayList<>();
    UserShort user = new UserShort();
    user.setFirstName(name);
    user.setSkillset(new UserCategory(userCategory));
    user.setCity(new City(city));
    List<Skill> skills2 = new ArrayList<>();
    skills2.add(new Skill(skills));
    user.setSkills(skills2);
    List<Interest> interests2 = new ArrayList<>();
    interests2.add(new Interest(interests));
    user.setInterests(interests2);
    expected.add(user);
    when(this.usersShortRepository.getAll()).thenReturn(expected);
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    List<UserShort> actual = sut.getUsers(page,
            name,
            userCategory,
            city,
            skills,
            interests);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getUsers_whenAllParametersAreValidAndEntriesAreLessThan20AndSkillsAndInterestsAreMoreThanOne_shouldRetrunTheUsers() {
    int page = 1;
    String name = "name";
    String userCategory = "programmer";
    String city = "city";
    String skills = "skills, skills2, skills3";
    String interests = "interests, interests2, interests3";
    List<UserShort> expected = new ArrayList<>();
    UserShort user = new UserShort();
    user.setFirstName(name);
    user.setSkillset(new UserCategory(userCategory));
    user.setCity(new City(city));
    List<Skill> skills2 = new ArrayList<>();
    skills2.add(new Skill("skills"));
    skills2.add(new Skill("skills2"));
    skills2.add(new Skill("skills3"));
    user.setSkills(skills2);
    List<Interest> interests2 = new ArrayList<>();
    interests2.add(new Interest("interests"));
    interests2.add(new Interest("interests2"));
    interests2.add(new Interest("interests3"));
    user.setInterests(interests2);
    expected.add(user);
    when(this.usersShortRepository.getAll()).thenReturn(expected);
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    List<UserShort> actual = sut.getUsers(page,
            name,
            userCategory,
            city,
            skills,
            interests);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getUsers_whenAllParametersAreValid_shouldRetrunTheUsers() {
    ResponsePagination expected = new ResponsePagination(1, 2);
    String name = "name";
    String userCategory = "programmer";
    String city = "city";
    String skills = "skills, skills2, skills3";
    String interests = "interests, interests2, interests3";
    List<UserShort> users = new ArrayList<>();
    UserShort user = new UserShort();
    user.setFirstName(name);
    user.setSkillset(new UserCategory(userCategory));
    user.setCity(new City(city));
    List<Skill> skills2 = new ArrayList<>();
    skills2.add(new Skill("skills"));
    skills2.add(new Skill("skills2"));
    skills2.add(new Skill("skills3"));
    user.setSkills(skills2);
    List<Interest> interests2 = new ArrayList<>();
    interests2.add(new Interest("interests"));
    interests2.add(new Interest("interests2"));
    interests2.add(new Interest("interests3"));
    user.setInterests(interests2);
    users.add(user);
    UserShort user2 = new UserShort();
    user2.setFirstName(name);
    user2.setSkillset(new UserCategory(userCategory));
    user2.setCity(new City(city));
    List<Skill> skills3 = new ArrayList<>();
    skills3.add(new Skill("skills"));
    skills3.add(new Skill("skills2"));
    skills3.add(new Skill("skills3"));
    user2.setSkills(skills3);
    List<Interest> interests3 = new ArrayList<>();
    interests3.add(new Interest("interests"));
    interests3.add(new Interest("interests2"));
    interests3.add(new Interest("interests3"));
    user2.setInterests(interests3);
    users.add(user2);
    when(this.usersShortRepository.getAll()).thenReturn(users);
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    ResponsePagination actual = sut.getPeoplePages(name,
            userCategory,
            city,
            skills,
            interests);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void tourCompleted_whenIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int id = 0;
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    sut.tourCompleted(id);
  }

  @Test
  public void tourCompleted_whenParametersAreValid_shouldReturnTrue() {
    int id = 1;
    when(this.usersRepositoryMock.getById(id)).thenReturn(new User());
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    boolean actual = sut.tourCompleted(id);

    Assert.assertTrue(actual);
  }

  @Test
  public void loadUserByUsername_whenParametersAreValid_shouldReturnValidUserDetails() {
    String username = "test";
    List<User> users = new ArrayList<>();
    User user = new User();
    user.setEmail("test");
    user.setPassword("$2a$10$pOB/qGMNZ2Cv2ELGtSW6NOsnWXOgoOFPuozUfWJL0eM563T6mOlNi");
    users.add(user);
    when(this.usersRepositoryMock.getAll()).thenReturn(users);
    DbProfileService sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.editedUserValidator);

    UserDetails actual = sut.loadUserByUsername(username);

    Assert.assertNotNull(actual);
  }
}
