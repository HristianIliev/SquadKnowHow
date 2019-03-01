package squadknowhow.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import squadknowhow.contracts.IProfileService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {

  @Autowired
  private IProfileService profileService;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    this.profileService.setOffline(authentication.getName());

    new SecurityContextLogoutHandler().logout(request, response, authentication);
  }
}
