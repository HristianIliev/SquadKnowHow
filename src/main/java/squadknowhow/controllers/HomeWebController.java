package squadknowhow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import squadknowhow.contracts.IProfileService;
import squadknowhow.contracts.IProjectsService;
import squadknowhow.dbmodels.Project;

import java.util.List;

@Controller
public class HomeWebController {
  private final IProjectsService projectsService;
  private final IProfileService profileService;

  @Autowired
  public HomeWebController(IProjectsService projectsService,
                           IProfileService profileService) {
    this.projectsService = projectsService;
    this.profileService = profileService;
  }

  @GetMapping("/")
  public String index(Model model) {
    List<Project> favouriteProjects = this.projectsService.getFavouriteProjects();
    model.addAttribute("project1", favouriteProjects.get(0));
    model.addAttribute("project2", favouriteProjects.get(1));
    model.addAttribute("project3", favouriteProjects.get(2));
    model.addAttribute("project4", favouriteProjects.get(3));
    model.addAttribute("project5", favouriteProjects.get(4));

    return "index";
  }

  @GetMapping("/home")
  public String home(Model model) {
    List<Project> favouriteProjects = this.projectsService.getFavouriteProjects();
    model.addAttribute("project1", favouriteProjects.get(0));
    model.addAttribute("project2", favouriteProjects.get(1));
    model.addAttribute("project3", favouriteProjects.get(2));
    model.addAttribute("project4", favouriteProjects.get(3));
    model.addAttribute("project5", favouriteProjects.get(4));

    return "index";
  }
}
