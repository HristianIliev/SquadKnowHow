package squadknowhow.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignInWebController {
  @GetMapping("/sign-in")
  public String home() {
    return "sign-in";
  }
}
