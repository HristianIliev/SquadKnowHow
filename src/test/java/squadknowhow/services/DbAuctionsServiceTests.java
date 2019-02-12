package squadknowhow.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.Auction;
import squadknowhow.dbmodels.Bid;
import squadknowhow.dbmodels.City;
import squadknowhow.dbmodels.Message;
import squadknowhow.dbmodels.Notification;
import squadknowhow.dbmodels.Project;
import squadknowhow.dbmodels.User;
import squadknowhow.request.models.AuctionData;
import squadknowhow.response.models.ResponseCreateAuction;
import squadknowhow.response.models.ResponsePagination;
import squadknowhow.utils.validators.IdValidator;
import squadknowhow.utils.validators.base.IValidator;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class DbAuctionsServiceTests {
  @Mock
  private IRepository<Auction> auctionsRepostiory;
  @Mock
  private IRepository<Project> projectsRepository;
  @Mock
  private IRepository<User> usersRepository;
  @Mock
  private IRepository<Bid> bidsRepository;
  @Mock
  private IRepository<Message> messagesRepository;
  @Mock
  private IRepository<Notification> notificationsRepository;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  private IValidator<Integer> idValidator;
  private DbAuctionsService sut;

  @Before
  public void setUpDependencies() {
    this.idValidator = new IdValidator();
    this.sut = new DbAuctionsService(this.auctionsRepostiory,
            this.projectsRepository,
            this.usersRepository,
            this.bidsRepository,
            this.messagesRepository,
            this.notificationsRepository,
            this.idValidator);
  }

  @Test
  public void getAuctions_whenPageIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Page cannot be less than 1");

    sut.getAuctions("test", 0);
  }

  @Test
  public void getAuctions_whenPageIsValid_shouldReturnTheAuctions() {
    List<Auction> auctions = new ArrayList<>();
    for (int i = 0; i < 40; i++) {
      auctions.add(new Auction());
    }
    when(this.auctionsRepostiory.getAll()).thenReturn(auctions);

    List<Auction> actual = sut.getAuctions("test", 2);

    Assert.assertEquals(12, actual.size());
  }

  @Test
  public void getAuctions_whenPageIsValidButToIndexIsMoreThanAuctions_shouldReturnTheAuctions(){
    List<Auction> auctions = new ArrayList<>();
    for (int i = 0; i < 40; i++) {
      auctions.add(new Auction());
    }
    when(this.auctionsRepostiory.getAll()).thenReturn(auctions);

    List<Auction> actual = sut.getAuctions("test", 4);

    Assert.assertEquals(4, actual.size());
  }

  @Test
  public void createAuction_whenEndDateIsBeforeNow_shouldReturnResponseCreateAuctionWithResponseId1() throws ParseException {
    ResponseCreateAuction expected = new ResponseCreateAuction(1);
    AuctionData auctionData = new AuctionData();
    auctionData.setEndDate("2018-08-29");
    auctionData.setEndHour("12:54");

    ResponseCreateAuction actual = sut.createAuction(auctionData);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createAuction_whenAuctionAlreadyExists_shouldReturnResponseCreateAuctionWith0() throws ParseException {
    ResponseCreateAuction expected = new ResponseCreateAuction(0);
    AuctionData auctionData = new AuctionData();
    auctionData.setEndDate("2022-08-31");
    auctionData.setEndHour("12:54");
    auctionData.setProjectId(1);
    List<Auction> auctions = new ArrayList<>();
    Auction auction = new Auction();
    auction.setProjectId(1);
    auctions.add(auction);
    when(this.auctionsRepostiory.getAll()).thenReturn(auctions);

    ResponseCreateAuction actual = sut.createAuction(auctionData);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createAuction_whenParametersAreValidAndHasBuyMeNow_shouldReturnResponseCreateAuction() throws ParseException {
    ResponseCreateAuction expected = new ResponseCreateAuction(2);
    AuctionData auctionData = new AuctionData();
    auctionData.setEndDate("2022-08-31");
    auctionData.setEndHour("12:54");
    auctionData.setProjectId(1);
    auctionData.setBuyMeNow(1);
    List<Auction> auctions = new ArrayList<>();
    Auction auction = new Auction();
    auction.setProjectId(3);
    auctions.add(auction);
    Project project = new Project();
    project.setId(1);
    project.setName("Name");
    project.setDescription("Description");
    project.setCover("");
    project.setCity("name");
    project.setCreator(1);
    when(this.auctionsRepostiory.getAll()).thenReturn(auctions);
    when(this.projectsRepository.getById(1)).thenReturn(project);

    ResponseCreateAuction actual = sut.createAuction(auctionData);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createAuction_whenParametersAreValid_shouldReturnResponseCreateAuction2() throws ParseException {
    ResponseCreateAuction expected = new ResponseCreateAuction(2);
    AuctionData auctionData = new AuctionData();
    auctionData.setEndDate("2022-08-31");
    auctionData.setEndHour("12:54");
    auctionData.setProjectId(1);
    auctionData.setBuyMeNow(0);
    List<Auction> auctions = new ArrayList<>();
    Auction auction = new Auction();
    auction.setProjectId(3);
    auctions.add(auction);
    Project project = new Project();
    project.setId(1);
    project.setName("Name");
    project.setDescription("Description");
    project.setCover("");
    project.setCity("name");
    project.setCreator(1);
    when(this.auctionsRepostiory.getAll()).thenReturn(auctions);
    when(this.projectsRepository.getById(1)).thenReturn(project);

    ResponseCreateAuction actual = sut.createAuction(auctionData);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void startFollowingAuction_whenAuctionIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("AuctionId is not valid");
    int auctionId = 0;
    int userId = 1;

    sut.startFollowingAuction(auctionId, userId);
  }

  @Test
  public void startFollowingAuction_whenUserIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("UserId is not valid");
    int auctionId = 1;
    int userId = 0;

    sut.startFollowingAuction(auctionId, userId);
  }

  @Test
  public void startFollowingAuction_whenParametersAreValidButUserIsAlreadyFollowing_shouldReturnFalse() {
    int auctionId = 1;
    int userId = 1;
    Auction auction = new Auction();
    auction.setId(auctionId);
    User user = new User();
    user.setId(1);
    List<User> users = new ArrayList<>();
    users.add(user);
    auction.setUsersWatching(users);
    when(this.auctionsRepostiory.getById(auctionId)).thenReturn(auction);
    when(this.usersRepository.getById(userId)).thenReturn(user);

    boolean actual = sut.startFollowingAuction(auctionId, userId);

    Assert.assertFalse(actual);
  }

  @Test
  public void startFollowingAuction_whenParametersAreValidAndUserIsNotFollowingAlready_shouldReturnTrue() {
    int auctionId = 1;
    int userId = 1;
    Auction auction = new Auction();
    auction.setId(auctionId);
    User user = new User();
    user.setId(userId);
    auction.setUsersWatching(new ArrayList<>());
    user.setWatchedAuctions(new ArrayList<>());
    when(this.auctionsRepostiory.getById(auctionId)).thenReturn(auction);
    when(this.usersRepository.getById(userId)).thenReturn(user);

    boolean actual = sut.startFollowingAuction(auctionId, userId);

    Assert.assertTrue(actual);
  }

  @Test
  public void stopFollowingAuction_whenAuctionIdIsNotValid_shouldThrowNewInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("AuctionId is not valid");
    int auctionId = 0;
    int userId = 1;

    sut.stopFollowingAuction(auctionId, userId);
  }

  @Test
  public void stopFollowingAuction_whenUserIdIsNotValid_shouldThrowNewInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("UserId is not valid");
    int auctionId = 1;
    int userId = 0;

    sut.stopFollowingAuction(auctionId, userId);
  }

  @Test
  public void stopFollowingAuction_whenParametersAreValid_shouldReturnFalse() {
    int auctionId = 1;
    int userId = 1;
    Auction auction = new Auction();
    auction.setUsersWatching(new ArrayList<>());
    User user = new User();
    user.setWatchedAuctions(new ArrayList<>());
    when(this.auctionsRepostiory.getById(auctionId)).thenReturn(auction);
    when(this.usersRepository.getById(userId)).thenReturn(user);

    boolean actual = sut.stopFollowingAuction(auctionId, userId);

    Assert.assertTrue(actual);
  }

  @Test
  public void getFollowingAuctions_whenUserIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("UserId is not valid");
    int userId = 0;

    sut.getFollowingAuctions(userId);
  }

  @Test
  public void getFollowingAuctions_whenParametersAreValid_shouldReturnListOfTheIds() {
    int userId = 1;
    User user = new User();
    List<Auction> auctions = new ArrayList<>();
    Auction auction = new Auction();
    auction.setId(2);
    auctions.add(auction);
    user.setWatchedAuctions(auctions);
    when(this.usersRepository.getById(userId)).thenReturn(user);

    List<Integer> actual = sut.getFollowingAuctions(userId);

    Assert.assertEquals(Integer.valueOf(2), actual.get(0));
  }

  @Test
  public void getAuction_whenIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int id = 0;

    sut.getAuction(id);
  }

  @Test
  public void getAuction_whenParametersAreValid_shouldReturnTheAuction() {
    int id = 1;
    Auction expected = new Auction();
    expected.setId(id);
    when(this.auctionsRepostiory.getById(id)).thenReturn(expected);

    Auction actual = sut.getAuction(id);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getEndDate_whenIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int id = 0;

    sut.getEndDate(id);
  }

  @Test
  public void getEndDate_whenIdIsValid_shouldReturnTheEndDate() {
    int id = 1;
    Auction auction = new Auction();
    auction.setEndDate("test");
    when(this.auctionsRepostiory.getById(id)).thenReturn(auction);

    String actual = sut.getEndDate(id);

    Assert.assertEquals(actual, auction.getEndDate());
  }

  @Test
  public void createBid_whenUserIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("UserId is not valid");
    int userId = 0;
    int auctionId = 1;
    double amount = 0.0;

    sut.createBid(userId, auctionId, amount);
  }

  @Test
  public void createBid_whenAuctionIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("AuctionId is not valid");
    int userId = 1;
    int auctionId = 0;
    double amount = 0.0;

    sut.createBid(userId, auctionId, amount);
  }

  @Test
  public void createBid_whenAmountIsLessThanMaxBid_shouldThrowInvaidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("The amount of the bid must be bigger than the max bid");
    int userId = 1;
    int auctionId = 1;
    double amount = 1.23;
    Auction auction = new Auction();
    List<Bid> bids = new ArrayList<>();
    Bid bid = new Bid();
    bid.setAmount(1.24);
    bids.add(bid);
    auction.setBids(bids);
    when(this.auctionsRepostiory.getById(auctionId)).thenReturn(auction);

    sut.createBid(userId, auctionId, amount);
  }

  @Test
  public void createBid_whenParametersAreValidButUserDoesntHaveEnoughMoney_shouldReturnTrue() {
    int userId = 1;
    int auctionId = 1;
    double amount = 1.23;
    Auction auction = new Auction();
    List<Bid> bids = new ArrayList<>();
    Bid bid = new Bid();
    bid.setAmount(1.22);
    bids.add(bid);
    auction.setBids(bids);
    User user = new User();
    user.setMoney(1.22);
    when(this.usersRepository.getById(userId)).thenReturn(user);
    when(this.auctionsRepostiory.getById(auctionId)).thenReturn(auction);

    boolean actual = sut.createBid(userId, auctionId, amount);

    Assert.assertFalse(actual);
  }

  @Test
  public void createBid_whenParametersAreValidAndUserHasEnoughMoney_shouldReturnTrue() {
    int userId = 1;
    int auctionId = 1;
    double amount = 1.23;
    Auction auction = new Auction();
    List<Bid> bids = new ArrayList<>();
    Bid bid = new Bid();
    bid.setAmount(1.22);
    bids.add(bid);
    auction.setBids(bids);
    User user = new User();
    user.setMoney(1.24);
    when(this.usersRepository.getById(userId)).thenReturn(user);
    when(this.auctionsRepostiory.getById(auctionId)).thenReturn(auction);

    boolean actual = sut.createBid(userId, auctionId, amount);

    Assert.assertTrue(actual);
  }

  @Test
  public void finishAuction_whenIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("Id is not valid");
    int id = 0;
    boolean isItFromBuyNow = false;
    int buyNowId = 0;

    sut.finishAuction(id, isItFromBuyNow, buyNowId);
  }

  @Test
  public void finishAuction_whenParamtersAreValidAndIsNotFromBuyNow_shouldReturnTrue(){
    int id = 1;
    boolean isItFromBuyNow = false;
    int buyNowId = 3;
    Auction auction = new Auction();
    auction.setCreatorId(2);
    auction.setProjectId(3);
    List<Bid> bids = new ArrayList<>();
    Bid bid = new Bid();
    User user = new User();
    user.setMoney(23.44);
    User winner = new User();
    winner.setId(4);
    winner.setMoney(23.44);
    bid.setBidder(winner);
    bid.setAmount(23.44);
    bids.add(bid);
    auction.setBids(bids);
    auction.setTitle("test");
    auction.setBuyMeNow(20.5);
    when(this.projectsRepository.getById(3)).thenReturn(new Project(new ArrayList<>()));
    when(this.auctionsRepostiory.getById(id)).thenReturn(auction);
    when(this.usersRepository.getById(2)).thenReturn(user);
    when(this.usersRepository.getById(buyNowId)).thenReturn(winner);

    boolean actual = sut.finishAuction(id, isItFromBuyNow, buyNowId);

    Assert.assertTrue(actual);
  }

  @Test
  public void finishAuction_whenParametersAreValidAndTheWinnerIsFromBuyNowId_shouldReturnTrue() {
    int id = 1;
    boolean isItFromBuyNow = true;
    int buyNowId = 3;
    Auction auction = new Auction();
    auction.setCreatorId(2);
    auction.setProjectId(3);
    List<Bid> bids = new ArrayList<>();
    Bid bid = new Bid();
    User user = new User();
    user.setMoney(23.44);
    User winner = new User();
    winner.setId(4);
    winner.setMoney(23.44);
    bid.setBidder(winner);
    bid.setAmount(23.44);
    auction.setBids(bids);
    auction.setTitle("test");
    auction.setBuyMeNow(20.5);
    bids.add(bid);
    when(this.projectsRepository.getById(3)).thenReturn(new Project(new ArrayList<>()));
    when(this.auctionsRepostiory.getById(id)).thenReturn(auction);
    when(this.usersRepository.getById(2)).thenReturn(user);
    when(this.usersRepository.getById(buyNowId)).thenReturn(winner);

    boolean actual = sut.finishAuction(id, isItFromBuyNow, buyNowId);

    Assert.assertTrue(actual);
  }

  @Test
  public void buyNowAuction_whenUserIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("UserId is not valid");
    int userId = 0;
    int auctionId = 1;

    sut.buyNowAuction(userId, auctionId);
  }

  @Test
  public void buyNowAuction_whenAuctionIdIsNotValid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("AuctionId is not valid");
    int userId = 1;
    int auctionId = 0;

    sut.buyNowAuction(userId, auctionId);
  }

  @Test
  public void buyNowAuction_whenParametersAreValidButUserHasLessMoney_shouldReturnFalse() {
    int userId = 1;
    int auctionId = 1;
    User user = new User();
    user.setMoney(20.24);
    Auction auction = new Auction();
    auction.setBuyMeNow(20.25);
    when(this.usersRepository.getById(userId)).thenReturn(user);
    when(this.auctionsRepostiory.getById(auctionId)).thenReturn(auction);

    boolean actual = sut.buyNowAuction(userId, auctionId);

    Assert.assertFalse(actual);
  }

  @Test
  public void buyNowAuction_whenParametersAreValidAndUserHasEnoughMoney_shouldReturnTrue() {
    int userId = 1;
    int auctionId = 1;
    User user = new User();
    user.setMoney(20.24);
    user.setId(2);
    Auction auction = new Auction();
    auction.setBuyMeNow(20.23);
    auction.setCreatorId(2);
    auction.setProjectId(3);
    auction.setBids(new ArrayList<>());
    when(this.projectsRepository.getById(3)).thenReturn(new Project(new ArrayList<>()));
    when(this.usersRepository.getById(2)).thenReturn(user);
    when(this.usersRepository.getById(userId)).thenReturn(user);
    when(this.auctionsRepostiory.getById(auctionId)).thenReturn(auction);

    boolean actual = sut.buyNowAuction(userId, auctionId);

    Assert.assertTrue(actual);
  }

  @Test
  public void deleteAuction_whenAuctionIdIsInvalid_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("AuctionId is not valid");
    int auctionId = 0;

    sut.deleteAuction(auctionId);
  }

  @Test
  public void deleteAuction_whenParametersAreValid_shouldReturnTheAuction() {
    int auctionId = 1;
    Auction expected = new Auction();
    expected.setId(auctionId);
    List<Bid> bids = new ArrayList<>();
    bids.add(new Bid());
    expected.setBids(bids);
    when(this.auctionsRepostiory.getById(auctionId)).thenReturn(expected);
    when(this.auctionsRepostiory.delete(expected)).thenReturn(expected);

    Auction actual = sut.deleteAuction(auctionId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getAuctionPages_whenCalled_shouldReturnResponsePagiantionWithTheCorrectData() {
    ResponsePagination expected = new ResponsePagination(2, 24);
    List<Auction> auctions = new ArrayList<>();
    for (int i = 0; i < 24; i++) {
      auctions.add(new Auction());
    }
    when(this.auctionsRepostiory.getAll()).thenReturn(auctions);

    ResponsePagination actual = sut.getAuctionPages();

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void isUserAlreadyFollowing_whenAuctionHasTheUser_shouldReturnTrue() {
    Auction auction = new Auction();
    User user = new User(2);
    List<User> users = new ArrayList<>();
    users.add(new User(2));
    auction.setUsersWatching(users);

    boolean actual = sut.isUserAlreadyFollowing(auction, user);

    Assert.assertTrue(actual);
  }

  @Test
  public void isUserAlreadyFollowing_whenUserHasTheAuction_shouldReturnTrue() {
    Auction auction = new Auction();
    auction.setId(2);
    User user = new User(2);
    List<User> users = new ArrayList<>();
    users.add(new User(1));
    auction.setUsersWatching(users);
    List<Auction> auctions = new ArrayList<>();
    auctions.add(auction);
    user.setWatchedAuctions(auctions);

    boolean actual = sut.isUserAlreadyFollowing(auction, user);

    Assert.assertTrue(actual);
  }

  @Test
  public void isUserAlreadyFollowing_whenUserIsNotAlreadyFollowing_shoulReturnFalse() {
    Auction auction = new Auction();
    auction.setUsersWatching(new ArrayList<>());
    User user = new User(2);
    user.setWatchedAuctions(new ArrayList<>());

    boolean actual = sut.isUserAlreadyFollowing(auction, user);

    Assert.assertFalse(actual);
  }
}
