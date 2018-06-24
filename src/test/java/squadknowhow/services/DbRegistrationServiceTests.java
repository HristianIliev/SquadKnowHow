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
import squadknowhow.response.models.ResponseRegister;
import squadknowhow.response.models.ResponseUpload;
import squadknowhow.utils.validators.*;
import squadknowhow.utils.validators.base.IValidator;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
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

  private IValidator<Integer> idValidator;
  private IValidator<User> userValidator;
  private PasswordEncoder passwordEncoder;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Before
  public void setUpValidators() {
    this.idValidator = new IdValidator();
    this.userValidator = new UserValidator(new PasswordValidator(),
            new EmailValidator());
    this.passwordEncoder = new BCryptPasswordEncoder();
  }

  @Test
  public void uploadImage_whenIdIsLessThan1_shouldThrowInvalidParameterException() throws IOException {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    DbRegistrationService sut = new DbRegistrationService(this.usersRepositoryMock,
            this.citiesRepositoryMock,
            this.skillsRepositoryMock,
            this.interestsRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.companiesRepositoryMock,
            this.idValidator,
            this.userValidator,
            this.passwordEncoder);

    sut.uploadImage(null, 0);
  }

  @Test
  public void uploadImage_whenParametersAreValid_shouldReturnResponseUploadTrue() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    when(this.usersRepositoryMock.getById(id)).thenReturn(new User());
    DbRegistrationService sut = new DbRegistrationService(this.usersRepositoryMock,
            this.citiesRepositoryMock,
            this.skillsRepositoryMock,
            this.interestsRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.companiesRepositoryMock,
            this.idValidator,
            this.userValidator,
            this.passwordEncoder);

    ResponseUpload actual = sut.uploadImage(null, id);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void isEmailFree_wheUserWithThatEmailHasBeenFound_shouldReturnFalse() {
    String email = "email";
    List<User> users = new ArrayList<>();
    users.add(new User(email));
    when(this.usersRepositoryMock.getAll()).thenReturn(users);
    DbRegistrationService sut = new DbRegistrationService(this.usersRepositoryMock,
            this.citiesRepositoryMock,
            this.skillsRepositoryMock,
            this.interestsRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.companiesRepositoryMock,
            this.idValidator,
            this.userValidator,
            this.passwordEncoder);

    boolean actual = sut.isEmailFree(email);

    Assert.assertFalse(actual);
  }

  @Test
  public void isEmailFree_wheUserWithThatEmailHasNotBeenFound_shouldReturnTrue() {
    String email = "email";
    List<User> users = new ArrayList<>();
    users.add(new User(email + "d"));
    when(this.usersRepositoryMock.getAll()).thenReturn(users);
    DbRegistrationService sut = new DbRegistrationService(this.usersRepositoryMock,
            this.citiesRepositoryMock,
            this.skillsRepositoryMock,
            this.interestsRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.companiesRepositoryMock,
            this.idValidator,
            this.userValidator,
            this.passwordEncoder);

    boolean actual = sut.isEmailFree(email);

    Assert.assertTrue(actual);
  }

  @Test
  public void attemptRegister_whenUserIsNull_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewUser is not valid");
    User newUser = null;
    DbRegistrationService sut = new DbRegistrationService(this.usersRepositoryMock,
            this.citiesRepositoryMock,
            this.skillsRepositoryMock,
            this.interestsRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.companiesRepositoryMock,
            this.idValidator,
            this.userValidator,
            this.passwordEncoder);

    sut.attemptRegister(newUser);
  }

  @Test
  public void attemptRegister_whenUserFirstNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewUser is not valid");
    User newUser = new User(1, "");
    DbRegistrationService sut = new DbRegistrationService(this.usersRepositoryMock,
            this.citiesRepositoryMock,
            this.skillsRepositoryMock,
            this.interestsRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.companiesRepositoryMock,
            this.idValidator,
            this.userValidator,
            this.passwordEncoder);

    sut.attemptRegister(newUser);
  }

  @Test
  public void attemptRegister_whenUserFirstNameIsLessThan5Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewUser is not valid");
    User newUser = new User(1, "dsa");
    DbRegistrationService sut = new DbRegistrationService(this.usersRepositoryMock,
            this.citiesRepositoryMock,
            this.skillsRepositoryMock,
            this.interestsRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.companiesRepositoryMock,
            this.idValidator,
            this.userValidator,
            this.passwordEncoder);

    sut.attemptRegister(newUser);
  }

  @Test
  public void attemptRegister_whenUserFirstNameIsMoreThan16Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewUser is not valid");
    User newUser = new User(1, "dsdsadasfsfsdafasdfsafasfsaa");
    DbRegistrationService sut = new DbRegistrationService(this.usersRepositoryMock,
            this.citiesRepositoryMock,
            this.skillsRepositoryMock,
            this.interestsRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.companiesRepositoryMock,
            this.idValidator,
            this.userValidator,
            this.passwordEncoder);

    sut.attemptRegister(newUser);
  }

  @Test
  public void attemptRegister_whenUserLastNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewUser is not valid");
    User newUser = new User("firstName", "", 1);
    DbRegistrationService sut = new DbRegistrationService(this.usersRepositoryMock,
            this.citiesRepositoryMock,
            this.skillsRepositoryMock,
            this.interestsRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.companiesRepositoryMock,
            this.idValidator,
            this.userValidator,
            this.passwordEncoder);

    sut.attemptRegister(newUser);
  }

  @Test
  public void attemptRegister_whenUserLastNameIsLessThan5Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewUser is not valid");
    User newUser = new User("firstName", "1234", 1);
    DbRegistrationService sut = new DbRegistrationService(this.usersRepositoryMock,
            this.citiesRepositoryMock,
            this.skillsRepositoryMock,
            this.interestsRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.companiesRepositoryMock,
            this.idValidator,
            this.userValidator,
            this.passwordEncoder);

    sut.attemptRegister(newUser);
  }

  @Test
  public void attemptRegister_whenUserLastNameIsMoreThan16Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewUser is not valid");
    User newUser = new User("valid", "dsklafmsdalkcmksadlcmklasdkmdc", 1);
    DbRegistrationService sut = new DbRegistrationService(this.usersRepositoryMock,
            this.citiesRepositoryMock,
            this.skillsRepositoryMock,
            this.interestsRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.companiesRepositoryMock,
            this.idValidator,
            this.userValidator,
            this.passwordEncoder);

    sut.attemptRegister(newUser);
  }

  @Test
  public void attemptRegister_whenUserDescriptionIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewUser is not valid");
    User newUser = new User("firstName", "lastName", "", "email@da", "1234567890");
    DbRegistrationService sut = new DbRegistrationService(this.usersRepositoryMock,
            this.citiesRepositoryMock,
            this.skillsRepositoryMock,
            this.interestsRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.companiesRepositoryMock,
            this.idValidator,
            this.userValidator,
            this.passwordEncoder);

    sut.attemptRegister(newUser);
  }

  @Test
  public void attemptRegister_whenUserDescriptionIsLessThan25Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("NewUser is not valid");
    User newUser = new User("firstName", "lastName", "dsada", "email@da", "1234567890");
    DbRegistrationService sut = new DbRegistrationService(this.usersRepositoryMock,
            this.citiesRepositoryMock,
            this.skillsRepositoryMock,
            this.interestsRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.companiesRepositoryMock,
            this.idValidator,
            this.userValidator,
            this.passwordEncoder);

    sut.attemptRegister(newUser);
  }

  @Test
  public void attemptRegister_whenParametersAreValid_shouldReturnResponseRegisterWithTheIdOfTheInsertedUser() {
    int expectedId = 1;
    ResponseRegister expected = new ResponseRegister(expectedId);
    User newUser = new User("firstName", "lastName", "dsadaadsadacasdacasdcdasdcaskcmklasdmklcmasldmkcmalsdmaskldmcmkaskmcsdkmalckma", "email@da", "1234567890");
    newUser.setCity(new City());
    newUser.setEmployer(new Company());
    newUser.setSkills(new ArrayList<>());
    newUser.setInterests(new ArrayList<>());
    this.usersRepositoryMock = new IRepository<User>() {
      @Override
      public List<User> getAll() {
        return null;
      }

      @Override
      public User getById(int id) {
        return null;
      }

      @Override
      public User create(User entity) {
        entity.setId(expectedId);
        return null;
      }

      @Override
      public User delete(User entity) {
        return null;
      }

      @Override
      public User update(User entity) {
        return null;
      }
    };
    DbRegistrationService sut = new DbRegistrationService(this.usersRepositoryMock,
            this.citiesRepositoryMock,
            this.skillsRepositoryMock,
            this.interestsRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.companiesRepositoryMock,
            this.idValidator,
            this.userValidator,
            this.passwordEncoder);

    ResponseRegister actual = sut.attemptRegister(newUser);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void attemptRegister_whenParametersAreValid_shouldReturnResponseRegisterWithTheIdOfTheInsertedUserAndSetCityEmployerSkillsInterestsAndPreviousEmployment() {
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
    this.usersRepositoryMock = new IRepository<User>() {
      @Override
      public List<User> getAll() {
        return null;
      }

      @Override
      public User getById(int id) {
        return null;
      }

      @Override
      public User create(User entity) {
        entity.setId(expectedId);
        return null;
      }

      @Override
      public User delete(User entity) {
        return null;
      }

      @Override
      public User update(User entity) {
        return null;
      }
    };
    DbRegistrationService sut = new DbRegistrationService(this.usersRepositoryMock,
            this.citiesRepositoryMock,
            this.skillsRepositoryMock,
            this.interestsRepositoryMock,
            this.userCategoriesRepositoryMock,
            this.companiesRepositoryMock,
            this.idValidator,
            this.userValidator,
            this.passwordEncoder);

    ResponseRegister actual = sut.attemptRegister(newUser);

    Assert.assertEquals(expected, actual);
  }
}
