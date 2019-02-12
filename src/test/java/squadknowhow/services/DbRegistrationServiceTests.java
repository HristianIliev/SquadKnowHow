package squadknowhow.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.*;
import squadknowhow.request.models.EditedUser;
import squadknowhow.response.models.ResponseRegister;
import squadknowhow.response.models.ResponseUpload;
import squadknowhow.utils.validators.*;
import squadknowhow.utils.validators.base.IValidator;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

public class DbRegistrationServiceTests {
  @Mock
  private IRepository<User> usersRepositoryMock;
  @Mock
  private IRepository<City> citiesRepositoryMock;
  @Mock
  private IRepository<Skill> skillsRepositoryMock;
  @Mock
  private IRepository<Interest> interestsRepositoryMock;
  @Mock
  private IRepository<UserCategory> userCategoriesRepositoryMock;
  @Mock
  private IRepository<Company> companiesRepositoryMock;
  @Mock
  private IRepository<Language> languagesRepositoryMock;

  private IValidator<Integer> idValidator;
  private IValidator<User> userValidator;
  private IValidator<EditedUser> editedUserValidator;
  private PasswordEncoder passwordEncoder;
  private DbRegistrationService sut;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Before
  public void setUpValidators() {
    this.idValidator = new IdValidator();
    this.userValidator = new UserValidator(new PasswordValidator(),
            new EmailValidator());
    this.editedUserValidator = new EditedUserValidator(new PasswordValidator(),
            new EmailValidator());
    this.passwordEncoder = new BCryptPasswordEncoder();
    this.sut = new DbRegistrationService(this.usersRepositoryMock,
            this.citiesRepositoryMock,
            this.skillsRepositoryMock,
            this.interestsRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.companiesRepositoryMock,
            this.idValidator,
            this.userValidator,
            this.passwordEncoder,
            this.languagesRepositoryMock,
            this.editedUserValidator);
  }

  @Test
  public void uploadImage_whenIdIsLessThan1_shouldThrowInvalidParameterException() throws IOException {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");

    sut.uploadImage(null, 0);
  }

  @Test
  public void uploadImage_whenParametersAreValid_shouldReturnResponseUploadTrue() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    when(this.usersRepositoryMock.getById(id)).thenReturn(new User());

    ResponseUpload actual = sut.uploadImage(null, id);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void isEmailFree_wheUserWithThatEmailHasBeenFound_shouldReturnFalse() {
    String email = "email";
    List<User> users = new ArrayList<>();
    users.add(new User(email));
    when(this.usersRepositoryMock.getAll()).thenReturn(users);

    boolean actual = sut.isEmailFree(email);

    Assert.assertFalse(actual);
  }

  @Test
  public void isEmailFree_wheUserWithThatEmailHasNotBeenFound_shouldReturnTrue() {
    String email = "email";
    List<User> users = new ArrayList<>();
    users.add(new User(email + "d"));
    when(this.usersRepositoryMock.getAll()).thenReturn(users);

    boolean actual = sut.isEmailFree(email);

    Assert.assertTrue(actual);
  }

  @Test
  public void attemptRegister_whenUserIsNull_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewUser is not valid");
    User newUser = null;

    sut.attemptRegister(newUser);
  }

  @Test
  public void attemptRegister_whenUserFirstNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewUser is not valid");
    User newUser = new User(1, "");

    sut.attemptRegister(newUser);
  }

  @Test
  public void attemptRegister_whenUserFirstNameIsLessThan5Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewUser is not valid");
    User newUser = new User(1, "dsa");

    sut.attemptRegister(newUser);
  }

  @Test
  public void attemptRegister_whenUserFirstNameIsMoreThan16Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewUser is not valid");
    User newUser = new User(1, "dsdsadasfsfsdafasdfsafasfsaa");

    sut.attemptRegister(newUser);
  }

  @Test
  public void attemptRegister_whenUserLastNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewUser is not valid");
    User newUser = new User("firstName", "", 1);

    sut.attemptRegister(newUser);
  }

  @Test
  public void attemptRegister_whenUserLastNameIsLessThan5Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewUser is not valid");
    User newUser = new User("firstName", "1234", 1);

    sut.attemptRegister(newUser);
  }

  @Test
  public void attemptRegister_whenUserLastNameIsMoreThan16Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewUser is not valid");
    User newUser = new User("valid", "dsklafmsdalkcmksadlcmklasdkmdc", 1);

    sut.attemptRegister(newUser);
  }

  @Test
  public void attemptRegister_whenUserDescriptionIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewUser is not valid");
    User newUser = new User("firstName", "lastName", "", "email@da", "1234567890");

    sut.attemptRegister(newUser);
  }

  @Test
  public void attemptRegister_whenUserDescriptionIsLessThan25Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewUser is not valid");
    User newUser = new User("firstName", "lastName", "dsada", "email@da", "1234567890");

    sut.attemptRegister(newUser);
  }

  @Test
  public void attemptRegister_whenParametersAreValid_shouldReturnResponseRegisterWithTheIdOfTheInsertedUser() {
    int expectedId = 1;
    ResponseRegister expected = new ResponseRegister(expectedId);
    List<User> users = new ArrayList<>();
    User newUser = new User("firstName", "lastName", "dsadaadsadacasdacasdcdasdcaskcmklasdmklcmasldmkcmalsdmaskldmcmkaskmcsdkmalckma", "email@da", "1234567890");
    newUser.setCity(new City());
    newUser.setEmployer(new Company());
    newUser.setSkills(new ArrayList<>());
    newUser.setInterests(new ArrayList<>());
    newUser.setLanguages(new ArrayList<>());
    newUser.setEmail("hristian00i@abv.bg");
    users.add(newUser);
    when(this.usersRepositoryMock.getAll()).thenAnswer(invocation -> users);
    when(this.usersRepositoryMock.create(isA(User.class))).thenAnswer(invocation -> {
      User entity = invocation.getArgument(0);
      entity.setId(expectedId);
      return null;
    });

    ResponseRegister actual = sut.attemptRegister(newUser);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void attemptRegister_whenParametersAreValid_shouldReturnResponseRegisterWithTheIdOfTheInsertedUserAndSetCityEmployerSkillsInterestsAndPreviousEmploymentAndLanguages() {
    int expectedId = 1;
    ResponseRegister expected = new ResponseRegister(expectedId);
    User newUser = new User("firstName", "lastName", "dsadaadsadacasdacasdcdasdcaskcmklasdmklcmasldmkcmalsdmaskldmcmkaskmcsdkmalckma", "email@da", "1234567890");
    newUser.setCity(new City("name"));
    newUser.setEmployer(new Company("name"));
    List<Skill> userSkills = new ArrayList<>();
    userSkills.add(new Skill("name"));
    newUser.setSkills(userSkills);
    List<Interest> userInterests = new ArrayList<>();
    userInterests.add(new Interest("name"));
    newUser.setInterests(userInterests);
    List<Company> userPreviousEmployment = new ArrayList<>();
    userPreviousEmployment.add(new Company("name"));
    newUser.setPreviousEmployment(userPreviousEmployment);
    List<Language> langauges = new ArrayList<>();
    langauges.add(new Language("name"));
    newUser.setLanguages(langauges);
    List<User> users = new ArrayList<>();
    newUser.setEmail("hristian00i@abv.bg");
    users.add(newUser);
    when(this.usersRepositoryMock.getAll()).thenAnswer(invocation -> users);
    when(this.usersRepositoryMock.create(isA(User.class))).thenAnswer(invocation -> {
      User entity = invocation.getArgument(0);
      entity.setId(expectedId);
      return null;
    });

    ResponseRegister actual = sut.attemptRegister(newUser);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void forgotPassword_whenCalled_shouldReturnTrue() {
    String email = "test";
    User user = new User();
    user.setEmail(email);
    List<User> users = new ArrayList<>();
    users.add(user);
    when(this.usersRepositoryMock.getAll()).thenReturn(users);

    boolean actual = sut.forgotPassword(email);

    Assert.assertTrue(actual);
  }

  @Test
  public void resetPassword_whenCalled_shouldReturnTrue() {
    User user = new User();
    user.setPassword("oldPass");
    user.setId(1);
    when(this.usersRepositoryMock.getById(1)).thenReturn(user);

    boolean actual = sut.resetPassword("new password", 1);

    Assert.assertTrue(actual);
  }

  @Test
  public void resetPassword_whenCalled_shouldChangeUserPassword() {
    String expected = "newPass";
    int id = 1;
    User user = new User();
    user.setPassword(this.passwordEncoder.encode("oldPass"));
    user.setId(1);
    when(this.usersRepositoryMock.getById(isA(Integer.class))).thenAnswer(invocation -> {
      return user;
    });
    when(this.usersRepositoryMock.update(isA(User.class))).thenAnswer(invocation -> {
      return invocation.getArgument(0);
    });

    sut.resetPassword(expected, id);

    // Should be different because of the encodings
    Assert.assertNotEquals(expected, user.getPassword());
  }

  @Test
  public void editProfile_whenUserIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("User id is not valid");
    EditedUser user = new EditedUser(0);
    sut.editProfile(user);
  }

  @Test
  public void editProfile_whenFirstNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("User is not valid");
    EditedUser user = new EditedUser(1, "");
    sut.editProfile(user);
  }

  @Test
  public void editProfile_whenFirstNameIsLessThan2Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("User is not valid");
    EditedUser user = new EditedUser(1, "1");

    sut.editProfile(user);
  }

  @Test
  public void editProfile_whenFirstNameIsMoreThan16Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("User is not valid");
    EditedUser user = new EditedUser(1, "123456675487657437346757567");

    sut.editProfile(user);
  }

  @Test
  public void editProfile_whenLastNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("User is not valid");
    EditedUser user = new EditedUser(1, "Valid firstName", "");

    sut.editProfile(user);
  }

  @Test
  public void editProfile_whenLastNameIsLessThan2Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("User is not valid");
    EditedUser user = new EditedUser(1, "Valid firstName", "1");

    sut.editProfile(user);
  }

  @Test
  public void editProfile_whenLastNameIsMoreThan16Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("User is not valid");
    EditedUser user = new EditedUser(1, "Valid firstName", "1287348912734871238047128904780");

    sut.editProfile(user);
  }

  @Test
  public void editProfile_whenDescriptionIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("User is not valid");
    EditedUser user = new EditedUser(1, "Valid firstName", "Valid lastName", "");

    sut.editProfile(user);
  }

  @Test
  public void editProfile_WhenDescriptionIsLessThan25Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("User is not valid");
    EditedUser user = new EditedUser(1, "Valid firstName", "ValidfirstName", "1");

    sut.editProfile(user);
  }

  @Test
  public void editProfile_whenUserIsValid_shouldReturnTrue() {
    EditedUser user = new EditedUser(1, "Valid firstName", "ValidfirstName", "Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmk", "emailtopass@abv.bg", "1234567890");
    when(this.usersRepositoryMock.getById(1)).thenReturn(new User());
    when(this.usersRepositoryMock.update(isA(User.class))).thenReturn(null);

    boolean actual = sut.editProfile(user);

    Assert.assertTrue(actual);
  }

  @Test
  public void checkActivationKey_whenCalledAndNoUserIsFound_shouldReturnFalse() {
    String activationKey = "test";
    List<User> users = new ArrayList<>();
    User user = new User();
    user.setActivationKey("nottest");
    users.add(user);
    when(this.usersRepositoryMock.getAll()).thenReturn(users);

    boolean actual = sut.checkActivationKey(activationKey);

    Assert.assertFalse(actual);
  }

  @Test
  public void checkActivationKey_whenCalledAndUserIsFoundButItIsAlreadyActivated_shouldReturnFalse() {
    String activationKey = "test";
    List<User> users = new ArrayList<>();
    User user = new User();
    user.setActivationKey("test");
    user.setActivated(true);
    users.add(user);
    when(this.usersRepositoryMock.getAll()).thenReturn(users);

    boolean actual = sut.checkActivationKey(activationKey);

    Assert.assertFalse(actual);
  }

  @Test
  public void checkActivationKey_whenCalledAndUserIsFoundAndItIsNotAlreadyActivated_shouldReturnTrue() {
    String activationKey = "test";
    List<User> users = new ArrayList<>();
    User user = new User();
    user.setActivationKey("test");
    user.setActivated(false);
    users.add(user);
    when(this.usersRepositoryMock.getAll()).thenReturn(users);

    boolean actual = sut.checkActivationKey(activationKey);

    Assert.assertTrue(actual);
  }

  @Test
  public void areTagsCorrect_whenCityIsInvalid_shouldReturnTrue() {
    User tags = new User();
    List<City> cities = new ArrayList<>(Collections.singletonList(new City("test city")));
    tags.setCity(new City("test city1"));
    List<Company> companies = new ArrayList<>(Arrays.asList(new Company(0, "test company"),
            new Company(1, "test company 1"),
            new Company(2, "test company 2")));
    tags.setEmployer(new Company("test company"));
    List<Skill> skills = new ArrayList<>(Arrays.asList(new Skill(1, "test skill 1"),
            new Skill(2, "test skill 2")));
    tags.setSkills(skills);
    List<Interest> interests = new ArrayList<>(Arrays.asList(new Interest(1, "test interest 1"),
            new Interest(2, "test interest 2")));
    tags.setInterests(interests);
    List<Language> languages = new ArrayList<>(Arrays.asList(new Language(1, "test language 1"),
            new Language(2, "test language 2")));
    tags.setLanguages(languages);
    tags.setPreviousEmployment(new ArrayList<>(Arrays.asList(new Company(1, "test company 1"),
            new Company(2, "test company 2"))));
    when(this.citiesRepositoryMock.getAll()).thenReturn(cities);
    when(this.skillsRepositoryMock.getAll()).thenReturn(skills);
    when(this.companiesRepositoryMock.getAll()).thenReturn(companies);
    when(this.interestsRepositoryMock.getAll()).thenReturn(interests);
    when(this.languagesRepositoryMock.getAll()).thenReturn(languages);

    boolean actual = sut.areTagsCorrect(tags);

    Assert.assertFalse(actual);
  }

  @Test
  public void areTagsCorrect_whenEmployerIsNotValid_shouldReturnTrue() {
    User tags = new User();
    List<City> cities = new ArrayList<>(Collections.singletonList(new City("test city")));
    tags.setCity(new City("test city"));
    List<Company> companies = new ArrayList<>(Arrays.asList(new Company(0, "test company"),
            new Company(1, "test company 1"),
            new Company(2, "test company 2")));
    tags.setEmployer(new Company("test company3"));
    List<Skill> skills = new ArrayList<>(Arrays.asList(new Skill(1, "test skill 1"),
            new Skill(2, "test skill 2")));
    tags.setSkills(skills);
    List<Interest> interests = new ArrayList<>(Arrays.asList(new Interest(1, "test interest 1"),
            new Interest(2, "test interest 2")));
    tags.setInterests(interests);
    List<Language> languages = new ArrayList<>(Arrays.asList(new Language(1, "test language 1"),
            new Language(2, "test language 2")));
    tags.setLanguages(languages);
    tags.setPreviousEmployment(new ArrayList<>(Arrays.asList(new Company(1, "test company 1"),
            new Company(2, "test company 2"))));
    when(this.citiesRepositoryMock.getAll()).thenReturn(cities);
    when(this.skillsRepositoryMock.getAll()).thenReturn(skills);
    when(this.companiesRepositoryMock.getAll()).thenReturn(companies);
    when(this.interestsRepositoryMock.getAll()).thenReturn(interests);
    when(this.languagesRepositoryMock.getAll()).thenReturn(languages);

    boolean actual = sut.areTagsCorrect(tags);

    Assert.assertFalse(actual);
  }

  @Test
  public void areTagsCorrect_whenASkillIsNotCorrect_shouldReturnTrue() {
    User tags = new User();
    List<City> cities = new ArrayList<>(Collections.singletonList(new City("test city")));
    tags.setCity(new City("test city"));
    List<Company> companies = new ArrayList<>(Arrays.asList(new Company(0, "test company"),
            new Company(1, "test company 1"),
            new Company(2, "test company 2")));
    tags.setEmployer(new Company("test company"));
    List<Skill> skills = new ArrayList<>(Arrays.asList(new Skill(1, "test skill 1"),
            new Skill(2, "test skill 2")));
    tags.setSkills(new ArrayList<>(Arrays.asList(new Skill(1, "test skill 1"),
            new Skill(2, "test skill 3"))));
    List<Interest> interests = new ArrayList<>(Arrays.asList(new Interest(1, "test interest 1"),
            new Interest(2, "test interest 2")));
    tags.setInterests(interests);
    List<Language> languages = new ArrayList<>(Arrays.asList(new Language(1, "test language 1"),
            new Language(2, "test language 2")));
    tags.setLanguages(languages);
    tags.setPreviousEmployment(new ArrayList<>(Arrays.asList(new Company(1, "test company 1"),
            new Company(2, "test company 2"))));
    when(this.citiesRepositoryMock.getAll()).thenReturn(cities);
    when(this.skillsRepositoryMock.getAll()).thenReturn(skills);
    when(this.companiesRepositoryMock.getAll()).thenReturn(companies);
    when(this.interestsRepositoryMock.getAll()).thenReturn(interests);
    when(this.languagesRepositoryMock.getAll()).thenReturn(languages);

    boolean actual = sut.areTagsCorrect(tags);

    Assert.assertFalse(actual);
  }

  @Test
  public void areTagsCorrect_whenSkillsAreSame_shouldReturnTrue() {
    User tags = new User();
    List<City> cities = new ArrayList<>(Collections.singletonList(new City("test city")));
    tags.setCity(new City("test city"));
    List<Company> companies = new ArrayList<>(Arrays.asList(new Company(0, "test company"),
            new Company(1, "test company 1"),
            new Company(2, "test company 2")));
    tags.setEmployer(new Company("test company"));
    List<Skill> skills = new ArrayList<>(Arrays.asList(new Skill(2, "test skill 1"),
            new Skill(2, "test skill 2")));
    tags.setSkills(new ArrayList<>(Arrays.asList(new Skill(2, "test skill 2"),
            new Skill(2, "test skill 2"))));
    List<Interest> interests = new ArrayList<>(Arrays.asList(new Interest(1, "test interest 1"),
            new Interest(2, "test interest 2")));
    tags.setInterests(interests);
    List<Language> languages = new ArrayList<>(Arrays.asList(new Language(1, "test language 1"),
            new Language(2, "test language 2")));
    tags.setLanguages(languages);
    tags.setPreviousEmployment(new ArrayList<>(Arrays.asList(new Company(1, "test company 1"),
            new Company(2, "test company 2"))));
    when(this.citiesRepositoryMock.getAll()).thenReturn(cities);
    when(this.skillsRepositoryMock.getAll()).thenReturn(skills);
    when(this.companiesRepositoryMock.getAll()).thenReturn(companies);
    when(this.interestsRepositoryMock.getAll()).thenReturn(interests);
    when(this.languagesRepositoryMock.getAll()).thenReturn(languages);

    boolean actual = sut.areTagsCorrect(tags);

    Assert.assertFalse(actual);
  }

  @Test
  public void areTagsCorrect_whenAnInterestIsNotCorrect_shouldReturnTrue() {
    User tags = new User();
    List<City> cities = new ArrayList<>(Collections.singletonList(new City("test city")));
    tags.setCity(new City("test city"));
    List<Company> companies = new ArrayList<>(Arrays.asList(new Company(0, "test company"),
            new Company(1, "test company 1"),
            new Company(2, "test company 2")));
    tags.setEmployer(new Company("test company"));
    List<Skill> skills = new ArrayList<>(Arrays.asList(new Skill(1, "test skill 1"),
            new Skill(2, "test skill 2")));
    tags.setSkills(skills);
    List<Interest> interests = new ArrayList<>(Arrays.asList(new Interest(1, "test interest 1"),
            new Interest(2, "test interest 2")));
    tags.setInterests(new ArrayList<>(Arrays.asList(new Interest(1, "test interest 1"),
            new Interest(2, "test interest 3"))));
    List<Language> languages = new ArrayList<>(Arrays.asList(new Language(1, "test language 1"),
            new Language(2, "test language 2")));
    tags.setLanguages(languages);
    tags.setPreviousEmployment(new ArrayList<>(Arrays.asList(new Company(1, "test company 1"),
            new Company(2, "test company 2"))));
    when(this.citiesRepositoryMock.getAll()).thenReturn(cities);
    when(this.skillsRepositoryMock.getAll()).thenReturn(skills);
    when(this.companiesRepositoryMock.getAll()).thenReturn(companies);
    when(this.interestsRepositoryMock.getAll()).thenReturn(interests);
    when(this.languagesRepositoryMock.getAll()).thenReturn(languages);

    boolean actual = sut.areTagsCorrect(tags);

    Assert.assertFalse(actual);
  }

  @Test
  public void areTagsCorrect_whenInterestsAreSame_shouldReturnTrue() {
    User tags = new User();
    List<City> cities = new ArrayList<>(Collections.singletonList(new City("test city")));
    tags.setCity(new City("test city"));
    List<Company> companies = new ArrayList<>(Arrays.asList(new Company(0, "test company"),
            new Company(1, "test company 1"),
            new Company(2, "test company 2")));
    tags.setEmployer(new Company("test company"));
    List<Skill> skills = new ArrayList<>(Arrays.asList(new Skill(1, "test skill 1"),
            new Skill(2, "test skill 2")));
    tags.setSkills(skills);
    List<Interest> interests = new ArrayList<>(Arrays.asList(new Interest(2, "test interest 1"),
            new Interest(2, "test interest 2")));
    tags.setInterests(new ArrayList<>(Arrays.asList(new Interest(2, "test interest 2"),
            new Interest(2, "test interest 2"))));
    List<Language> languages = new ArrayList<>(Arrays.asList(new Language(1, "test language 1"),
            new Language(2, "test language 2")));
    tags.setLanguages(languages);
    tags.setPreviousEmployment(new ArrayList<>(Arrays.asList(new Company(1, "test company 1"),
            new Company(2, "test company 2"))));
    when(this.citiesRepositoryMock.getAll()).thenReturn(cities);
    when(this.skillsRepositoryMock.getAll()).thenReturn(skills);
    when(this.companiesRepositoryMock.getAll()).thenReturn(companies);
    when(this.interestsRepositoryMock.getAll()).thenReturn(interests);
    when(this.languagesRepositoryMock.getAll()).thenReturn(languages);

    boolean actual = sut.areTagsCorrect(tags);

    Assert.assertFalse(actual);
  }

  @Test
  public void areTagsCorrect_whenALanguageIsNotCorrect_shouldReturnTrue() {
    User tags = new User();
    List<City> cities = new ArrayList<>(Collections.singletonList(new City("test city")));
    tags.setCity(new City("test city"));
    List<Company> companies = new ArrayList<>(Arrays.asList(new Company(0, "test company"),
            new Company(1, "test company 1"),
            new Company(2, "test company 2")));
    tags.setEmployer(new Company("test company"));
    List<Skill> skills = new ArrayList<>(Arrays.asList(new Skill(1, "test skill 1"),
            new Skill(2, "test skill 2")));
    tags.setSkills(skills);
    List<Interest> interests = new ArrayList<>(Arrays.asList(new Interest(1, "test interest 1"),
            new Interest(2, "test interest 2")));
    tags.setInterests(interests);
    List<Language> languages = new ArrayList<>(Arrays.asList(new Language(1, "test language 1"),
            new Language(2, "test language 2")));
    tags.setLanguages(new ArrayList<>(Arrays.asList(new Language(1, "test language 1"),
            new Language(2, "test language 3"))));
    tags.setPreviousEmployment(new ArrayList<>(Arrays.asList(new Company(1, "test company 1"),
            new Company(2, "test company 2"))));
    when(this.citiesRepositoryMock.getAll()).thenReturn(cities);
    when(this.skillsRepositoryMock.getAll()).thenReturn(skills);
    when(this.companiesRepositoryMock.getAll()).thenReturn(companies);
    when(this.interestsRepositoryMock.getAll()).thenReturn(interests);
    when(this.languagesRepositoryMock.getAll()).thenReturn(languages);

    boolean actual = sut.areTagsCorrect(tags);

    Assert.assertFalse(actual);
  }

  @Test
  public void areTagsCorrect_whenLanguagesAreSame_shouldReturnTrue() {
    User tags = new User();
    List<City> cities = new ArrayList<>(Collections.singletonList(new City("test city")));
    tags.setCity(new City("test city"));
    List<Company> companies = new ArrayList<>(Arrays.asList(new Company(0, "test company"),
            new Company(1, "test company 1"),
            new Company(2, "test company 2")));
    tags.setEmployer(new Company("test company"));
    List<Skill> skills = new ArrayList<>(Arrays.asList(new Skill(1, "test skill 1"),
            new Skill(2, "test skill 2")));
    tags.setSkills(skills);
    List<Interest> interests = new ArrayList<>(Arrays.asList(new Interest(1, "test interest 1"),
            new Interest(2, "test interest 2")));
    tags.setInterests(interests);
    List<Language> languages = new ArrayList<>(Arrays.asList(new Language(2, "test language 1"),
            new Language(2, "test language 2")));
    tags.setLanguages(new ArrayList<>(Arrays.asList(new Language(2, "test language 1"),
            new Language(2, "test language 2"))));
    tags.setPreviousEmployment(new ArrayList<>(Arrays.asList(new Company(1, "test company 1"),
            new Company(2, "test company 2"))));
    when(this.citiesRepositoryMock.getAll()).thenReturn(cities);
    when(this.skillsRepositoryMock.getAll()).thenReturn(skills);
    when(this.companiesRepositoryMock.getAll()).thenReturn(companies);
    when(this.interestsRepositoryMock.getAll()).thenReturn(interests);
    when(this.languagesRepositoryMock.getAll()).thenReturn(languages);

    boolean actual = sut.areTagsCorrect(tags);

    Assert.assertFalse(actual);
  }

  @Test
  public void areTagsCorrect_whenACompanyIsNotCorrect_shouldReturnTrue() {
    User tags = new User();
    List<City> cities = new ArrayList<>(Collections.singletonList(new City("test city")));
    tags.setCity(new City("test city"));
    List<Company> companies = new ArrayList<>(Arrays.asList(new Company(0, "test company"),
            new Company(1, "test company 1"),
            new Company(2, "test company 2")));
    tags.setEmployer(new Company("test company"));
    List<Skill> skills = new ArrayList<>(Arrays.asList(new Skill(1, "test skill 1"),
            new Skill(2, "test skill 2")));
    tags.setSkills(skills);
    List<Interest> interests = new ArrayList<>(Arrays.asList(new Interest(1, "test interest 1"),
            new Interest(2, "test interest 2")));
    tags.setInterests(interests);
    List<Language> languages = new ArrayList<>(Arrays.asList(new Language(1, "test language 1"),
            new Language(2, "test language 2")));
    tags.setLanguages(new ArrayList<>(Arrays.asList(new Language(1, "test language 1"),
            new Language(2, "test language 2"))));
    tags.setPreviousEmployment(new ArrayList<>(Arrays.asList(new Company(1, "test company 1"),
            new Company(2, "test company 3"))));
    when(this.citiesRepositoryMock.getAll()).thenReturn(cities);
    when(this.skillsRepositoryMock.getAll()).thenReturn(skills);
    when(this.companiesRepositoryMock.getAll()).thenReturn(companies);
    when(this.interestsRepositoryMock.getAll()).thenReturn(interests);
    when(this.languagesRepositoryMock.getAll()).thenReturn(languages);

    boolean actual = sut.areTagsCorrect(tags);

    Assert.assertFalse(actual);
  }

  @Test
  public void areTagsCorrect_whenCompaniesAreSame_shouldReturnTrue() {
    User tags = new User();
    List<City> cities = new ArrayList<>(Collections.singletonList(new City("test city")));
    tags.setCity(new City("test city"));
    List<Company> companies = new ArrayList<>(Arrays.asList(new Company(0, "test company"),
            new Company(2, "test company 1"),
            new Company(2, "test company 2")));
    tags.setEmployer(new Company("test company"));
    List<Skill> skills = new ArrayList<>(Arrays.asList(new Skill(1, "test skill 1"),
            new Skill(2, "test skill 2")));
    tags.setSkills(skills);
    List<Interest> interests = new ArrayList<>(Arrays.asList(new Interest(1, "test interest 1"),
            new Interest(2, "test interest 2")));
    tags.setInterests(interests);
    List<Language> languages = new ArrayList<>(Arrays.asList(new Language(1, "test language 1"),
            new Language(2, "test language 2")));
    tags.setLanguages(languages);
    tags.setPreviousEmployment(new ArrayList<>(Arrays.asList(new Company(1, "test company 1"),
            new Company(2, "test company 2"))));
    when(this.citiesRepositoryMock.getAll()).thenReturn(cities);
    when(this.skillsRepositoryMock.getAll()).thenReturn(skills);
    when(this.companiesRepositoryMock.getAll()).thenReturn(companies);
    when(this.interestsRepositoryMock.getAll()).thenReturn(interests);
    when(this.languagesRepositoryMock.getAll()).thenReturn(languages);

    boolean actual = sut.areTagsCorrect(tags);

    Assert.assertFalse(actual);
  }

  @Test
  public void areTagsCorrect_whenAllTagsAreCorrect_shouldReturnTrue() {
    User tags = new User();
    List<City> cities = new ArrayList<>(Collections.singletonList(new City("test city")));
    tags.setCity(new City("test city"));
    List<Company> companies = new ArrayList<>(Arrays.asList(new Company(0, "test company"),
            new Company(1, "test company 1"),
            new Company(2, "test company 2")));
    tags.setEmployer(new Company("test company"));
    List<Skill> skills = new ArrayList<>(Arrays.asList(new Skill(1, "test skill 1"),
            new Skill(2, "test skill 2")));
    tags.setSkills(skills);
    List<Interest> interests = new ArrayList<>(Arrays.asList(new Interest(1, "test interest 1"),
            new Interest(2, "test interest 2")));
    tags.setInterests(interests);
    List<Language> languages = new ArrayList<>(Arrays.asList(new Language(1, "test language 1"),
            new Language(2, "test language 2")));
    tags.setLanguages(languages);
    tags.setPreviousEmployment(new ArrayList<>(Arrays.asList(new Company(1, "test company 1"),
            new Company(2, "test company 2"))));
    when(this.citiesRepositoryMock.getAll()).thenReturn(cities);
    when(this.skillsRepositoryMock.getAll()).thenReturn(skills);
    when(this.companiesRepositoryMock.getAll()).thenReturn(companies);
    when(this.interestsRepositoryMock.getAll()).thenReturn(interests);
    when(this.languagesRepositoryMock.getAll()).thenReturn(languages);

    boolean actual = sut.areTagsCorrect(tags);

    Assert.assertTrue(actual);
  }

  @Test
  public void changeOtherInfo_whenIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int id = 0;

    sut.changeOtherInfo(id, new User());
  }

  @Test
  public void changeOtherInfo_whenParametersAreValid_shouldReturnTrueAndCHnageTheUser() {
    int id = 1;
    User newInfo = new User();
    newInfo.setCity(new City("test city"));
    newInfo.setDegree("3");
    newInfo.setPersonalSite("test site");
    newInfo.setEmployer(new Company("test employer"));
    newInfo.setUnemployed(false);
    newInfo.setSkills(new ArrayList<>(Collections.singletonList(new Skill("test skills"))));
    newInfo.setInterests(new ArrayList<>(Collections.singletonList(new Interest("test interest"))));
    newInfo.setLanguages(new ArrayList<>(Collections.singletonList(new Language("test language"))));
    newInfo.setPreviousEmployment(new ArrayList<>(Collections.singletonList(new Company("test company"))));
    when(this.citiesRepositoryMock.getAll()).thenReturn(new ArrayList<>(Arrays.asList(new City("test city"))));
    when(this.companiesRepositoryMock.getAll()).thenReturn(new ArrayList<>(Arrays.asList(new Company("test employer"),
            new Company("test company"))));
    when(this.skillsRepositoryMock.getAll()).thenReturn(new ArrayList<>(Collections.singletonList(new Skill("test skills"))));
    when(this.interestsRepositoryMock.getAll()).thenReturn(new ArrayList<>(Collections.singletonList(new Interest("test interest"))));
    when(this.languagesRepositoryMock.getAll()).thenReturn(new ArrayList<>(Collections.singletonList(new Language("test language"))));
    when(this.usersRepositoryMock.getById(id)).thenReturn(new User());

    boolean actual = sut.changeOtherInfo(id, newInfo);

    Assert.assertTrue(actual);
  }

  @Test
  public void changeOtherInfo_whenParametersAreValidAndValuesAreNull_shouldReturnTrueAndCHnageTheUser() {
    int id = 1;
    User newInfo = new User();
    newInfo.setCity(new City(""));
    newInfo.setDegree("3");
    newInfo.setPersonalSite("test site");
    newInfo.setEmployer(new Company(""));
    newInfo.setUnemployed(false);
    newInfo.setSkills(new ArrayList<>());
    newInfo.setInterests(new ArrayList<>());
    newInfo.setLanguages(new ArrayList<>());
    newInfo.setPreviousEmployment(new ArrayList<>());
    when(this.citiesRepositoryMock.getAll()).thenReturn(new ArrayList<>(Arrays.asList(new City("test city"))));
    when(this.companiesRepositoryMock.getAll()).thenReturn(new ArrayList<>(Arrays.asList(new Company("test employer"),
            new Company("test company"))));
    when(this.skillsRepositoryMock.getAll()).thenReturn(new ArrayList<>(Collections.singletonList(new Skill("test skills"))));
    when(this.interestsRepositoryMock.getAll()).thenReturn(new ArrayList<>(Collections.singletonList(new Interest("test interest"))));
    when(this.languagesRepositoryMock.getAll()).thenReturn(new ArrayList<>(Collections.singletonList(new Language("test language"))));
    when(this.usersRepositoryMock.getById(id)).thenReturn(new User());

    boolean actual = sut.changeOtherInfo(id, newInfo);

    Assert.assertTrue(actual);
  }

  @Test
  public void changePassword_whenIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int id = 0;

    sut.changePassword(id, "test");
  }

  @Test
  public void changePassword_whenParametersAreCorrect_shouldReturnTrue() {
    int id = 1;
    when(this.usersRepositoryMock.getById(id)).thenReturn(new User());
    String newPass = "new password";

    boolean actual = sut.changePassword(id, newPass);

    Assert.assertTrue(actual);
  }

  @Test
  public void checkIfPassIsCorrect_whenIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int id = 0;

    sut.checkIfPassIsCorrect(id, "test");
  }

  @Test
  public void checkIfPassIsCorrect_whenParametersAreValid_shouldReturnTrue() {
    int id = 1;
    User user = new User();
    user.setPassword("$2a$10$4GzglOTLezZnAYIJxCzYmem13HPddgBWkiZ/qQa24aTjdNOM8f.a2");
    when(this.usersRepositoryMock.getById(id)).thenReturn(user);

    boolean actual = sut.checkIfPassIsCorrect(id, "test");

    Assert.assertTrue(actual);
  }
}
