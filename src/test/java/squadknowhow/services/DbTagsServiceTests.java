package squadknowhow.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.City;
import squadknowhow.dbmodels.Company;
import squadknowhow.dbmodels.Interest;
import squadknowhow.dbmodels.Language;
import squadknowhow.dbmodels.Skill;
import squadknowhow.dbmodels.User;
import squadknowhow.request.models.Parameter;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.when;

public class DbTagsServiceTests {
  @Mock
  private IRepository<City> citiesRepositoryMock;
  @Mock
  private IRepository<Skill> skillsRepositoryMock;
  @Mock
  private IRepository<Interest> interestsRepositoryMock;
  @Mock
  private IRepository<Company> companiesRepositoryMock;
  @Mock
  private IRepository<Language> languagesRepositoryMock;
  @Mock
  private IRepository<User> usersRepositoryMock;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  private DbTagsService sut;

  @Before
  public void setUpSUT() {
    this.sut = new DbTagsService(this.citiesRepositoryMock,
            this.skillsRepositoryMock,
            this.interestsRepositoryMock,
            this.companiesRepositoryMock,
            this.languagesRepositoryMock,
            this.usersRepositoryMock);
  }

  @Test
  public void getCities_whenTermIsLessThan2Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Term is not valid");
    String term = "1";

    sut.getCities(term);
  }

  @Test
  public void getCities_whenTermIsValidButNothingIsFound_shouldReturnHashSetWithFirstItemResultNotFound() {
    HashSet<String> expected = new HashSet<>();
    expected.add("Не съществува такъв резултат");
    String term = "valid";
    List<City> cities = new ArrayList<>();
    cities.add(new City("nope"));
    when(this.citiesRepositoryMock.getAll()).thenReturn(cities);
    HashSet<String> actual = sut.getCities(term);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getCities_whenTermIsValidAndElementsAreFound_shouldReturnHashSetWithTheCityNames() {
    HashSet<String> expected = new HashSet<>();
    expected.add("valid");
    String term = "valid";
    List<City> cities = new ArrayList<>();
    cities.add(new City("valid"));
    when(this.citiesRepositoryMock.getAll()).thenReturn(cities);

    HashSet<String> actual = sut.getCities(term);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getSkills_whenTermIsLessThan2Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Term is not valid");
    String term = "1";

    sut.getSkills(term);
  }

  @Test
  public void getSkills_whenTermIsValidButNothingIsFound_shouldReturnHashSetWithFirstItemResultNotFound() {
    HashSet<String> expected = new HashSet<>();
    expected.add("Не съществува такъв резултат");
    String term = "valid";
    List<Skill> skills = new ArrayList<>();
    skills.add(new Skill("nope"));
    when(this.skillsRepositoryMock.getAll()).thenReturn(skills);

    HashSet<String> actual = sut.getSkills(term);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getSkills_whenTermIsValidAndElementsAreFound_shouldReturnHashSetWithTheSkillNames() {
    HashSet<String> expected = new HashSet<>();
    expected.add("valid");
    String term = "valid";
    List<Skill> skills = new ArrayList<>();
    skills.add(new Skill("valid"));
    when(this.skillsRepositoryMock.getAll()).thenReturn(skills);

    HashSet<String> actual = sut.getSkills(term);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getInterests_whenTermIsLessThan2Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Term is not valid");
    String term = "1";

    sut.getInterests(term);
  }

  @Test
  public void getInterests_whenTermIsValidButNothingIsFound_shouldReturnHashSetWithFirstItemResultNotFound() {
    HashSet<String> expected = new HashSet<>();
    expected.add("Не съществува такъв резултат");
    String term = "valid";
    List<Interest> interests = new ArrayList<>();
    interests.add(new Interest("nope"));
    when(this.interestsRepositoryMock.getAll()).thenReturn(interests);

    HashSet<String> actual = sut.getInterests(term);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getInterests_whenTermIsValidAndElementsAreFound_shouldReturnHashSetWithTheInterestNames() {
    HashSet<String> expected = new HashSet<>();
    expected.add("valid");
    String term = "valid";
    List<Interest> interests = new ArrayList<>();
    interests.add(new Interest("valid"));
    when(this.interestsRepositoryMock.getAll()).thenReturn(interests);

    HashSet<String> actual = sut.getInterests(term);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getEmployedBy_whenTermIsLessThan2Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Term is not valid");
    String term = "1";

    sut.getEmployedBy(term);
  }

  @Test
  public void getEmployedBy_whenTermIsValidButNothingIsFound_shouldReturnHashSetWithFirstItemResultNotFound() {
    HashSet<String> expected = new HashSet<>();
    expected.add("Не съществува такъв резултат");
    String term = "valid";
    List<Company> companies = new ArrayList<>();
    companies.add(new Company("nope"));
    when(this.companiesRepositoryMock.getAll()).thenReturn(companies);

    HashSet<String> actual = sut.getEmployedBy(term);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getEmployedBy_whenTermIsValidAndElementsAreFound_shouldReturnHashSetWithTheCompanyNames() {
    HashSet<String> expected = new HashSet<>();
    expected.add("valid");
    String term = "valid";
    List<Company> companies = new ArrayList<>();
    companies.add(new Company("valid"));
    when(this.companiesRepositoryMock.getAll()).thenReturn(companies);

    HashSet<String> actual = sut.getEmployedBy(term);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getLanguages_whenTermIsLessThan2Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Term is not valid");
    String term = "l";

    sut.getLanguages(term);
  }

  @Test
  public void getLanguages_whenTermIsValidButNothingIsFound_shouldReturnHashSetWithFirstItemResultNotFound() {
    HashSet<String> expected = new HashSet<>();
    expected.add("Не съществува такъв резултат");
    String term = "valid";
    List<Language> languages = new ArrayList<>();
    languages.add(new Language("nope"));
    when(this.languagesRepositoryMock.getAll()).thenReturn(languages);
    HashSet<String> actual = sut.getLanguages(term);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getLanguages_whenTermIsValidAndElementsAreFound_shouldReturnHashSetWithTheCompanyNames() {
    HashSet<String> expected = new HashSet<>();
    expected.add("valid");
    String term = "valid";
    List<Language> languages = new ArrayList<>();
    languages.add(new Language("valid"));
    when(this.languagesRepositoryMock.getAll()).thenReturn(languages);

    HashSet<String> actual = sut.getLanguages(term);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getEmails_whenTermIsLessThan2Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Term is not valid");
    String term = "l";

    sut.getEmails(term);
  }

  @Test
  public void getEmails_whenTermIsValidButNothingIsFound_shouldReturnHashSetWithFirstItemResultNotFound() {
    List<String> expected = new ArrayList<>();
    expected.add("Не съществува такъв резултат");
    String term = "valid";
    when(this.usersRepositoryMock.getAll()).thenReturn(new ArrayList<>());

    List<String> actual = sut.getEmails(term);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getEmails_whenTermIsValidAndElementsAreFound_shouldReturnHashSetWithTheCompanyNames() {
    List<String> expected = new ArrayList<>();
    expected.add("valid");
    String term = "valid";
    List<User> users = new ArrayList<>();
    users.add(new User("valid"));
    when(this.usersRepositoryMock.getAll()).thenReturn(users);

    List<String> actual = sut.getEmails(term);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void checkIfTagsAreCorrect_whenAllParametersAreValid_shouldReturnTrue() {
    List<Parameter> parameters = new ArrayList<>();
    Parameter parameter = new Parameter("test",
            "skills",
            new ArrayList<>(Collections.singletonList("Skill")));
    Parameter parameter2 = new Parameter("test",
            "languages",
            new ArrayList<>(Collections.singletonList("Language")));
    Parameter parameter3 = new Parameter("test",
            "city",
            new ArrayList<>(Collections.singletonList("City")));
    parameters.add(parameter);
    parameters.add(parameter2);
    parameters.add(parameter3);
    when(this.skillsRepositoryMock.getAll())
            .thenReturn(new ArrayList<>(Collections.singletonList(new Skill("Skill"))));
    when(this.languagesRepositoryMock.getAll())
            .thenReturn(new ArrayList<>(Collections.singletonList(new Language("Language"))));
    when(this.citiesRepositoryMock.getAll())
            .thenReturn(new ArrayList<>(Collections.singletonList(new City("City"))));

    boolean actual = sut.checkIfTagsAreCorrect(parameters);

    Assert.assertTrue(actual);
  }

  @Test
  public void checkIfTagsAreCorrect_whenASkillIsNotValid_shouldReturnTrue() {
    List<Parameter> parameters = new ArrayList<>();
    Parameter parameter = new Parameter("test",
            "skills",
            new ArrayList<>(Collections.singletonList("Skill")));
    Parameter parameter2 = new Parameter("test",
            "languages",
            new ArrayList<>(Collections.singletonList("Language")));
    Parameter parameter3 = new Parameter("test",
            "city",
            new ArrayList<>(Collections.singletonList("City")));
    parameters.add(parameter);
    parameters.add(parameter2);
    parameters.add(parameter3);
    when(this.skillsRepositoryMock.getAll())
            .thenReturn(new ArrayList<>(Collections.singletonList(new Skill("Skill1"))));
    when(this.languagesRepositoryMock.getAll())
            .thenReturn(new ArrayList<>(Collections.singletonList(new Language("Language"))));
    when(this.citiesRepositoryMock.getAll())
            .thenReturn(new ArrayList<>(Collections.singletonList(new City("City"))));

    boolean actual = sut.checkIfTagsAreCorrect(parameters);

    Assert.assertFalse(actual);
  }

  @Test
  public void checkIfTagsAreCorrect_whenSkillsAreSame_shouldReturnTrue() {
    List<Parameter> parameters = new ArrayList<>();
    Parameter parameter = new Parameter("test",
            "skills",
            new ArrayList<>(Arrays.asList("Skill", "Skill")));
    Parameter parameter2 = new Parameter("test",
            "languages",
            new ArrayList<>(Collections.singletonList("Language")));
    Parameter parameter3 = new Parameter("test",
            "city",
            new ArrayList<>(Collections.singletonList("City")));
    parameters.add(parameter);
    parameters.add(parameter2);
    parameters.add(parameter3);
    when(this.skillsRepositoryMock.getAll())
            .thenReturn(new ArrayList<>(Collections.singletonList(new Skill("Skill"))));
    when(this.languagesRepositoryMock.getAll())
            .thenReturn(new ArrayList<>(Collections.singletonList(new Language("Language"))));
    when(this.citiesRepositoryMock.getAll())
            .thenReturn(new ArrayList<>(Collections.singletonList(new City("City"))));

    boolean actual = sut.checkIfTagsAreCorrect(parameters);

    Assert.assertFalse(actual);
  }

  @Test
  public void checkIfTagsAreCorrect_whenALanguageIsNotValid_shouldReturnTrue() {
    List<Parameter> parameters = new ArrayList<>();
    Parameter parameter = new Parameter("test",
            "skills",
            new ArrayList<>(Collections.singletonList("Skill")));
    Parameter parameter2 = new Parameter("test",
            "languages",
            new ArrayList<>(Collections.singletonList("Language")));
    Parameter parameter3 = new Parameter("test",
            "city",
            new ArrayList<>(Collections.singletonList("City")));
    parameters.add(parameter);
    parameters.add(parameter2);
    parameters.add(parameter3);
    when(this.skillsRepositoryMock.getAll())
            .thenReturn(new ArrayList<>(Collections.singletonList(new Skill("Skill"))));
    when(this.languagesRepositoryMock.getAll())
            .thenReturn(new ArrayList<>(Collections.singletonList(new Language("Language1"))));
    when(this.citiesRepositoryMock.getAll())
            .thenReturn(new ArrayList<>(Collections.singletonList(new City("City"))));

    boolean actual = sut.checkIfTagsAreCorrect(parameters);

    Assert.assertFalse(actual);
  }

  @Test
  public void checkIfTagsAreCorrect_whenLanguagesAreSame_shouldReturnTrue() {
    List<Parameter> parameters = new ArrayList<>();
    Parameter parameter = new Parameter("test",
            "skills",
            new ArrayList<>(Arrays.asList("Skill")));
    Parameter parameter2 = new Parameter("test",
            "languages",
            new ArrayList<>(Arrays.asList("Language", "Language")));
    Parameter parameter3 = new Parameter("test",
            "city",
            new ArrayList<>(Collections.singletonList("City")));
    parameters.add(parameter);
    parameters.add(parameter2);
    parameters.add(parameter3);
    when(this.skillsRepositoryMock.getAll())
            .thenReturn(new ArrayList<>(Collections.singletonList(new Skill("Skill"))));
    when(this.languagesRepositoryMock.getAll())
            .thenReturn(new ArrayList<>(Collections.singletonList(new Language("Language"))));
    when(this.citiesRepositoryMock.getAll())
            .thenReturn(new ArrayList<>(Collections.singletonList(new City("City"))));

    boolean actual = sut.checkIfTagsAreCorrect(parameters);

    Assert.assertFalse(actual);
  }
}
