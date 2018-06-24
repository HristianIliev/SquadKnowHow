package squadknowhow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import squadknowhow.contracts.IProfileService;
import squadknowhow.dbmodels.User;

import java.security.Principal;

@Controller
public class PeopleWebController {
  private final IProfileService profileService;

  @Autowired
  public PeopleWebController(IProfileService profileService) {
    this.profileService = profileService;
  }

  @GetMapping("/people")
  public String home() {
    return "people";
  }

  @GetMapping("/users")
  public String users(Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());

    model.addAttribute("user", user);

    return "people-logged";
  }

  @GetMapping("/user/{id}")
  public String userProfile(@PathVariable("id") int id, Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());
    User profile = this.profileService.getUserById(id);

    if (user.getId() == profile.getId()) {
      return "redirect:/profile";
    }

    String previousEmployment = user.getPreviousEmploymentString();
    String profileSkills = user.getProfileSkills();
    String profileInterests = user.getProfileInterests();

    model.addAttribute("user", user);
    model.addAttribute("profile", profile);
    model.addAttribute("previousEmployment", previousEmployment);
    model.addAttribute("profileSkills", profileSkills);
    model.addAttribute("profileInterests", profileInterests);

    return "profile";
  }

  @GetMapping("/profile")
  public String myProfile(Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());

    String previousEmployment = user.getPreviousEmploymentString();
    String profileSkills = user.getProfileSkills();
    String profileInterests = user.getProfileInterests();

    model.addAttribute("user", user);
    model.addAttribute("previousEmployment", previousEmployment);
    model.addAttribute("profileSkills", profileSkills);
    model.addAttribute("profileInterests", profileInterests);

    return "my-profile";
  }

  @GetMapping("/edit-profile")
  public String editProfile(Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());

    model.addAttribute("user", user);

    return "/profile-edit";
  }
}
