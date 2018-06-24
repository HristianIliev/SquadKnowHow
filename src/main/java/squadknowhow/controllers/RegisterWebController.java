package squadknowhow.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterWebController {
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
}
