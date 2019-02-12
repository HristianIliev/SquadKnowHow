package squadknowhow.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import squadknowhow.contracts.IProfileService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private IProfileService usersService;
  @Autowired
  private LogoutHandler logoutSuccessHandler;
  @Autowired
  private AuthenticationSuccessHandler authenticationSuccessHandler;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(usersService);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();

    http
            .authorizeRequests()
            .antMatchers("/",
                    "/css/**",
                    "/static/**",
//                    "/api/**",
                    "/api/contact",
                    "/api/getCoordinates",
                    "/api/getPotentialCandidates",
                    "/api/getCities",
                    "/api/getSkills",
                    "/api/getLanguages",
                    "/api/getPeoplePages",
                    "/api/users",
                    "/api/getProjectPages",
                    "/api/projects",
                    "/api/getGroupPages",
                    "/api/groups",
                    "/api/checkIfEmailIsTaken",
                    "/api/uploadProfilePicture",
                    "/api/register",
                    "/api/areTagsCorrect",
                    "/api/getEmployedBy",
                    "/api/getInterests",
                    "/api/reset-password",
                    "/api/forgot-password",
                    "/home",
                    "/",
                    "/people",
                    "/projects",
                    "/groups",
                    "/register",
                    "/register-developer",
                    "/register-engineer",
                    "/register-designer",
                    "/register-scientist",
                    "/register-musician",
                    "/register-artist",
                    "/register-writer",
                    "/register-filmmaker",
                    "/register-product-manager",
                    "/forgot-password",
                    "/reset-password/{id}",
                    "/termsAndConditions",
                    "/activation")
            .permitAll()
            .antMatchers(HttpMethod.POST, "/api/register")
            .permitAll()
            .antMatchers(HttpMethod.POST,
                    "http://0c8cc78a.ngrok.io/static/xml/test.xml")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
            .successHandler(authenticationSuccessHandler)
//            .successForwardUrl("/profile")
//            .defaultSuccessUrl("/profile")
            .loginPage("/sign-in")
            .permitAll()
            .and()
            .logout()
            .logoutSuccessUrl("/sign-in?logout")
            .addLogoutHandler(logoutSuccessHandler)
            .permitAll()
            .and()
            .rememberMe().key("SquadKnowHow");
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth)
          throws Exception {
    auth.userDetailsService(usersService)
            .passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
