package squadknowhow.dbmodels;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import squadknowhow.contracts.Model;

@Entity
@Table(name = "bids")
public class Bid implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "amount")
  private double amount;
  @ManyToOne
  @JsonIgnore
  private User bidder;
  @ManyToOne
  @JsonBackReference
  private Auction auction;
  @Column(name = "date")
  private String date;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public User getBidder() {
    return bidder;
  }

  public void setBidder(User bidder) {
    this.bidder = bidder;
  }

  public Auction getAuction() {
    return auction;
  }

  public void setAuction(Auction auction) {
    this.auction = auction;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getHiddenName() {
    StringBuilder name = new StringBuilder(
            this.getBidder().getFirstName() + " "
                    + this.getBidder().getLastName());

    for (int i = 1; i < name.length() - 1; i++) {
      name.replace(i, i + 1, "*");
    }

    return name.toString();
  }
}
