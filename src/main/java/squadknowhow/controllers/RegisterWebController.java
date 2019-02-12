package squadknowhow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import squadknowhow.contracts.IRegistrationService;

@Controller
public class RegisterWebController {
  private IRegistrationService registrationService;

  @Autowired
  public RegisterWebController(IRegistrationService registrationService) {
    this.registrationService = registrationService;
  }

  @GetMapping("/register-developer")
  public String registerDeveloper() {
    return "register-developer";
  }

  @GetMapping("/register-engineer")
  public String registerEngineer() {
    return "register-engineer";
  }

  @GetMapping("/register-designer")
  public String registerDesigner() {
    return "register-designer";
  }

  @GetMapping("/register-scientist")
  public String registerScientist() {
    return "register-scientist";
  }

  @GetMapping("/register-musician")
  public String registerMusician() {
    return "register-musician";
  }

  @GetMapping("/register-artist")
  public String registerArtist() {
    return "register-artist";
  }

  @GetMapping("/register-filmmaker")
  public String registerFilmmaker() {
    return "register-filmmaker";
  }

  @GetMapping("/register-writer")
  public String registerWriter() {
    return "register-writer";
  }

  @GetMapping("/register-product-manager")
  public String registerProductManager() {
    return "register-product-manager";
  }

  @GetMapping("/register")
  public String register() {
    return "register";
  }

  @GetMapping("/termsAndConditions")
  public String termsAndConditions() {
    return "termsAndConditions";
  }

  @GetMapping("/activation")
  public String activation(@RequestParam(value = "activationKey",
          required = false) String activationKey,
                           Model model) {
    if (activationKey != null) {
      System.out.println("Activation key is " + activationKey);
      model.addAttribute("successfullyActivated",
              this.checkActivationKey(activationKey));
    }

    return "activation";
  }

  private boolean checkActivationKey(String activationKey) {
    return this.registrationService.checkActivationKey(activationKey);
  }
}
