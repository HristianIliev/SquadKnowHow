package squadknowhow.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import squadknowhow.contracts.IAuctionsService;
import squadknowhow.contracts.IProfileService;
import squadknowhow.contracts.IProjectsService;
import squadknowhow.dbmodels.Auction;
import squadknowhow.dbmodels.User;

@Controller
public class AuctionsWebController {
  private final IProfileService profileService;
  private final IProjectsService projectsService;
  private final IAuctionsService auctionService;

  @Autowired
  public AuctionsWebController(IProfileService profileService,
                               IAuctionsService auctionsService,
                               IProjectsService projectsService) {
    this.profileService = profileService;
    this.auctionService = auctionsService;
    this.projectsService = projectsService;
  }

  @GetMapping("/auctions")
  public String groups(Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());

    model.addAttribute("user", user);

    return "auctions";
  }

  @GetMapping("/auction/{id}")
  public String auctionPage(@PathVariable("id") int id, Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());
    Auction auction = this.auctionService.getAuction(id);

    boolean isOwner = this.isUserOwner(user, auction);
    boolean isMember = this.isUserMember(user, auction);
    int workingOnProject = this.projectsService.getWorkingOnProject(auction.getProjectId());
    String sellerName = this.profileService.getUserName(auction.getCreatorId());

    model.addAttribute("user", user);
    model.addAttribute("auction", auction);
    model.addAttribute("isOwner", isOwner);
    model.addAttribute("isMember", isMember);
    model.addAttribute("workingOnProject", workingOnProject);
    model.addAttribute("sellerName", sellerName);
    model.addAttribute("description", this.projectsService.getDescription(auction.getProjectId()));

    return "auction-page";
  }

  @GetMapping("/auction-admin/{id}")
  public String auctionAdmin(@PathVariable("id") int id, Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());
    Auction auction = this.auctionService.getAuction(id);

    boolean isOwner = this.isUserOwner(user, auction);
    boolean isMember = this.isUserMember(user, auction);
    int workingOnProject = this.projectsService.getWorkingOnProject(auction.getProjectId());
    String sellerName = this.profileService.getUserName(auction.getCreatorId());

    model.addAttribute("user", user);
    model.addAttribute("auction", auction);
    model.addAttribute("isOwner", isOwner);
    model.addAttribute("isMember", isMember);
    model.addAttribute("workingOnProject", workingOnProject);
    model.addAttribute("sellerName", sellerName);
    model.addAttribute("description", this.projectsService.getDescription(auction.getProjectId()));

    return "auction-page-admin";
  }

  private boolean isUserMember(User user, Auction auction) {
    return this.projectsService.isUserMember(user, auction);
  }

  private boolean isUserOwner(User user, Auction auction) {
    return user.getId() == auction.getCreatorId();
  }
}
