package squadknowhow.contracts;

import squadknowhow.dbmodels.Auction;
import squadknowhow.request.models.AuctionData;
import squadknowhow.response.models.ResponseCreateAuction;
import squadknowhow.response.models.ResponsePagination;

import java.text.ParseException;
import java.util.List;

public interface IAuctionsService {
  List<Auction> getAuctions(String sortBy, int page);

  ResponseCreateAuction createAuction(
          AuctionData auctionData) throws ParseException;

  boolean startFollowingAuction(int auctionId, int userId);

  boolean stopFollowingAuction(int auctionId, int userId);

  List<Integer> getFollowingAuctions(int userId);

  Auction getAuction(int id);

  String getEndDate(int id);

  boolean createBid(int userId, int auctionId, double amount);

  boolean finishAuction(int id, boolean isItFromBuyNow, int buyNowId);

  boolean buyNowAuction(int userId, int auctionId);

  Auction deleteAuction(int auctionId);

  ResponsePagination getAuctionPages();
}
