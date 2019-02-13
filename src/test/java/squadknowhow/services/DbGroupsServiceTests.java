package squadknowhow.services;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.Group;
import squadknowhow.dbmodels.GroupShort;
import squadknowhow.dbmodels.User;
import squadknowhow.response.models.ResponseCheckGroupName;
import squadknowhow.response.models.ResponseGroupId;
import squadknowhow.response.models.ResponsePagination;
import squadknowhow.response.models.ResponseUpload;
import squadknowhow.utils.validators.GroupValidator;
import squadknowhow.utils.validators.IdValidator;
import squadknowhow.utils.validators.base.IValidator;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

public class DbGroupsServiceTests {
  @Mock
  private IRepository<Group> groupsRepositoryMock;
  @Mock
  private IRepository<GroupShort> groupsShortRepository;
  @Mock
  private IRepository<User> usersRepositoryMock;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  private IValidator<Group> groupsValidator;
  private IValidator<Integer> idValidator;
  private DbGroupsService sut;

  @Before
  public void setUpDependencies() {
    this.groupsValidator = new GroupValidator();
    this.idValidator = new IdValidator();
    sut = new DbGroupsService(this.groupsRepositoryMock,
            this.groupsShortRepository,
            this.usersRepositoryMock,
            this.groupsValidator,
            this.idValidator);
  }

//  @After
//  public void dsadasda(){
//    System.out.println("AFTER");
//  }

  @Test
  public void createGroup_whenCreatorIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("CreatorId is not valid");
    Group groupStub = new Group();
    groupStub.setName("Test name to pass");
    groupStub.setDescription("Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmklcmasdmklcmklasdmcklsadmsdlamclkmsadlkcmsdklaklcasdmacmklmasasdmklasdklmcmklasmcklmasdklcmasmkldcmlkasdklmc123456789");
    groupStub.setGroupType("Group type to pass");
    int creatorId = 0;

    sut.createGroup(groupStub, creatorId);
  }

  @Test
  public void createGroup_whenGroupNameIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Group is not valid");
    Group groupStub = new Group();
    groupStub.setDescription("Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmklcmasdmklcmklasdmcklsadmsdlamclkmsadlkcmsdklaklcasdmacmklmasasdmklasdklmcmklasmcklmasdklcmasmkldcmlkasdklmc123456789");
    groupStub.setGroupType("Group type to pass");
    int creatorId = 1;

    sut.createGroup(groupStub, creatorId);
  }

  @Test
  public void createGroup_whenGroupNameIsLessThan5Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Group is not valid");
    Group groupStub = new Group();
    groupStub.setName("1234");
    groupStub.setDescription("Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmklcmasdmklcmklasdmcklsadmsdlamclkmsadlkcmsdklaklcasdmacmklmasasdmklasdklmcmklasmcklmasdklcmasmkldcmlkasdklmc123456789");
    groupStub.setGroupType("Group type to pass");
    int creatorId = 1;

    sut.createGroup(groupStub, creatorId);
  }

  @Test
  public void createGroup_whenGroupDescriptionIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Group is not valid");
    Group groupStub = new Group();
    groupStub.setName("Group name to pass");
    groupStub.setGroupType("Group type to pass");
    int creatorId = 1;

    sut.createGroup(groupStub, creatorId);
  }

  @Test
  public void createGroup_whenGroupDescriptionIsLessThan150Symbols_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Group is not valid");
    Group groupStub = new Group();
    groupStub.setName("Group name to pass");
    groupStub.setDescription("Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmklcmasdmklcmklasdmcklsadmsdlamclkmsadlkcm");
    groupStub.setGroupType("Group type to pass");
    int creatorId = 1;

    sut.createGroup(groupStub, creatorId);
  }

  @Test
  public void createGroup_whenGroupTypeIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Group is not valid");
    Group groupStub = new Group();
    groupStub.setName("Group name to pass");
    groupStub.setDescription("Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmklcmasdmklcmklasdmcklsadmsdlamclkmsadlkcmsdklaklcasdmacmklmasasdmklasdklmcmklasmcklmasdklcmasmkldcmlkasdklmc123456789");
    int creatorId = 1;

    sut.createGroup(groupStub, creatorId);
  }

  @Test
  public void createGroup_whenParametersAreValid_shouldReturnResponseGroupIdWithTheInsertedGroupsId() {
    int expected = 20;
    Group groupStub = new Group();
    groupStub.setName("Group name to pass");
    groupStub.setDescription("Description to pass cdasdcasddcasdlkkcmsadklcmklasdkmklcmasdmklcmklasdmcklsadmsdlamclkmsadlkcmsdklaklcasdmacmklmasasdmklasdklmcmklasmcklmasdklcmasmkldcmlkasdklmc123456789");
    groupStub.setGroupType("GroupType to pass");
    int creatorId = 1;
    when(this.usersRepositoryMock.getById(anyInt())).thenReturn(new User());
    when(this.groupsRepositoryMock.create(isA(Group.class))).thenAnswer(invocation -> {
      Group group = invocation.getArgument(0);
      group.setId(20);
      return group;
    });

    ResponseGroupId actual = sut.createGroup(groupStub, creatorId);

    Assert.assertEquals(expected, actual.getId());
  }

  @Test
  public void uploadImage_whenIdIsLessThan1_shouldThrowInvalidParameterException() throws IOException {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int expected = 0;
    DbGroupsService sut = new DbGroupsService(this.groupsRepositoryMock,
            this.groupsShortRepository,
            this.usersRepositoryMock,
            this.groupsValidator,
            this.idValidator);

    sut.uploadImage(null, expected);
  }

  @Test
  public void uploadImage_whenParametersAreValid_shouldReturnResponseUploadWithTrue() throws IOException {
    ResponseUpload expected = new ResponseUpload(true);
    int id = 1;
    when(this.groupsRepositoryMock.getById(id)).thenReturn(new Group());
    when(this.groupsRepositoryMock.update(isA(Group.class))).thenReturn(null);

    ResponseUpload actual = sut.uploadImage(null, id);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getGroupById_whenIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int expected = 0;

    sut.getGroupById(expected);
  }

  @Test
  public void getGroupById_whenIdIsValid_shouldReturnGroupWithThisId() {
    int expected = 1;
    when(this.groupsRepositoryMock.getById(expected)).thenReturn(new Group(expected));

    Group actual = sut.getGroupById(expected);

    Assert.assertEquals(expected, actual.getId());
  }

  @Test
  public void getGroupPages_whenCalled_shouldReturnResponsePagiantion() {
    int expectedNumberOfPages = 1;
    int expectedGroupsSize = 2;
    String testName = "test";
    List<Group> list = new ArrayList<>();
    list.add(new Group(testName));
    list.add(new Group(testName));
    when(this.groupsRepositoryMock.getAll()).thenReturn(list);

    ResponsePagination actual = sut.getGroupPages(testName);

    Assert.assertEquals(expectedNumberOfPages, actual.getNumberOfPages());
    Assert.assertEquals(expectedGroupsSize, actual.getNumberOfEntries());
  }

  @Test
  public void checkGroupName_whenCalledWithExistingName_shouldReturnResponseCheckGroupNameWithStatus400() {
    int expected = 400;
    String testName = "test";
    List<Group> list = new ArrayList<>();
    list.add(new Group(testName));
    when(this.groupsRepositoryMock.getAll()).thenReturn(list);

    ResponseCheckGroupName actual = sut.checkGroupName(testName);

    Assert.assertEquals(expected, actual.getStatus());
  }

  @Test
  public void checkGroupName_whenCalledWithNonexistingName_shouldReturnResponseCheckGroupNameWithStatus200() {
    int expected = 200;
    String testName = "test";
    List<Group> list = new ArrayList<>();
    list.add(new Group("not test"));
    when(this.groupsRepositoryMock.getAll()).thenReturn(list);

    ResponseCheckGroupName actual = sut.checkGroupName(testName);

    Assert.assertEquals(expected, actual.getStatus());
  }

  @Test
  public void getGroups_whenCalledWithPageLessThan1_shouldReturnNull() {
    int page = 0;

    List<GroupShort> actual = sut.getGroups(page, "test");

    Assert.assertNull(actual);
  }

  @Test
  public void getGroups_whenCalledWithValidParametersButGroupsSizeIsLessThan20_shouldReturnListWithGroups() {
    int page = 1;
    String name = "valid";
    List<GroupShort> expected = new ArrayList<>();
    expected.add(new GroupShort("valid"));
    when(this.groupsShortRepository.getAll()).thenReturn(expected);

    List<GroupShort> actual = sut.getGroups(page, name);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getGroups_whenCalledWithValidParametersButGroupsSizeIsMoreThan20_shouldReturnListOfGroups() {
    int page = 1;
    String name = "valid";
    List<GroupShort> expected = new ArrayList<>();
    for (int i = 0; i < 12; i++) {
      expected.add(new GroupShort("valid"));
    }
    List<GroupShort> groupsToReturn = new ArrayList<>();
    for (int i = 0; i < 40; i++) {
      groupsToReturn.add(new GroupShort("valid"));
    }
    when(this.groupsShortRepository.getAll()).thenReturn(groupsToReturn);

    List<GroupShort> actual = sut.getGroups(page, name);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void addGroupMember_whenCalledWithGroupIdLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("GroupId is not valid");
    int groupId = 0;
    int userId = 1;

    sut.addGroupMember(groupId, userId);
  }

  @Test
  public void addGroupMember_whenCalledWithUserIdLessThan1_shouldThrowInvalidParameterEception() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("UserId is not valid");
    int groupId = 1;
    int userId = 0;

    sut.addGroupMember(groupId, userId);
  }

  @Test
  public void addGroupMember_whenCalledWithValidIds_shouldReturnTrue() {
    int groupId = 1;
    int userId = 1;
    when(this.groupsRepositoryMock.getById(groupId)).thenReturn(new Group(new ArrayList<>()));
    when(this.groupsRepositoryMock.update(isA(Group.class))).thenReturn(null);
    when(this.usersRepositoryMock.getById(userId)).thenReturn(new User());

    boolean actual = sut.addGroupMember(groupId, userId);

    Assert.assertTrue(actual);
  }
}
