package squadknowhow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import squadknowhow.contracts.IGroupsService;
import squadknowhow.contracts.IProfileService;
import squadknowhow.dbmodels.Group;
import squadknowhow.dbmodels.Project;
import squadknowhow.dbmodels.User;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GroupsWebController {
  private final IProfileService profileService;
  private final IGroupsService groupsService;

  @Autowired
  public GroupsWebController(IProfileService profileService,
                             IGroupsService groupsService) {
    this.profileService = profileService;
    this.groupsService = groupsService;
  }

  @GetMapping("/groups")
  public String groups() {
    return "groups";
  }

  @GetMapping("/groups-of-people")
  public String groupsOfPeople(Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());

    model.addAttribute("user", user);

    return "groups-logged";
  }

  @GetMapping("/create-group")
  public String createGroup(Model model, Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());

    model.addAttribute("user", user);

    return "create-group.html";
  }

  @GetMapping("/group/{id}")
  public String groupPage(@PathVariable("id") int id,
                          Model model,
                          Principal principal) {
    User user = this.profileService.getUserByEmail(principal.getName());
    Group group = this.groupsService.getGroupById(id);

    model.addAttribute("user", user);
    model.addAttribute("group", group);
    model.addAttribute("allProjects", this.getAllProjects(group));

    return "group-page";
  }

  private List<Project> getAllProjects(Group group) {
    List<Project> result = new ArrayList<>();
    for (User member
            : group.getMembers()) {
      for (Project project
              : member.getProjects()) {
        boolean doesExist = false;
        for (Project existingProject
                : result) {
          if (existingProject.getId() == project.getId()) {
            doesExist = true;
          }
        }

        if (!doesExist) {
          result.add(project);
        }
      }
    }

    return result;
  }
}
