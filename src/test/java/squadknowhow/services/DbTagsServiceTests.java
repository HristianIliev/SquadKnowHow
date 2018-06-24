package squadknowhow.services;

import org.junit.Assert;
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
import squadknowhow.dbmodels.Skill;

import java.security.InvalidParameterException;
import java.util.ArrayList;
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

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void getCities_whenTermIsLessThan2Symbols_shouldThrowInvalidParameterException(){
        expectedEx.expect(InvalidParameterException.class);
        expectedEx.expectMessage("Term is not valid");
        String term = "1";
        DbTagsService sut = new DbTagsService(this.citiesRepositoryMock,
                this.skillsRepositoryMock,
                this.interestsRepositoryMock,
                this.companiesRepositoryMock);

        sut.getCities(term);
    }

    @Test
    public void getCities_whenTermIsValidButNothingIsFound_shouldReturnHashSetWithFirstItemResultNotFound(){
        HashSet<String> expected = new HashSet<>();
        expected.add("Не съществува такъв резултат");
        String term = "valid";
        List<City> cities = new ArrayList<>();
        cities.add(new City("nope"));
        when(this.citiesRepositoryMock.getAll()).thenReturn(cities);
        DbTagsService sut = new DbTagsService(this.citiesRepositoryMock,
                this.skillsRepositoryMock,
                this.interestsRepositoryMock,
                this.companiesRepositoryMock);

        HashSet<String> actual = sut.getCities(term);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getCities_whenTermIsValidAndElementsAreFound_shouldReturnHashSetWithTheCityNames(){
        HashSet<String> expected = new HashSet<>();
        expected.add("valid");
        String term = "valid";
        List<City> cities = new ArrayList<>();
        cities.add(new City("valid"));
        when(this.citiesRepositoryMock.getAll()).thenReturn(cities);
        DbTagsService sut = new DbTagsService(this.citiesRepositoryMock,
                this.skillsRepositoryMock,
                this.interestsRepositoryMock,
                this.companiesRepositoryMock);

        HashSet<String> actual = sut.getCities(term);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getSkills_whenTermIsLessThan2Symbols_shouldThrowInvalidParameterException(){
        expectedEx.expect(InvalidParameterException.class);
        expectedEx.expectMessage("Term is not valid");
        String term = "1";
        DbTagsService sut = new DbTagsService(this.citiesRepositoryMock,
                this.skillsRepositoryMock,
                this.interestsRepositoryMock,
                this.companiesRepositoryMock);

        sut.getSkills(term);
    }

    @Test
    public void getSkills_whenTermIsValidButNothingIsFound_shouldReturnHashSetWithFirstItemResultNotFound(){
        HashSet<String> expected = new HashSet<>();
        expected.add("Не съществува такъв резултат");
        String term = "valid";
        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill("nope"));
        when(this.skillsRepositoryMock.getAll()).thenReturn(skills);
        DbTagsService sut = new DbTagsService(this.citiesRepositoryMock,
                this.skillsRepositoryMock,
                this.interestsRepositoryMock,
                this.companiesRepositoryMock);

        HashSet<String> actual = sut.getSkills(term);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getSkills_whenTermIsValidAndElementsAreFound_shouldReturnHashSetWithTheSkillNames(){
        HashSet<String> expected = new HashSet<>();
        expected.add("valid");
        String term = "valid";
        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill("valid"));
        when(this.skillsRepositoryMock.getAll()).thenReturn(skills);
        DbTagsService sut = new DbTagsService(this.citiesRepositoryMock,
                this.skillsRepositoryMock,
                this.interestsRepositoryMock,
                this.companiesRepositoryMock);

        HashSet<String> actual = sut.getSkills(term);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getInterests_whenTermIsLessThan2Symbols_shouldThrowInvalidParameterException(){
        expectedEx.expect(InvalidParameterException.class);
        expectedEx.expectMessage("Term is not valid");
        String term = "1";
        DbTagsService sut = new DbTagsService(this.citiesRepositoryMock,
                this.skillsRepositoryMock,
                this.interestsRepositoryMock,
                this.companiesRepositoryMock);

        sut.getInterests(term);
    }

    @Test
    public void getInterests_whenTermIsValidButNothingIsFound_shouldReturnHashSetWithFirstItemResultNotFound(){
        HashSet<String> expected = new HashSet<>();
        expected.add("Не съществува такъв резултат");
        String term = "valid";
        List<Interest> interests = new ArrayList<>();
        interests.add(new Interest("nope"));
        when(this.interestsRepositoryMock.getAll()).thenReturn(interests);
        DbTagsService sut = new DbTagsService(this.citiesRepositoryMock,
                this.skillsRepositoryMock,
                this.interestsRepositoryMock,
                this.companiesRepositoryMock);

        HashSet<String> actual = sut.getInterests(term);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getInterests_whenTermIsValidAndElementsAreFound_shouldReturnHashSetWithTheInterestNames(){
        HashSet<String> expected = new HashSet<>();
        expected.add("valid");
        String term = "valid";
        List<Interest> interests = new ArrayList<>();
        interests.add(new Interest("valid"));
        when(this.interestsRepositoryMock.getAll()).thenReturn(interests);
        DbTagsService sut = new DbTagsService(this.citiesRepositoryMock,
                this.skillsRepositoryMock,
                this.interestsRepositoryMock,
                this.companiesRepositoryMock);

        HashSet<String> actual = sut.getInterests(term);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getEmployedBy_whenTermIsLessThan2Symbols_shouldThrowInvalidParameterException(){
        expectedEx.expect(InvalidParameterException.class);
        expectedEx.expectMessage("Term is not valid");
        String term = "1";
        DbTagsService sut = new DbTagsService(this.citiesRepositoryMock,
                this.skillsRepositoryMock,
                this.interestsRepositoryMock,
                this.companiesRepositoryMock);

        sut.getEmployedBy(term);
    }

    @Test
    public void getEmployedBy_whenTermIsValidButNothingIsFound_shouldReturnHashSetWithFirstItemResultNotFound(){
        HashSet<String> expected = new HashSet<>();
        expected.add("Не съществува такъв резултат");
        String term = "valid";
        List<Company> companies = new ArrayList<>();
        companies.add(new Company("nope"));
        when(this.companiesRepositoryMock.getAll()).thenReturn(companies);
        DbTagsService sut = new DbTagsService(this.citiesRepositoryMock,
                this.skillsRepositoryMock,
                this.interestsRepositoryMock,
                this.companiesRepositoryMock);

        HashSet<String> actual = sut.getEmployedBy(term);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getEmployedBy_whenTermIsValidAndElementsAreFound_shouldReturnHashSetWithTheCompanyNames(){
        HashSet<String> expected = new HashSet<>();
        expected.add("valid");
        String term = "valid";
        List<Company> companies = new ArrayList<>();
        companies.add(new Company("valid"));
        when(this.companiesRepositoryMock.getAll()).thenReturn(companies);
        DbTagsService sut = new DbTagsService(this.citiesRepositoryMock,
                this.skillsRepositoryMock,
                this.interestsRepositoryMock,
                this.companiesRepositoryMock);

        HashSet<String> actual = sut.getEmployedBy(term);

        Assert.assertEquals(expected, actual);
    }
}
