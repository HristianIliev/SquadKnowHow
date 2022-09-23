package squadknowhow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import squadknowhow.contracts.IProfileService;
import squadknowhow.dbmodels.Project;
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

    String previousEmployment = profile.getPreviousEmploymentString();
    String profileSkills = profile.getProfileSkills();
    String profileInterests = profile.getProfileInterests();
    String profileLanguages = profile.getProfileLanguages();

    model.addAttribute("user", user);
    model.addAttribute("profile", profile);
    model.addAttribute("reviews", profile.getReviews().size());
    model.addAttribute("previousEmployment", previousEmployment);
    model.addAttribute("profileSkills", profileSkills);
    model.addAttribute("profileInterests", profileInterests);
    model.addAttribute("profileLanguages", profileLanguages);
    model.addAttribute("profileClass", "profileScientist");

    return "profile";
  }

  @GetMapping("/profile")
  public String myProfile(Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());

    String previousEmployment = user.getPreviousEmploymentString();
    String profileSkills = user.getProfileSkills();
    String profileInterests = user.getProfileInterests();
    String profileLanguages = user.getProfileLanguages();

    String isZero = "notGreenClass";
    if (user.calculateSuccessPercentage() == 0) {
      isZero = "greenClass";
    }

    model.addAttribute("isZero", isZero);
    model.addAttribute("user", user);
    model.addAttribute("reviews", user.getReviews().size());
    model.addAttribute("previousEmployment", previousEmployment);
    model.addAttribute("profileSkills", profileSkills);
    model.addAttribute("profileInterests", profileInterests);
    model.addAttribute("profileLanguages", profileLanguages);
    model.addAttribute("profileClass", "profileScientist");

    return "my-profile";
  }

  private String getProfileClass(User user) {
    switch (user.getSkillset().getName()) {
      case "Programmer":
        return "profileProgrammer";
      case "Engineer":
        return "profileEngineer";
      case "Designer":
        return "profileDesigner";
      case "Scientist":
        return "profileScientist";
      case "Musician":
        return "profileMusician";
      case "Artis":
        return "profileArtist";
      case "Writer":
        return "profileWriter";
      case "Filmmaker":
        return "profileFilmmaker";
      case "Product Manager":
        return "profileProductManager";
      default:
        return "";
    }
  }

  @GetMapping("/edit-profile")
  public String editProfile(Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());

    boolean hasImage = false;
    if (!user.getImage().equals("") && user.getImage() != null) {
      hasImage = true;
    }

    boolean isCreator = false;
    for (Project project :
            user.getProjects()) {
      if (project.getCreator() == user.getId()) {
        isCreator = true;
      }
    }

    String city = "";
    if (user.getCity() != null) {
      city = user.getCity().getName();
    }

    String employer = "";
    if (user.getEmployer() != null) {
      employer = user.getEmployer().getName();
    }

    model.addAttribute("user", user);
    model.addAttribute("skills", this.getCorrectString(user.getProfileSkills()));
    model.addAttribute("interests", this.getCorrectString(user.getProfileInterests()));
    model.addAttribute("languages", this.getCorrectString(user.getProfileLanguages()));
    model.addAttribute("previousEmployment", this.getCorrectString(user.getPreviousEmploymentString()));
    model.addAttribute("hasImage", hasImage);
    model.addAttribute("isCreator", isCreator);
    model.addAttribute("city", city);
    model.addAttribute("employer", employer);

    return "/profile-edit";
  }

  private String getCorrectString(String profileInfo) {
    if (profileInfo.equals("No provided interests") ||
            profileInfo.equals("No provided skills") ||
            profileInfo.equals("No previous work places") ||
            profileInfo.equals("No previous languages")) {
      return "";
    }

    return profileInfo;
  }

  @GetMapping("/notes")
  public String notes(Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());

    model.addAttribute("user", user);
    return "/notes";
  }
}
