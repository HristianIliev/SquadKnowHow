package squadknowhow.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeWebController {
  @GetMapping("/")
  public String index() {
    return "index";
  }
  
  @GetMapping("/home")
  public String home() {
    return "index";
  }
}
