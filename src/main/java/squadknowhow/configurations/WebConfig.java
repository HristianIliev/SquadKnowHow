package squadknowhow.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
  /**
   * Thymeleaf template resolver serving HTML 5.
   *
   * @return ClassLoaderTemplateResolver
   */
  @Bean
  @Description("Thymeleaf template resolver serving HTML 5")
  public ClassLoaderTemplateResolver templateResolver() {

    ClassLoaderTemplateResolver templateResolver =
            new ClassLoaderTemplateResolver();

    templateResolver.setPrefix("templates/");
    templateResolver.setCacheable(false);
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode("HTML5");
    templateResolver.setCharacterEncoding("UTF-8");

    return templateResolver;
  }

  /**
   * Thymeleaf template engine with Spring integration.
   *
   * @return SpringTemplateEngine
   */
  @Bean
  @Description("Thymeleaf template engine with Spring integration")
  public SpringTemplateEngine templateEngine() {

    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(templateResolver());

    return templateEngine;
  }

  /**
   * Thymeleaf view resolver.
   *
   * @return ViewResolver
   */
  @Bean
  @Description("Thymeleaf view resolver")
  public ViewResolver viewResolver() {

    ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();

    viewResolver.setTemplateEngine(templateEngine());
    viewResolver.setCharacterEncoding("UTF-8");

    return viewResolver;
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    //    registry.addViewController("/").setViewName("index");
  }
}
