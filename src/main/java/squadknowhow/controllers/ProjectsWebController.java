package squadknowhow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import squadknowhow.contracts.IProfileService;
import squadknowhow.contracts.IProjectsService;
import squadknowhow.dbmodels.Project;
import squadknowhow.dbmodels.User;

import java.security.Principal;

@Controller
public class ProjectsWebController {
  private final IProfileService profileService;
  private final IProjectsService projectsService;

  @Autowired
  public ProjectsWebController(IProfileService profileService,
                               IProjectsService projectsService) {
    this.profileService = profileService;
    this.projectsService = projectsService;
  }

  @GetMapping("/projects")
  public String home() {
    return "projects";
  }

  @GetMapping("/projects-of-people")
  public String projectsOfPeople(Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());

    model.addAttribute("user", user);

    return "projects-logged";
  }

  @GetMapping("/project/{id}")
  public String projectPage(@PathVariable("id") int id, Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());
    Project project = this.projectsService.getProjectById(id);

    model.addAttribute("user", user);
    model.addAttribute("project", project);

    return "project-page";
  }

  @GetMapping("/project-admin/{id}")
  public String projectAdmin(@PathVariable("id") int id, Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());
    Project project = this.projectsService.getProjectById(id);

    model.addAttribute("user", user);
    model.addAttribute("project", project);

    return "project-page-admin";
  }

  @GetMapping("/create-project")
  public String createProject(Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());

    model.addAttribute("user", user);

    return "create-project";
  }

  @GetMapping("/my-projects")
  public String myProjects(Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());

    model.addAttribute("user", user);

    return "my-projects";
  }

  @GetMapping("/edit-project/{id}")
  public String editProject(@PathVariable("id") int id, Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());
    Project project = this.projectsService.getProjectById(id);

    model.addAttribute("user", user);
    model.addAttribute("project", project);

    return "project-edit";
  }

  @GetMapping("/project-member/{id}")
  public String projectMember(@PathVariable("id") int id, Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());
    Project project = this.projectsService.getProjectById(id);

    model.addAttribute("user", user);
    model.addAttribute("project", project);

    return "project-page-member";
  }
}
