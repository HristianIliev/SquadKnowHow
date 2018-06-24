package squadknowhow.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import squadknowhow.contracts.ILoginService;
import squadknowhow.request.models.LoginForm;
import squadknowhow.response.models.ResponseLogin;

@RestController
@RequestMapping("/api")
public class LoginController {
  private ILoginService loginService;

  @Autowired
  public LoginController(ILoginService loginService) {
    this.loginService = loginService;
  }

//  @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
//  @ResponseBody
//  public ResponseLogin register(@RequestBody LoginForm loginForm) {
//    return this.loginService.attemptLogin(loginForm.getEmail(), loginForm.getPassword());
//  }
}
