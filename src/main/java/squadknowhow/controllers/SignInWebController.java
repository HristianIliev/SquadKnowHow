package squadknowhow.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignInWebController {
  @GetMapping("/sign-in")
  public String home() {
    return "sign-in";
  }

  @GetMapping("/forgot-password")
  public String forgotPassword() {
    return "forgot-password";
  }

  @GetMapping("/reset-password/{id}")
  public String resetPassword() {
    return "reset-password";
  }
}
