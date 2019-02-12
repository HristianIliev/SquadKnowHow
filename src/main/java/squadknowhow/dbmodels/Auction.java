package squadknowhow.dbmodels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.jsoup.Jsoup;
import squadknowhow.contracts.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Model class for 'auctions' table.
 */
@Entity
@Table(name = "auctions")
public class Auction implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "title")
  private String title;
  @Column(name = "description")
  private String description;
  @Column(name = "picture")
  private String picture;
  @Column(name = "location")
  private String location;
  @Column(name = "end_date")
  private String endDate;
  @Column(name = "projectId")
  private int projectId;
  @OneToMany(mappedBy = "auction")
  @JsonManagedReference
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<Bid> bids;
  @ManyToMany()
  @LazyCollection(LazyCollectionOption.FALSE)
  @JoinTable(name = "auctions_users_watching",
          joinColumns = {@JoinColumn(name = "auction_id")},
          inverseJoinColumns = {
                  @JoinColumn(name = "user_id")})
  @JsonIgnore
  private List<User> usersWatching;
  @Column(name = "creatorId")
  private int creatorId;
  @Column(name = "buy_me_now")
  private Double buyMeNow;
  @Column(name = "created_at")
  private String createdAt;

  @JsonProperty
  public int getUsersWatchingNumber() {
    return this.usersWatching.size();
  }

  @JsonProperty
  public String getStrippedDescription() {
    return Jsoup.parse(this.getDescription()).text();
  }

  @JsonIgnore
  public int getBidsCount() {
    return this.getBids().size();
  }

  @JsonIgnore
  public double getMaxBid() {
    double maxBid = 0;
    for (int i = 0; i < this.getBids().size(); i++) {
      if (this.getBids().get(i).getAmount() > maxBid) {
        maxBid = this.getBids().get(i).getAmount();
      }
    }

    return maxBid;
  }

  @JsonIgnore
  public Bid getMaxBidBid() {
    double maxBid = 0;
    int index = -1;
    for (int i = 0; i < this.getBids().size(); i++) {
      if (this.getBids().get(i).getAmount() > maxBid) {
        maxBid = this.getBids().get(i).getAmount();
        index = i;
      }
    }

    if (index != -1) {
      return this.getBids().get(index);
    } else {
      return null;
    }
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public List<Bid> getBids() {
    return bids;
  }

  public void setBids(List<Bid> bids) {
    this.bids = bids;
  }

  public List<User> getUsersWatching() {
    return usersWatching;
  }

  public void setUsersWatching(List<User> usersWatching) {
    this.usersWatching = usersWatching;
  }

  public int getProjectId() {
    return projectId;
  }

  public void setProjectId(int projectId) {
    this.projectId = projectId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Auction auction = (Auction) o;
    return getId() == auction.getId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  public int getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(int creatorId) {
    this.creatorId = creatorId;
  }

  public Double getBuyMeNow() {
    return buyMeNow;
  }

  public void setBuyMeNow(Double buyMeNow) {
    this.buyMeNow = buyMeNow;
  }

  public static final Comparator<Auction> TIME_ASC_COMPARATOR = (o1, o2) -> {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    Date first = new Date();
    Date second = new Date();

    try {
      first = sdf.parse(o1.getEndDate());
      second = sdf.parse(o2.getEndDate());
    } catch (ParseException e) {
      e.printStackTrace();
    }

    if (first.before(second)) {
      return 1;
    } else if (first.after(second)) {
      return -1;
    } else {
      return 0;
    }
  };

  public static final Comparator<Auction> TIME_DESC_COMPARATOR = (o1, o2) -> {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    Date first = new Date();
    Date second = new Date();

    try {
      first = sdf.parse(o1.getEndDate());
      second = sdf.parse(o2.getEndDate());
    } catch (ParseException e) {
      e.printStackTrace();
    }

    if (first.before(second)) {
      return -1;
    } else if (first.after(second)) {
      return 1;
    } else {
      return 0;
    }
  };

  public static final Comparator<Auction> PRICE_ASC_COMPARATOR = (o1, o2) -> {
    double highestBid1 = 0.00;
    for (int i = 0; i < o1.getBids().size(); i++) {
      if (o1.getBids().get(i).getAmount() > highestBid1) {
        highestBid1 = o1.getBids().get(i).getAmount();
      }
    }

    double highestBid2 = 0.00;
    for (int i = 0; i < o2.getBids().size(); i++) {
      if (o2.getBids().get(i).getAmount() > highestBid2) {
        highestBid2 = o2.getBids().get(i).getAmount();
      }
    }

    return Double.compare(highestBid1, highestBid2);
  };

  public static final Comparator<Auction> PRICE_DESC_COMPARATOR = (o1, o2) -> {
    double highestBid1 = 0.00;
    for (int i = 0; i < o1.getBids().size(); i++) {
      if (o1.getBids().get(i).getAmount() > highestBid1) {
        highestBid1 = o1.getBids().get(i).getAmount();
      }
    }

    double highestBid2 = 0.00;
    for (int i = 0; i < o2.getBids().size(); i++) {
      if (o2.getBids().get(i).getAmount() > highestBid2) {
        highestBid2 = o2.getBids().get(i).getAmount();
      }
    }

    return Double.compare(highestBid2, highestBid1);
  };

  public static final Comparator<Auction> BIDS_ASC_COMPARATOR = (o1, o2) -> {
    int bids1 = 0;
    for (int i = 0; i < o1.getBids().size(); i++) {
      bids1++;
    }

    int bids2 = 0;
    for (int i = 0; i < o2.getBids().size(); i++) {
      bids2++;
    }

    return Integer.compare(bids2, bids1);
  };

  public static final Comparator<Auction> BIDS_DESC_COMPARATOR = (o1, o2) -> {
    int bids1 = 0;
    for (int i = 0; i < o1.getBids().size(); i++) {
      bids1++;
    }

    int bids2 = 0;
    for (int i = 0; i < o2.getBids().size(); i++) {
      bids2++;
    }

    return Integer.compare(bids1, bids2);
  };

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public int getAuctionBidders() {
    int result = 0;
    List<Integer> idOfBidders = new ArrayList<>();
    for (int i = 0; i < this.getBids().size(); i++) {
      if (!idOfBidders.contains(this.getBids().get(i).getBidder().getId())) {
        idOfBidders.add(this.getBids().get(i).getBidder().getId());
        ++result;
      }
    }

    return result;
  }

  public int getDurationInDays() throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date now = new Date();
    Date end = dateFormat.parse(this.getEndDate());

    Calendar endCal = Calendar.getInstance();
    endCal.setTime(end);
    Calendar nowCal = Calendar.getInstance();
    nowCal.setTime(now);

    return endCal.get(Calendar.DAY_OF_YEAR) - nowCal.get(Calendar.DAY_OF_YEAR);
  }
}
