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
import squadknowhow.request.models.PersonalInfo;
import squadknowhow.request.models.RequestBase64;
import squadknowhow.request.models.SentMessage;
import squadknowhow.response.models.ResponseCall;
import squadknowhow.response.models.ResponseCheckCall;
import squadknowhow.response.models.ResponsePagination;
import squadknowhow.response.models.ResponseSuccessful;
import squadknowhow.response.models.UserInformation;
import squadknowhow.utils.validators.EditedUserValidator;
import squadknowhow.utils.validators.EmailValidator;
import squadknowhow.utils.validators.IdValidator;
import squadknowhow.utils.validators.PasswordValidator;
import squadknowhow.utils.validators.base.IValidator;

import java.io.IOException;
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
  @Mock
  private IRepository<Note> notesRepositoryMock;

  private IValidator<Integer> idValidator;
  private IValidator<EditedUser> editedUserValidator;
  private DbProfileService sut;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Before
  public void setUpSUT() {
    this.idValidator = new IdValidator();
    this.editedUserValidator = new EditedUserValidator(new PasswordValidator(),
            new EmailValidator());
    sut = new DbProfileService(this.usersRepositoryMock,
            this.usersShortRepository,
            this.notificationsRepositoryMock,
            this.messagesRepositoryMock,
            this.interestsRepositoryMock,
            this.skillsRepositoryMock,
            this.citiesRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.idValidator,
            this.notesRepositoryMock);
  }

  @Test
  public void deleteNotification_whenNotificationIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NotificationId is not valid");
    int notificationId = 0;

    sut.deleteNotification(notificationId);
  }

  @Test
  public void deleteNotification_whenNotificationIdIsValid_shouldReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    int notificationId = 1;
    when(this.notificationsRepositoryMock.getById(notificationId)).thenReturn(new Notification());
    when(this.notificationsRepositoryMock.delete(isA(Notification.class))).thenReturn(null);

    ResponseSuccessful actual = sut.deleteNotification(notificationId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void deleteMessage_whenMessageIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("MessageId is not valid");
    int messageId = 0;

    sut.deleteMessage(messageId);
  }

  @Test
  public void deleteMessage_whenMessageIdIsValid_shouldReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    int messageId = 1;
    when(this.messagesRepositoryMock.getById(messageId)).thenReturn(new Message());
    when(this.messagesRepositoryMock.delete(isA(Message.class))).thenReturn(null);

    ResponseSuccessful actual = sut.deleteMessage(messageId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void sendMessage_whenMessageSenderIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("SenderId is not valid");
    SentMessage message = new SentMessage(0);

    sut.sendMessage(message);
  }

  @Test
  public void sendMessage_whenMessageRecipientIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("RecipientId is not valid");
    SentMessage message = new SentMessage(1, 0);

    sut.sendMessage(message);
  }

  @Test
  public void sendMessage_whenMessageTopicIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Topic is empty");
    SentMessage message = new SentMessage(1, 1, "");

    sut.sendMessage(message);
  }

  @Test
  public void sendMessage_whenMessageIsValid_shouldReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    SentMessage message = new SentMessage(1, 1, "ValidTopic");
    when(this.usersRepositoryMock.getById(1)).thenReturn(new User());
    when(this.messagesRepositoryMock.create(isA(Message.class))).thenReturn(null);
    when(this.notificationsRepositoryMock.create(isA(Notification.class))).thenReturn(null);

    ResponseSuccessful actual = sut.sendMessage(message);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getUserById_whenIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int id = 0;

    sut.getUserById(id);
  }

  @Test
  public void getUserById_whenIdIsValid_shouldReturnUser() {
    int id = 1;
    when(this.usersRepositoryMock.getById(id)).thenReturn(new User(id));

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
    String languages = "languages";
    List<UserShort> expected = new ArrayList<>();
    UserShort user = new UserShort();
    user.setFirstName(name);
    user.setLastName("");
    user.setSkillset(new UserCategory(userCategory));
    user.setCity(new City(city));
    user.setActivated(true);
    List<Skill> skills2 = new ArrayList<>();
    skills2.add(new Skill(skills));
    user.setSkills(skills2);
    List<Language> languages2 = new ArrayList<>();
    languages2.add(new Language(languages));
    user.setLanguages(languages2);
    expected.add(user);
    when(this.usersShortRepository.getAll()).thenReturn(expected);

    List<UserShort> actual = sut.getUsers(page,
            name,
            userCategory,
            city,
            skills,
            languages);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getUsers_whenAllParametersAreValidAndEntriesAreLessThan20AndSkillsAndInterestsAreMoreThanOne_shouldReturnTheUsers() {
    int page = 1;
    String name = "name";
    String userCategory = "programmer";
    String city = "city";
    String skills = "skills, skills2, skills3";
    String languages = "languages, languages2, languages3";
    List<UserShort> expected = new ArrayList<>();
    UserShort user = new UserShort();
    user.setFirstName(name);
    user.setLastName("");
    user.setSkillset(new UserCategory(userCategory));
    user.setCity(new City(city));
    user.setActivated(true);
    List<Skill> skills2 = new ArrayList<>();
    skills2.add(new Skill("skills"));
    skills2.add(new Skill("skills2"));
    skills2.add(new Skill("skills3"));
    user.setSkills(skills2);
    List<Language> languages2 = new ArrayList<>();
    languages2.add(new Language("languages"));
    languages2.add(new Language("languages2"));
    languages2.add(new Language("languages3"));
    user.setLanguages(languages2);
    expected.add(user);
    when(this.usersShortRepository.getAll()).thenReturn(expected);

    List<UserShort> actual = sut.getUsers(page,
            name,
            userCategory,
            city,
            skills,
            languages);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getUsers_whenAllParametersAreValid_shouldRetrunTheUsers() {
    ResponsePagination expected = new ResponsePagination(1, 2);
    String name = "name";
    String userCategory = "programmer";
    String city = "city";
    String skills = "skills, skills2, skills3";
    String interests = "languages, languages2, languages3";
    List<UserShort> users = new ArrayList<>();
    UserShort user = new UserShort();
    user.setFirstName(name);
    user.setLastName("");
    user.setSkillset(new UserCategory(userCategory));
    user.setCity(new City(city));
    user.setActivated(true);
    List<Skill> skills2 = new ArrayList<>();
    skills2.add(new Skill("skills"));
    skills2.add(new Skill("skills2"));
    skills2.add(new Skill("skills3"));
    user.setSkills(skills2);
    List<Language> languages2 = new ArrayList<>();
    languages2.add(new Language("languages"));
    languages2.add(new Language("languages2"));
    languages2.add(new Language("languages3"));
    user.setLanguages(languages2);
    users.add(user);
    UserShort user2 = new UserShort();
    user2.setFirstName(name);
    user2.setLastName("");
    user2.setSkillset(new UserCategory(userCategory));
    user2.setCity(new City(city));
    user2.setActivated(true);
    List<Skill> skills3 = new ArrayList<>();
    skills3.add(new Skill("skills"));
    skills3.add(new Skill("skills2"));
    skills3.add(new Skill("skills3"));
    user2.setSkills(skills3);
    List<Language> languages3 = new ArrayList<>();
    languages3.add(new Language("languages"));
    languages3.add(new Language("languages2"));
    languages3.add(new Language("languages3"));
    user2.setLanguages(languages3);
    users.add(user2);
    when(this.usersShortRepository.getAll()).thenReturn(users);

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

    sut.tourCompleted(id);
  }

  @Test
  public void tourCompleted_whenParametersAreValid_shouldReturnTrue() {
    int id = 1;
    when(this.usersRepositoryMock.getById(id)).thenReturn(new User());

    boolean actual = sut.tourCompleted(id);

    Assert.assertTrue(actual);
  }

  @Test
  public void loadUserByUsername_whenParametersAreValid_shouldReturnValidUserDetails() {
    String username = "test";
    List<User> users = new ArrayList<>();
    User user = new User();
    user.setEmail("test");
    user.setActivated(true);
    user.setPassword("$2a$10$pOB/qGMNZ2Cv2ELGtSW6NOsnWXOgoOFPuozUfWJL0eM563T6mOlNi");
    users.add(user);
    when(this.usersRepositoryMock.getAll()).thenReturn(users);

    UserDetails actual = sut.loadUserByUsername(username);

    Assert.assertNotNull(actual);
  }

  @Test
  public void checkIfUserNeedsTour_whenIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int id = 0;

    sut.checkIfUserNeedsTour(id);
  }

  @Test
  public void checkIfUserNeedsTour_whenIdIsValidAndUserDoesntNeedTour_shouldReturnFalseWithUsersFirstName() {
    UserInformation expected = new UserInformation(false, "Name");
    int id = 1;
    User user = new User();
    user.setNeedsTour(false);
    user.setFirstName("Name");
    when(this.usersRepositoryMock.getById(id)).thenReturn(user);

    UserInformation actual = sut.checkIfUserNeedsTour(id);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void checkIfUserNeedsTour_whenIdIsValidAndUserNeedsTour_shouldReturnTrueWithUsersFirstName() {
    UserInformation expected = new UserInformation(true, "Name");
    int id = 1;
    User user = new User();
    user.setNeedsTour(true);
    user.setFirstName("Name");
    when(this.usersRepositoryMock.getById(id)).thenReturn(user);

    UserInformation actual = sut.checkIfUserNeedsTour(id);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void contact_whenCalled_shouldReturnTrue() {
    boolean actual = sut.contact("name", "whatever", "dsad", "dsada");

    Assert.assertTrue(actual);
  }

  @Test
  public void setOffline_whenCalled_shouldSetTheUserOffline() {
    String email = "test";
    User user = new User();
    user.setEmail(email);
    user.setOnline(true);
    List<User> users = new ArrayList<>();
    users.add(user);
    when(this.usersRepositoryMock.getAll()).thenReturn(users);

    sut.setOffline(email);

    Assert.assertFalse(user.getOnline());
  }

  @Test
  public void setOnline_whenCalled_shouldSetTheUserOnline() {
    String email = "test";
    User user = new User();
    user.setEmail(email);
    user.setOnline(false);
    List<User> users = new ArrayList<>();
    users.add(user);
    when(this.usersRepositoryMock.getAll()).thenReturn(users);

    sut.setOnline(email);

    Assert.assertTrue(user.getOnline());
  }

  @Test
  public void getUserName_whenCreatorIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("CreatorId is not valid");
    int creatorId = 0;

    sut.getUserName(creatorId);
  }

  @Test
  public void getUserName_whenParametersAreValid_shoudReturnTheUsername() {
    int creatorId = 1;
    String expected = "test TEST";
    User user = new User();
    user.setFirstName("test");
    user.setLastName("TEST");
    when(this.usersRepositoryMock.getById(creatorId)).thenReturn(user);

    String actual = sut.getUserName(creatorId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void checkIfOnline_whenNotEveryUserEmailIsValid_shouldReturnResponseCallWithFalse() {
    ResponseCall expected = new ResponseCall(false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    String emails = "hrisko, ";
    int senderId = 1;
    boolean isAudio = false;
    when(this.usersRepositoryMock.getAll()).thenReturn(new ArrayList<>());

    ResponseCall actual = sut.checkIfOnline(emails, senderId, isAudio);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void checkIfOnline_whenThereIsUserButTokenIsNotEmpty_shouldReturnResponseCallWithFalse() {
    ResponseCall expected = new ResponseCall(false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    String emails = "something";
    int senderId = 1;
    boolean isAudio = false;
    when(this.usersRepositoryMock.getAll()).thenReturn(new ArrayList<>());

    ResponseCall actual = sut.checkIfOnline(emails, senderId, isAudio);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void checkIfOnline_whenThereIsUserFoundButHeIsNotOnline_shouldReturnResponseCallWithFalse() {
    ResponseCall expected = new ResponseCall(false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    String emails = "hrisko, ";
    int senderId = 1;
    boolean isAudio = false;
    List<User> users = new ArrayList<>();
    User user = new User("hrisko");
    user.setOnline(false);
    users.add(user);
    when(this.usersRepositoryMock.getAll()).thenReturn(users);

    ResponseCall actual = sut.checkIfOnline(emails, senderId, isAudio);

    System.out.println(actual.isSuccessful());
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void checkIfOnline_whenThereIsUserFoundAndHeIsOnline_shouldReturnResponseCallWithTrue() {
    List<Integer> expectedIds = new ArrayList<>();
    List<String> expectedImages = new ArrayList<>();
    List<String> expectedNames = new ArrayList<>();
    String email = "hrisko, ";
    int senderId = 1;
    boolean isAudio = false;
    List<User> users = new ArrayList<>();
    User user = new User("hrisko");
    user.setOnline(true);
    user.setId(2);
    user.setImage("");
    user.setFirstName("Hristian");
    user.setLastName("Iliev");
    users.add(user);
    expectedIds.add(2);
    expectedImages.add(user.getImage());
    expectedNames.add(user.getFirstName() + " " + user.getLastName());
    ResponseCall expected = new ResponseCall(true, expectedIds, expectedImages, expectedNames);
    when(this.usersRepositoryMock.getAll()).thenReturn(users);
    when(this.usersRepositoryMock.update(isA(User.class))).thenReturn(null);

    ResponseCall actual = sut.checkIfOnline(email, senderId, isAudio);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void noMoreCalling_whenParametersAreValid_returnTrue() {
    String email = "hrisko, ";
    int id = 1;
    List<User> users = new ArrayList<>();
    users.add(new User("hrisko"));
    when(this.usersRepositoryMock.getById(id)).thenReturn(new User());
    when(this.usersRepositoryMock.getAll()).thenReturn(users);

    boolean actual = sut.noMoreCalling(email, id);

    Assert.assertTrue(actual);
  }

  @Test
  public void createNote_whenParametersAreValidAndUserHasSuchAnote_shouldReturnTheNewNote() {
    Note expected = new Note();
    expected.setId(1);
    expected.setName("Test");
    expected.setDescription("Description");
    User user = new User();
    List<Note> notes = new ArrayList<>();
    notes.add(expected);
    user.setNotes(notes);
    Note note = new Note();
    note.setName("Test");
    note.setDescription("Description 2");
    int id = 3;
    int projectId = 1;
    when(this.usersRepositoryMock.getById(id)).thenReturn(user);
    when(this.notesRepositoryMock.update(isA(Note.class))).thenReturn(expected);

    Note actual = sut.createNote(note, id, projectId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createNote_whenParamtersAreValidAndUserHasSuchANoteButTheNewNoteIsWithEmptyDescription_shouldReturnTheDeletedNote() {
    Note expected = new Note();
    expected.setId(1);
    expected.setName("Test");
    expected.setDescription("");
    User user = new User();
    List<Note> notes = new ArrayList<>();
    notes.add(expected);
    user.setNotes(notes);
    Note note = new Note();
    note.setId(1);
    note.setName("Test");
    note.setDescription("");
    int id = 3;
    int projectId = 1;
    when(this.usersRepositoryMock.getById(id)).thenReturn(user);
    when(this.notesRepositoryMock.update(isA(Note.class))).thenReturn(expected);
    when(this.notesRepositoryMock.delete(isA(Note.class))).thenReturn(note);

    Note actual = sut.createNote(note, id, projectId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createNote_whenParametersAreValidAndUserDoesntHaveSuchANote_shouldReturnTheNewNote() {
    Note expected = new Note();
    expected.setId(1);
    expected.setName("Test");
    expected.setDescription("Description");
    User user = new User();
    List<Note> notes = new ArrayList<>();
    notes.add(expected);
    user.setNotes(notes);
    Note note = new Note();
    note.setName("Test2");
    note.setDescription("Description");
    int id = 3;
    int projectId = 3;
    when(this.usersRepositoryMock.getById(id)).thenReturn(user);
    when(this.notesRepositoryMock.create(isA(Note.class))).thenReturn(expected);

    Note actual = sut.createNote(note, id, projectId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getNotes_whenIdIsValid_shouldReturnListOfNotes() {
    List<Note> expected = new ArrayList<>();
    Note note = new Note();
    note.setName("TEST");
    note.setDescription("descrip");
    expected.add(note);
    User user = new User();
    user.setId(1);
    user.setNotes(expected);
    when(this.usersRepositoryMock.getById(1)).thenReturn(user);

    List<Note> actual = sut.getNotes(1);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void deleteNote_whenNoteIdIsValid_shouldReturnTheDeletedNote() {
    int noteId = 1;
    Note note = new Note();
    note.setId(noteId);
    note.setName("TEST");
    note.setDescription("Description");
    when(this.notesRepositoryMock.getById(noteId)).thenReturn(note);
    when(this.notesRepositoryMock.delete(note)).thenReturn(note);

    Note actual = sut.deleteNote(noteId);

    Assert.assertEquals(note, actual);
  }

  @Test
  public void checkIfThereIsACall_whenIdIsValidAndNooneIsCalling_shouldReturnResponseCheckCallWith0() {
    ResponseCheckCall expected = new ResponseCheckCall(0, "", "", "", false);
    int id = 1;
    User user = new User();
    user.setId(id);
    user.setSomeoneIsCalling(0);
    user.setOnlyAudio(false);
    when(this.usersRepositoryMock.getById(id)).thenReturn(user);

    ResponseCheckCall actual = sut.checkIfThereIsACall(id);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void checkIfThereIsACall_whenIdIsValidAndSomeoneIsCalling_shouldReturnResponseCheckCall() {
    int id = 1;
    User user = new User();
    user.setId(id);
    user.setSomeoneIsCalling(2);
    user.setOnlyAudio(false);
    User caller = new User();
    caller.setImage("");
    caller.setFirstName("Hristian");
    caller.setLastName("Iliev");
    caller.setCity(new City("STZ"));
    ResponseCheckCall expected = new ResponseCheckCall(2, caller.getImage(), caller.getFirstName() + " " + caller.getLastName(), caller.getCity().getName(), false);
    when(this.usersRepositoryMock.getById(id)).thenReturn(user);
    when(this.usersRepositoryMock.getById(2)).thenReturn(caller);

    ResponseCheckCall actual = sut.checkIfThereIsACall(id);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void thereIsNoCall_whenIdIsValid_shouldReturnTrue() {
    when(this.usersRepositoryMock.getById(1)).thenReturn(new User());

    boolean actual = sut.thereIsNoCall(1);

    Assert.assertTrue(actual);
  }

  @Test
  public void changePersonalInfo_whenIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int id = 0;

    sut.changePersonalInfo(id, new PersonalInfo());
  }

  @Test
  public void changePersonalInfo_whenPersonalInfoIsNotValidAndFirstNameIsBelow2Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("PersonalInfo is not valid");
    int id = 1;
    PersonalInfo personalInfo = new PersonalInfo();
    personalInfo.setFirstName("1");
    personalInfo.setLastName("valid");
    personalInfo.setDescription("ddls;kfl;sdkvl;asdkl;vksadl;vksad;lvksl;avk;sdlkv;lsakvl;sadkvl;sadk'v;ksadvl;skav'v;lksad;lvksa;lvsadv" +
            "asdkvlasjvklsdajkvljsdaklvjaskldvjksladjvklsadjvklsajvklsajvklsadjvklsdjakvlajsklvjsaklvjsdklvjskladjvkl;sdajvas" +
            ";oajdsvkl;sdjavkldsjavklsdjavkldsajvkldasjklvjadsklvjsdklvjksldjvklsdajvklsdjvklsdjvlksdajv;aksdjvkldsjvklsajvl;kas" +
            "jkavldsjlv;ajslvksjadlk;v");

    sut.changePersonalInfo(id, personalInfo);
  }

  @Test
  public void changePersonalInfo_whenPersonalInfoIsNotValidAndFirstNameIsMOreThan16Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("PersonalInfo is not valid");
    int id = 1;
    PersonalInfo personalInfo = new PersonalInfo();
    personalInfo.setFirstName("12345678901232131");
    personalInfo.setLastName("valid");
    personalInfo.setDescription("ddls;kfl;sdkvl;asdkl;vksadl;vksad;lvksl;avk;sdlkv;lsakvl;sadkvl;sadk'v;ksadvl;skav'v;lksad;lvksa;lvsadv" +
            "asdkvlasjvklsdajkvljsdaklvjaskldvjksladjvklsadjvklsajvklsajvklsadjvklsdjakvlajsklvjsaklvjsdklvjskladjvkl;sdajvas" +
            ";oajdsvkl;sdjavkldsjavklsdjavkldsajvkldasjklvjadsklvjsdklvjksldjvklsdajvklsdjvklsdjvlksdajv;aksdjvkldsjvklsajvl;kas" +
            "jkavldsjlv;ajslvksjadlk;v");

    sut.changePersonalInfo(id, personalInfo);
  }

  @Test
  public void changePersonalInfo_whenPersonalInfoIsNotValidAndLastNameIsLessThan2Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("PersonalInfo is not valid");
    int id = 1;
    PersonalInfo personalInfo = new PersonalInfo();
    personalInfo.setFirstName("valid");
    personalInfo.setLastName("1");
    personalInfo.setDescription("ddls;kfl;sdkvl;asdkl;vksadl;vksad;lvksl;avk;sdlkv;lsakvl;sadkvl;sadk'v;ksadvl;skav'v;lksad;lvksa;lvsadv" +
            "asdkvlasjvklsdajkvljsdaklvjaskldvjksladjvklsadjvklsajvklsajvklsadjvklsdjakvlajsklvjsaklvjsdklvjskladjvkl;sdajvas" +
            ";oajdsvkl;sdjavkldsjavklsdjavkldsajvkldasjklvjadsklvjsdklvjksldjvklsdajvklsdjvklsdjvlksdajv;aksdjvkldsjvklsajvl;kas" +
            "jkavldsjlv;ajslvksjadlk;v");

    sut.changePersonalInfo(id, personalInfo);
  }

  @Test
  public void changePersonalInfo_whenPersonalInfoIsNotValidAndLastNameIsMoreThan16Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("PersonalInfo is not valid");
    int id = 1;
    PersonalInfo personalInfo = new PersonalInfo();
    personalInfo.setFirstName("valid");
    personalInfo.setLastName("1234567891011121314");
    personalInfo.setDescription("ddls;kfl;sdkvl;asdkl;vksadl;vksad;lvksl;avk;sdlkv;lsakvl;sadkvl;sadk'v;ksadvl;skav'v;lksad;lvksa;lvsadv" +
            "asdkvlasjvklsdajkvljsdaklvjaskldvjksladjvklsadjvklsajvklsajvklsadjvklsdjakvlajsklvjsaklvjsdklvjskladjvkl;sdajvas" +
            ";oajdsvkl;sdjavkldsjavklsdjavkldsajvkldasjklvjadsklvjsdklvjksldjvklsdajvklsdjvklsdjvlksdajv;aksdjvkldsjvklsajvl;kas" +
            "jkavldsjlv;ajslvksjadlk;v");

    sut.changePersonalInfo(id, personalInfo);
  }

  @Test
  public void changePersonalInfo_whenPersonalInfoIsNotValidAndDescriptionIsLessThan160Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("PersonalInfo is not valid");
    int id = 1;
    PersonalInfo personalInfo = new PersonalInfo();
    personalInfo.setFirstName("valid");
    personalInfo.setLastName("valid");
    personalInfo.setDescription("invalid");

    sut.changePersonalInfo(id, personalInfo);
  }

  @Test
  public void changePersonalInfo_whenParametersAreValid_shouldReturnTrue() {
    boolean expected = true;
    int id = 1;
    PersonalInfo personalInfo = new PersonalInfo();
    personalInfo.setFirstName("valid");
    personalInfo.setLastName("valid");
    personalInfo.setDescription("ddls;kfl;sdkvl;asdkl;vksadl;vksad;lvksl;avk;sdlkv;lsakvl;sadkvl;sadk'v;ksadvl;skav'v;lksad;lvksa;lvsadv" +
            "asdkvlasjvklsdajkvljsdaklvjaskldvjksladjvklsadjvklsajvklsajvklsadjvklsdjakvlajsklvjsaklvjsdklvjskladjvkl;sdajvas" +
            ";oajdsvkl;sdjavkldsjavklsdjavkldsajvkldasjklvjadsklvjsdklvjksldjvklsdajvklsdjvklsdjvlksdajv;aksdjvkldsjvklsajvl;kas" +
            "jkavldsjlv;ajslvksjadlk;v");
    when(this.usersRepositoryMock.getById(isA(Integer.class))).thenReturn(new User());

    boolean actual = sut.changePersonalInfo(id, personalInfo);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void changeImage_whenIdIsNotValid_shouldThrowInvalidParameterException() throws IOException {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int id = 0;
    RequestBase64 base64 = new RequestBase64("TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlz\n" +
            "IHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2Yg\n" +
            "dGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGlu\n" +
            "dWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRo\n" +
            "ZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4=", "jpg");

    sut.changeImage(id, base64);
  }

  @Test
  public void changeImage_whenParametersAreValid_shouldReturnTrue() throws IOException {
    int id = 1;
    RequestBase64 base64 = new RequestBase64("TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlz\n" +
            "IHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2Yg\n" +
            "dGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGlu\n" +
            "dWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRo\n" +
            "ZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4=", "jpg");
    User user = new User();
    user.setImage("test");
    when(this.usersRepositoryMock.getById(isA(Integer.class))).thenReturn(user);

    boolean actual = sut.changeImage(id, base64);

    Assert.assertTrue(actual);
  }

  @Test
  public void deleteNotifications_whenIdIsNotValid_shouldThrowInvalidParameterException(){
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int id = 0;

    sut.deleteNotifications(id);
  }

  @Test
  public void deleteNotifications_whenParametersAreValid_shouldReturnResponseSuccessfulWithTrue(){
    ResponseSuccessful expected = new ResponseSuccessful(true);
    int id = 1;
    User user = new User();
    List<Notification> notifications = new ArrayList<>();
    notifications.add(new Notification());
    user.setNotifications(notifications);
    when(this.usersRepositoryMock.getById(isA(Integer.class))).thenReturn(user);

    ResponseSuccessful actual = sut.deleteNotifications(id);

    Assert.assertEquals(expected, actual);
  }
}
