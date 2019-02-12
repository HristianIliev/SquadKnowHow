package squadknowhow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import squadknowhow.contracts.IProfileService;
import squadknowhow.dbmodels.User;

import java.security.Principal;

@Controller
public class MessagesWebController {
  private final IProfileService profileService;

  @Autowired
  public MessagesWebController(IProfileService profileService) {
    this.profileService = profileService;
  }

  @GetMapping("/messages")
  public String messages(Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());

    model.addAttribute("user", user);

    return "my-messages";
  }
}
