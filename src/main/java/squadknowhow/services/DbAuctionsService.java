package squadknowhow.services;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import squadknowhow.contracts.IAuctionsService;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.Auction;
import squadknowhow.dbmodels.Bid;
import squadknowhow.dbmodels.Message;
import squadknowhow.dbmodels.Notification;
import squadknowhow.dbmodels.Project;
import squadknowhow.dbmodels.User;
import squadknowhow.request.models.AuctionData;
import squadknowhow.response.models.ResponseCreateAuction;
import squadknowhow.response.models.ResponsePagination;
import squadknowhow.utils.validators.base.IValidator;

@Service
public class DbAuctionsService implements IAuctionsService {
  private static final int PAGE_LENGTH = 12;
  private static final double PAGE_LENGTH_DOUBLE = 12.0;
  private final IRepository<Auction> auctionsRepository;
  private final IRepository<Project> projectsRepository;
  private final IRepository<User> usersRepository;
  private final IRepository<Bid> bidsRepository;
  private final IRepository<Message> messagesRepository;
  private final IRepository<Notification> notificationsRepository;

  private final IValidator<Integer> idValidator;

  private List<Integer> auctionsBeingFinishedNow;

  @Autowired
  public DbAuctionsService(IRepository<Auction> auctionsRepository,
                           IRepository<Project> projectsRepository,
                           IRepository<User> usersRepository,
                           IRepository<Bid> bidsRepository,
                           IRepository<Message> messagesRepository,
                           IRepository<Notification> notificationsRepository,
                           IValidator<Integer> idValidator) {
    this.auctionsRepository = auctionsRepository;
    this.projectsRepository = projectsRepository;
    this.usersRepository = usersRepository;
    this.bidsRepository = bidsRepository;
    this.messagesRepository = messagesRepository;
    this.notificationsRepository = notificationsRepository;

    this.idValidator = idValidator;

    this.auctionsBeingFinishedNow = new ArrayList<>();
  }

  // Gets all auctions sorted by a parameter.
  @Override
  public List<Auction> getAuctions(String sortBy, int page) {
    if (page < 1) {
      throw new InvalidParameterException("Page cannot be less than 1");
    }

    List<Auction> auctions = this.auctionsRepository.getAll();

    switch (sortBy) {
      case "timeAsc":
        auctions.sort(Auction.TIME_ASC_COMPARATOR);
        break;
      case "timeDesc":
        auctions.sort(Auction.TIME_DESC_COMPARATOR);
        break;
      case "priceAsc":
        auctions.sort(Auction.PRICE_ASC_COMPARATOR);
        break;
      case "priceDesc":
        auctions.sort(Auction.PRICE_DESC_COMPARATOR);
        break;
      case "bidsAsc":
        auctions.sort(Auction.BIDS_ASC_COMPARATOR);
        break;
      case "bidsDesc":
        auctions.sort(Auction.BIDS_DESC_COMPARATOR);
        break;
      case "test":
        break;
      default:
        auctions.sort(Auction.TIME_ASC_COMPARATOR);
        break;
    }

    int fromIndex = (page - 1) * PAGE_LENGTH;
    int toIndex = fromIndex + PAGE_LENGTH;

    if (toIndex > auctions.size()) {
      toIndex = auctions.size();
    }

    return auctions.subList(fromIndex, toIndex);
  }

  @Override
  public ResponseCreateAuction createAuction(
          AuctionData auctionData) throws ParseException {
    if (this.isEndDateBeforeNow(auctionData.getEndDate(),
            auctionData.getEndHour())) {
      return new ResponseCreateAuction(1);
    }

    if (this.doesAuctionAlreadyExist(auctionData.getProjectId())) {
      return new ResponseCreateAuction(0);
    }

    Project project = this.projectsRepository
            .getById(auctionData.getProjectId());

    Auction auction = new Auction();
    auction.setTitle(project.getName());
    auction.setDescription(project.getDescription());
    auction.setPicture(project.getCover());
    auction.setLocation(project.getCity());
    auction.setEndDate(auctionData.getEndDate()
            + " " + auctionData.getEndHour());
    auction.setProjectId(project.getId());
    auction.setCreatorId(project.getCreator());

    // Determines if there should be a feature "Buy me now"
    if (auctionData.getBuyMeNow() == 0) {
      auction.setBuyMeNow(null);
    } else {
      auction.setBuyMeNow(auctionData.getBuyMeNow());
    }

    // Current date
    Date now = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    auction.setCreatedAt(sdf.format(now));

    this.auctionsRepository.create(auction);

    return new ResponseCreateAuction(2);
  }

  private boolean isEndDateBeforeNow(String endDate,
                                     String endHour) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    Date now = new Date();
    Date end = sdf.parse(endDate + " " + endHour);

    return end.before(now);
  }

  @Override
  public boolean startFollowingAuction(int auctionId, int userId) {
    if (!this.idValidator.isValid(auctionId)) {
      throw new InvalidParameterException("AuctionId is not valid");
    } else if (!this.idValidator.isValid(userId)) {
      throw new InvalidParameterException("UserId is not valid");
    }

    Auction auction = this.auctionsRepository.getById(auctionId);
    User user = this.usersRepository.getById(userId);
    if (this.isUserAlreadyFollowing(auction, user)) {
      return false;
    }

    List<User> users = auction.getUsersWatching();
    users.add(user);
    auction.setUsersWatching(users);

    List<Auction> auctions = user.getWatchedAuctions();
    auctions.add(auction);
    user.setWatchedAuctions(auctions);

    this.auctionsRepository.update(auction);
    this.usersRepository.update(user);

    return true;
  }

  @Override
  public boolean stopFollowingAuction(int auctionId, int userId) {
    if (!this.idValidator.isValid(auctionId)) {
      throw new InvalidParameterException("AuctionId is not valid");
    } else if (!this.idValidator.isValid(userId)) {
      throw new InvalidParameterException("UserId is not valid");
    }

    Auction auction = this.auctionsRepository.getById(auctionId);
    User user = this.usersRepository.getById(userId);

    auction.getUsersWatching().remove(user);
    user.getWatchedAuctions().remove(auction);

    this.auctionsRepository.update(auction);
    this.usersRepository.update(user);

    return true;
  }

  // Returns the auctions that the user is following
  @Override
  public List<Integer> getFollowingAuctions(int userId) {
    if (!this.idValidator.isValid(userId)) {
      throw new InvalidParameterException("UserId is not valid");
    }

    List<Integer> result = new ArrayList<>();
    User user = this.usersRepository.getById(userId);
    for (int i = 0; i < user.getWatchedAuctions().size(); i++) {
      result.add(user.getWatchedAuctions().get(i).getId());
    }

    return result;
  }

  @Override
  public Auction getAuction(int id) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    return this.auctionsRepository.getById(id);
  }

  // Gets the end date of an auction.
  @Override
  public String getEndDate(int id) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    return this.auctionsRepository.getById(id).getEndDate();
  }

  @Override
  public boolean createBid(int userId, int auctionId, double amount) {
    Auction auction = this.auctionsRepository.getById(auctionId);
    if (!this.idValidator.isValid(userId)) {
      throw new InvalidParameterException("UserId is not valid");
    } else if (!this.idValidator.isValid(auctionId)) {
      throw new InvalidParameterException("AuctionId is not valid");
    } else if (amount < auction.getMaxBid()
            && auction.getBids().size() != 0) {
      throw new InvalidParameterException(
              "The amount of the bid must be bigger than the max bid");
    }

    User user = this.usersRepository.getById(userId);
    if (user.getMoney() < amount) {
      return false;
    }

    this.usersRepository.update(user);

    Bid bid = new Bid();
    bid.setBidder(this.usersRepository.getById(userId));
    bid.setAuction(auction);
    bid.setAmount(amount);

    // Current date
    Date now = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    bid.setDate(sdf.format(now));

    this.bidsRepository.create(bid);

    return true;
  }

  @Override
  public boolean finishAuction(int id, boolean isItFromBuyNow, int buyNowId) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    if (this.auctionsBeingFinishedNow.stream()
            .anyMatch(a -> a.equals(id))) {
      return false;
    } else {
      this.auctionsBeingFinishedNow.add(id);
    }

    Auction auction = this.auctionsRepository.getById(id);

    User user = this.usersRepository.getById(auction.getCreatorId());
    if (!isItFromBuyNow) {
      user.setMoney(user.getMoney() + auction.getMaxBid());
    } else {
      user.setMoney(user.getMoney() + auction.getBuyMeNow());
    }

    User winner;
    if (!isItFromBuyNow) {
      winner = auction.getMaxBidBid().getBidder();
      winner.setMoney(winner.getMoney() - auction.getMaxBid());
    } else {
      winner = this.usersRepository.getById(buyNowId);
      winner.setMoney(winner.getMoney() - auction.getBuyMeNow());
    }

    Project project = this.projectsRepository.getById(auction.getProjectId());
    project.setCreator(winner.getId());
    this.addMember(project, winner);

    // Sends messages and notifications to both winner and previous owner.
    Message messageToInsertCreator = this.writeMessage(auction.getTitle(),
            winner,
            this.usersRepository.getById(auction.getCreatorId()),
            "finishAuctionMessageCreator",
            auction.getMaxBid());
    this.messagesRepository.create(messageToInsertCreator);

    Message messageToInsertWinner = this.writeMessage(auction.getTitle(),
            this.usersRepository.getById(auction.getCreatorId()),
            winner,
            "finishAuctionMessageWinner",
            auction.getMaxBid());
    this.messagesRepository.create(messageToInsertWinner);

    Notification notificationCreator = this
            .writeNotification(auction.getTitle(),
                    winner,
                    this.usersRepository.getById(auction.getCreatorId()),
                    "auction-finish-creator");
    this.notificationsRepository.create(notificationCreator);

    Notification notificationWinner = this.writeNotification(auction.getTitle(),
            this.usersRepository.getById(auction.getCreatorId()),
            winner,
            "auction-finish-winner");
    this.notificationsRepository.create(notificationWinner);

    for (int i = 0; i < auction.getBids().size(); i++) {
      this.bidsRepository.delete(auction.getBids().get(i));
    }

    this.auctionsRepository.delete(auction);
    this.usersRepository.update(user);
    this.usersRepository.update(winner);
    this.projectsRepository.update(project);

    this.auctionsBeingFinishedNow.remove(Integer.valueOf(id));

    return true;
  }

  private void addMember(Project project, User winner) {
    List<User> members = project.getMembers();
    boolean isAlreadyInProject = false;
    for (User member
            : members) {
      if (winner.getId() == member.getId()) {
        isAlreadyInProject = true;
        break;
      }
    }

    if (!isAlreadyInProject) {
      members.add(winner);
      project.setMembers(members);
    }
  }

  // Does the same but with buy now feature.
  // That's why it reuses finishAuction() method.
  @Override
  public boolean buyNowAuction(int userId, int auctionId) {
    if (!this.idValidator.isValid(auctionId)) {
      throw new InvalidParameterException("AuctionId is not valid");
    } else if (!this.idValidator.isValid(userId)) {
      throw new InvalidParameterException("UserId is not valid");
    }

    User user = this.usersRepository.getById(userId);
    Auction auction = this.auctionsRepository.getById(auctionId);
    if (user.getMoney() < auction.getBuyMeNow()) {
      return false;
    } else {
      this.finishAuction(auctionId, true, userId);
      return true;
    }
  }

  @Override
  public Auction deleteAuction(int auctionId) {
    if (!this.idValidator.isValid(auctionId)) {
      throw new InvalidParameterException("AuctionId is not valid");
    }

    // First deletes of all the bids.
    Auction auction = this.auctionsRepository.getById(auctionId);
    for (int i = 0; i < auction.getBids().size(); i++) {
      this.bidsRepository.delete(auction.getBids().get(i));
    }

    return this.auctionsRepository
            .delete(this.auctionsRepository.getById(auctionId));
  }

  @Override
  public ResponsePagination getAuctionPages() {
    List<Auction> auctions = this.auctionsRepository.getAll();

    int numberOfPages = (int) Math.ceil(auctions.size() / PAGE_LENGTH_DOUBLE);

    return new ResponsePagination(numberOfPages, auctions.size());
  }

  public boolean isUserAlreadyFollowing(Auction auction, User user) {
    for (int i = 0; i < auction.getUsersWatching().size(); i++) {
      if (auction.getUsersWatching().get(i).getId() == user.getId()) {
        return true;
      }
    }

    for (int i = 0; i < user.getWatchedAuctions().size(); i++) {
      if (user.getWatchedAuctions().get(i).getId() == auction.getId()) {
        return true;
      }
    }

    return false;
  }

  private boolean doesAuctionAlreadyExist(int projectId) {
    return this.auctionsRepository.getAll().stream()
            .anyMatch(auction -> auction.getProjectId() == projectId);
  }


  // Helper method to write a message
  private Message writeMessage(String projectName,
                               User sender,
                               User recipient,
                               String kind,
                               double amount) {
    Message result = new Message();
    result.setTimestamp(this.createTimestamp());
    result.setSender(sender);
    result.setRecipient(recipient);
    switch (kind) {
      case "finishAuctionMessageCreator":
        result.setTopic("Вашият търг за проект \""
                + projectName + "\" приключи.");
        result.setContent("Правата за вашия проект бяха"
                + " закупени за "
                + amount + "EUR от " + sender.getFirstName() + " "
                + sender.getLastName());
        result.setKind("rejectionMessage");
        break;
      case "finishAuctionMessageWinner":
        result.setTopic("Спечелен търг "
                + "за правата на проект \""
                + projectName + "\".");
        result.setContent("Вие успешно успяхте"
                + " да закупите правата "
                + "на проект за "
                + amount + "EUR създаден от "
                + sender.getFirstName() + " "
                + sender.getLastName());
        result.setKind("rejectionMessage");
        break;
      default:
        break;
    }

    return result;
  }

  // Helper method to write a notification
  private Notification writeNotification(String projectName,
                                         User sender,
                                         User recipient,
                                         String kind) {
    Notification result = new Notification();
    result.setTimestamp(this.createTimestamp());
    result.setSender(sender);
    result.setRecipient(recipient);

    switch (kind) {
      case "auction-finish-creator":
        result.setContent("Търгът за \""
                + projectName + "\" приключи");
        result.setKind(kind);
        break;
      case "auction-finish-winner":
        result.setContent("Търгът за \""
                + projectName + "\" приключи");
        result.setKind(kind);
        break;
      default:
        break;
    }

    return result;
  }

  // Helper method to create a timestamp from date and time.
  private String createTimestamp() {
    int year = Calendar.getInstance().get(Calendar.YEAR);
    int month = Calendar.getInstance().get(Calendar.MONTH);
    int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    int minutes = Calendar.getInstance().get(Calendar.MINUTE);
    return year + "." + month + "." + day + " " + hour + ":" + minutes;
  }
}
