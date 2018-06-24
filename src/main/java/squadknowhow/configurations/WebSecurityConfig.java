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
import squadknowhow.contracts.IProfileService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private IProfileService usersService;

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
                    "/api/**",
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
                    "http://0c8cc78a.ngrok.io/static/xml/test.xml")
            .permitAll()
            .antMatchers(HttpMethod.POST, "/api/register")
            .permitAll()
            .antMatchers(HttpMethod.POST, "http://0c8cc78a.ngrok.io/static/xml/test.xml")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
            .defaultSuccessUrl("/profile", true)
            .loginPage("/sign-in")
            .permitAll()
            .and()
            .logout()
            .permitAll();
  }

//  @Override
//  public void configure(WebSecurity web) throws Exception {
//    web.ignoring().antMatchers("/api/register", "/api/createArena");
//  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(usersService)
            .passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
