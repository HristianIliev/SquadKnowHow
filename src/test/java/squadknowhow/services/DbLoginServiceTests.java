package squadknowhow.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import squadknowhow.contracts.ILoginService;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.User;
import squadknowhow.response.models.ResponseLogin;
import squadknowhow.utils.validators.EmailValidator;
import squadknowhow.utils.validators.PasswordValidator;
import squadknowhow.utils.validators.base.IValidator;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class DbLoginServiceTests {
    @Mock
    private IRepository<User> usersRepositoryMock;

    private IValidator<String> passwordValidator;
    private IValidator<String> emailValidator;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUpValidators() {
        this.passwordValidator = new PasswordValidator();
        this.emailValidator = new EmailValidator();
    }

    @Test
    public void attemptLogin_whenEmailIsEmpty_shouldThrowInvalidParameterException() {
        expectedEx.expect(InvalidParameterException.class);
        expectedEx.expectMessage("Email is not valid");
        String email = "";
        String password = "password to pass";
        DbLoginService sut = new DbLoginService(this.usersRepositoryMock,
                this.passwordValidator,
                this.emailValidator);

        sut.attemptLogin(email, password);
    }

    @Test
    public void attemptLogin_whenEmailDoesntContainAt_shouldThrowInvalidParameterException() {
        expectedEx.expect(InvalidParameterException.class);
        expectedEx.expectMessage("Email is not valid");
        String email = "dasdlasmkmdskcd";
        String password = "password to pass";
        DbLoginService sut = new DbLoginService(this.usersRepositoryMock,
                this.passwordValidator,
                this.emailValidator);

        sut.attemptLogin(email, password);
    }

    @Test
    public void attemptLogin_whenPasswordIsEmpty_shouldThrowInvalidParameterException() {
        expectedEx.expect(InvalidParameterException.class);
        expectedEx.expectMessage("Password is not valid");
        String email = "hristian00i@abv.bg";
        String password = "";
        DbLoginService sut = new DbLoginService(this.usersRepositoryMock,
                this.passwordValidator,
                this.emailValidator);

        sut.attemptLogin(email, password);
    }

    @Test
    public void attemptLogin_whenPasswordIsLessThan8Symbols_shouldThrowInvalidParameterException() {
        expectedEx.expect(InvalidParameterException.class);
        expectedEx.expectMessage("Password is not valid");
        String email = "hristian00i@abv.bg";
        String password = "dsa";
        DbLoginService sut = new DbLoginService(this.usersRepositoryMock,
                this.passwordValidator,
                this.emailValidator);

        sut.attemptLogin(email, password);
    }

    @Test
    public void attemptLogin_whenPasswordIsMoreThan20Symbols_shouldThrowIvalidParameterException() {
        expectedEx.expect(InvalidParameterException.class);
        expectedEx.expectMessage("Password is not valid");
        String email = "hristian00i@abv.bg";
        String password = "01234567891011112131415";
        DbLoginService sut = new DbLoginService(this.usersRepositoryMock,
                this.passwordValidator,
                this.emailValidator);

        sut.attemptLogin(email, password);
    }

    @Test
    public void attemptLogin_whenUserWithTheSameEmailHasNotBeenFound_shouldReturnResponseLoginWithInvalidCredentials() {
        ResponseLogin expected = new ResponseLogin("Invalid credetentials", -1);
        String email = "testemail@abv.bg";
        String password = "password to pass";
        List<User> list = new ArrayList<>();
        list.add(new User("nottestemail@abv.bg"));
        when(this.usersRepositoryMock.getAll()).thenReturn(list);
        DbLoginService sut = new DbLoginService(this.usersRepositoryMock,
                this.passwordValidator,
                this.emailValidator);

        ResponseLogin actual = sut.attemptLogin(email, password);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void attemptLogin_whenTheFoundUsersPasswordDoesntMatchThePassword_shouldReturnResponseLoginWithInvalidCredetentials() {
        ResponseLogin expected = new ResponseLogin("Invalid credetentials", -1);
        String email = "testemail@abv.bg";
        String password = "password to pass";
        List<User> list = new ArrayList<>();
        list.add(new User("testemail@abv.bg", "password to spass"));
        when(this.usersRepositoryMock.getAll()).thenReturn(list);
        DbLoginService sut = new DbLoginService(this.usersRepositoryMock,
                this.passwordValidator,
                this.emailValidator);

        ResponseLogin actual = sut.attemptLogin(email, password);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void attemptLogin_whenTheCredentialsAreCorrect_shouldReturnResponseLoginWithloginSuccessfull() {
        ResponseLogin expected = new ResponseLogin("Login successfull", 1);
        String email = "testemail@abv.bg";
        String password = "password to pass";
        List<User> list = new ArrayList<>();
        list.add(new User(1, "testemail@abv.bg", "password to pass"));
        when(this.usersRepositoryMock.getAll()).thenReturn(list);
        DbLoginService sut = new DbLoginService(this.usersRepositoryMock,
                this.passwordValidator,
                this.emailValidator);

        ResponseLogin actual = sut.attemptLogin(email, password);

        Assert.assertEquals(expected, actual);
    }
}
