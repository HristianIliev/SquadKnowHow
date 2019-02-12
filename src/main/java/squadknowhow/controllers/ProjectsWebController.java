package squadknowhow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import squadknowhow.contracts.IProfileService;
import squadknowhow.contracts.IProjectsService;
import squadknowhow.dbmodels.Project;
import squadknowhow.dbmodels.Update;
import squadknowhow.dbmodels.User;

import java.security.Principal;
import java.util.List;

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
  public String projectPage(@PathVariable("id") int id,
                            Model model,
                            Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());
    Project project = this.projectsService.getProjectById(id);

    if (this.isAdmin(user, project)) {
      return "redirect:" + "/project-admin/" + project.getId();
    }

    if (this.isMember(user, project)) {
      return "redirect:" + "/project-member/" + project.getId();
    }

    boolean isFavorite = false;
    for (int i = 0; i < user.getFavoriteProjects().size(); i++) {
      if (user.getFavoriteProjects().get(i).getId() == project.getId()) {
        isFavorite = true;
      }
    }

    boolean isBacker = false;
    for (Project backedProject :
            user.getBackedProjects()) {
      if (backedProject.getId() == project.getId()) {
        isBacker = true;
      }
    }

    List<Update> updates = project.getUpdates();
    this.orderUpdates(updates);
    boolean isEmpty = false;
    if (updates.size() == 0) {
      isEmpty = true;
    }

    model.addAttribute("isEmpty", isEmpty);
    model.addAttribute("updates", updates);
    model.addAttribute("pageUrl", "http://localhost:3000/project/" + id);
    model.addAttribute("pageIdentifier", "project" + id);
    model.addAttribute("pageTitle", project.getName());
    model.addAttribute("isBacker", isBacker);
    model.addAttribute("isFavorite", isFavorite);
    model.addAttribute("user", user);
    model.addAttribute("project", project);

    return "project-page";
  }

  private void orderUpdates(List<Update> updates) {
    updates.sort((o1, o2) -> {
      if (o1.getDateObj() == null || o2.getDateObj() == null)
        return 0;
      return o2.getDateObj().compareTo(o1.getDateObj());
    });
  }

  @GetMapping("/project-admin/{id}")
  public String projectAdmin(@PathVariable("id") int id,
                             Model model,
                             Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());
    Project project = this.projectsService.getProjectById(id);

    if (!this.isAdmin(user, project)) {
      if (!this.isMember(user, project)) {
        return "redirect:" + "/project/" + project.getId();
      } else {
        return "redirect:" + "/project-member/" + project.getId();
      }
    }

    boolean isBacker = false;
    for (Project backedProject :
            user.getBackedProjects()) {
      if (backedProject.getId() == project.getId()) {
        isBacker = true;
      }
    }

    List<Update> updates = project.getUpdates();
    this.orderUpdates(updates);

    boolean isEmpty = false;
    if (updates.size() == 0) {
      isEmpty = true;
    }

    model.addAttribute("isEmpty", isEmpty);
    model.addAttribute("updates", updates);
    model.addAttribute("pageUrl", "http://localhost:3000/project-admin/" + id);
    model.addAttribute("pageIdentifier", "project-admin" + id);
    model.addAttribute("pageTitle", project.getName());
    model.addAttribute("isBacker", isBacker);
    model.addAttribute("user", user);
    model.addAttribute("project", project);

    return "project-page-admin";
  }

  private boolean isMember(User user, Project project) {
    if (user.getId() == project.getCreator()) {
      return false;
    } else {
      for (Project project1 :
              user.getProjects()) {
        if (project1.getId() == project.getId()) {
          return true;
        }
      }
    }

    return false;
  }

  private boolean isAdmin(User user, Project project) {
    return user.getId() == project.getCreator();
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
  public String editProject(@PathVariable("id") int id,
                            Model model,
                            Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());
    Project project = this.projectsService.getProjectById(id);

    model.addAttribute("user", user);
    model.addAttribute("project", project);

    return "project-edit";
  }

  @GetMapping("/project-member/{id}")
  public String projectMember(@PathVariable("id") int id,
                              Model model,
                              Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());
    Project project = this.projectsService.getProjectById(id);

    if (this.isAdmin(user, project)) {
      return "redirect:" + "/project-admin/" + project.getId();
    }

    if (!this.isMember(user, project)) {
      return "redirect:" + "/project/" + project.getId();
    }

    boolean isBacker = false;
    for (Project backedProject :
            user.getBackedProjects()) {
      if (backedProject.getId() == project.getId()) {
        isBacker = true;
      }
    }

    List<Update> updates = project.getUpdates();
    this.orderUpdates(updates);
    boolean isEmpty = false;
    if (updates.size() == 0) {
      isEmpty = true;
    }

    model.addAttribute("isEmpty", isEmpty);
    model.addAttribute("updates", updates);
    model.addAttribute("pageUrl", "http://localhost:3000/project-member/" + id);
    model.addAttribute("pageIdentifier", "project-member" + id);
    model.addAttribute("pageTitle", project.getName());
    model.addAttribute("isBacker", isBacker);

    model.addAttribute("user", user);
    model.addAttribute("project", project);

    return "project-page-member";
  }

  @GetMapping("/presentation/{id}")
  public String projectPresentation() {
    return "presentation";
  }
}
