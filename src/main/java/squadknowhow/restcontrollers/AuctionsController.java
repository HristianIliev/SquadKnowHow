package squadknowhow.restcontrollers;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import squadknowhow.contracts.IAuctionsService;
import squadknowhow.contracts.IProfileService;
import squadknowhow.dbmodels.Auction;
import squadknowhow.request.models.Amount;
import squadknowhow.request.models.AuctionData;
import squadknowhow.response.models.ResponseCreateAuction;
import squadknowhow.response.models.ResponsePagination;

@RestController
@RequestMapping("/api")
public class AuctionsController {
  private final IAuctionsService auctionsService;
  private final IProfileService profileService;

  @Autowired
  public AuctionsController(IAuctionsService auctionsService,
                            IProfileService profileService) {
    this.auctionsService = auctionsService;
    this.profileService = profileService;
  }

  @RequestMapping(value = "/getAuctions", method = RequestMethod.GET)
  @ResponseBody
  public List<Auction> getEvents(@RequestParam("sortBy") String sortBy,
                                 @RequestParam("page") int page) {
    return this.auctionsService.getAuctions(sortBy, page);
  }

  @RequestMapping(value = "/getAuctionPages")
  @ResponseBody
  public ResponsePagination getAuctionPages() {
    return this.auctionsService.getAuctionPages();
  }

  @RequestMapping(value = "/createAuction", method = RequestMethod.POST)
  @ResponseBody
  public ResponseCreateAuction createAuction(
          @RequestBody AuctionData auctionData) throws ParseException {
    return this.auctionsService.createAuction(auctionData);
  }

  @RequestMapping(value = "/startFollowingAuction", method = RequestMethod.GET)
  @ResponseBody
  public boolean startFollowingAuction(@RequestParam("auctionId") int auctionId,
                                       @RequestParam("userId") int userId) {
    return this.auctionsService.startFollowingAuction(auctionId, userId);
  }

  @RequestMapping(value = "/stopFollowingAuction", method = RequestMethod.GET)
  @ResponseBody
  public boolean stopFollowingAuction(@RequestParam("auctionId") int auctionId,
                                      @RequestParam("userId") int userId) {
    return this.auctionsService.stopFollowingAuction(auctionId, userId);
  }

  @RequestMapping(value = "/getFollowingAuctions", method = RequestMethod.GET)
  @ResponseBody
  public List<Integer> getFollowingAuctions(
          @RequestParam("userId") int userId) {
    return this.auctionsService.getFollowingAuctions(userId);
  }

  @RequestMapping(value = "/getEndDate", method = RequestMethod.GET)
  @ResponseBody
  public String getEndDate(@RequestParam("auctionId") int id) {
    return this.auctionsService.getEndDate(id);
  }

  @RequestMapping(value = "/createBid", method = RequestMethod.POST)
  @ResponseBody
  public boolean createBid(@RequestParam("userId") int userId,
                           @RequestParam("auctionId") int auctionId,
                           @RequestBody Amount amount) {
    System.out.println(amount.getAmount());
    return this.auctionsService.createBid(userId,
            auctionId,
            amount.getAmount());
  }

  @RequestMapping(value = "/finishAuction", method = RequestMethod.GET)
  public void finishAuction(@RequestParam("auctionId") int id) {
    this.auctionsService.finishAuction(id, false, 0);
  }

  @RequestMapping(value = "/buyNowAuction", method = RequestMethod.GET)
  public boolean buyNowAuction(@RequestParam("userId") int userId,
                               @RequestParam("auctionId") int auctionId) {
    return this.auctionsService.buyNowAuction(userId, auctionId);
  }

  @RequestMapping(value = "/deleteAuction", method = RequestMethod.DELETE)
  public void deleteAuction(@RequestParam("auctionId") int auctionId) {
    this.auctionsService.deleteAuction(auctionId);
  }
}
