package squadknowhow.restcontrollers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import squadknowhow.contracts.IProjectsService;
import squadknowhow.dbmodels.Project;
import squadknowhow.request.models.EditedProject;
import squadknowhow.request.models.SentAdvice;
import squadknowhow.request.models.SentMessage;
import squadknowhow.request.models.SentQuestion;
import squadknowhow.response.models.ResponseCheckProjectName;
import squadknowhow.response.models.ResponsePagination;
import squadknowhow.response.models.ResponseProjectId;
import squadknowhow.response.models.ResponseProjects;
import squadknowhow.response.models.ResponseSuccessful;
import squadknowhow.response.models.ResponseUpload;
import squadknowhow.response.models.UserProjects;

@RestController
@RequestMapping("/api")
public class ProjectsController {
  private IProjectsService projectsService;

  @Autowired
  public ProjectsController(IProjectsService projectsService) {
    this.projectsService = projectsService;
  }

  @RequestMapping(value = "/addProjectVisit", method = RequestMethod.PUT)
  @ResponseBody
  public ResponseSuccessful addVisit(@RequestParam("projectId") int projectId) {
    return this.projectsService.addVisit(projectId);
  }

  @RequestMapping(value = "/removeProjectMember", method = RequestMethod.PUT)
  @ResponseBody
  public ResponseSuccessful removeProjectMember(@RequestParam("projectId") int projectId,
                                                @RequestParam("memberId") int memberId) {
    return this.projectsService.removeProjectMember(projectId, memberId);
  }

  @RequestMapping(value = "/deleteProject", method = RequestMethod.DELETE)
  @ResponseBody
  public ResponseSuccessful deleteProject(@RequestParam("projectId") int projectId) {
    return this.projectsService.deleteProject(projectId);
  }

  @RequestMapping(value = "/getProjectsOfUser", method = RequestMethod.GET)
  @ResponseBody
  public UserProjects getProjectsOfUser(@RequestParam("userId") int userId) {
    return this.projectsService.getProjectsOfUser(userId);
  }

  @RequestMapping(value = "/addProjectMember")
  @ResponseBody
  public ResponseSuccessful addProjectMember(@RequestParam("projectName") String projectName,
                                             @RequestParam("newMemberId") int newMemberId,
                                             @RequestParam("messageId") int messageId) {
    return this.projectsService.addProjectMember(projectName, newMemberId, messageId);
  }

  @RequestMapping(value = "/sendRejectMessage")
  @ResponseBody
  public ResponseSuccessful sendRejectMessage(@RequestParam("newMemberId") int newMemberId,
                                              @RequestParam("projectName") String projectName,
                                              @RequestParam("messageId") int messageId,
                                              @RequestParam("creatorId") int creatorId) {
    return this.projectsService.sendRejectMessage(newMemberId, projectName, messageId, creatorId);
  }

  @RequestMapping(value = "/sendMessageForApproval")
  @ResponseBody
  public ResponseSuccessful sendMessageForApproval(@RequestParam("projectId") int projectId,
                                                   @RequestParam("newMemberId") int newMemberId,
                                                   @RequestParam("creatorId") int creatorId) {
    return this.projectsService.sendMessageForApproval(projectId, newMemberId, creatorId);
  }

  @RequestMapping(value = "/projects")
  @ResponseBody
  public ResponseProjects getProjects(@RequestParam("page") int page,
                                      @RequestParam("name") String name,
                                      @RequestParam("userCategory") String userCategory,
                                      @RequestParam("city") String city) {
    return new ResponseProjects(this.projectsService.getProjects(page, name, userCategory, city));
  }

  @RequestMapping(value = "/createProject", method = RequestMethod.POST)
  @ResponseBody
  public ResponseProjectId createProject(@RequestBody Project project,
                                         @RequestParam("creatorId") int creatorId) {
    System.out.println(project.isNeedsMoney());
    System.out.println(project.getNeededMoney());
    return this.projectsService.createProject(project, creatorId);
  }

  @RequestMapping(value = "/uploadProjectPicture", method = RequestMethod.POST)
  @ResponseBody
  public ResponseUpload uploadProjectPicture(@RequestParam("qqfile") MultipartFile file,
                                             @RequestParam("qquuid") String uuid,
                                             @RequestParam("qqfilename") String fileName,
                                             @RequestParam(value = "qqpartindex",
                                                     required = false,
                                                     defaultValue = "-1") int partIndex,
                                             @RequestParam(value = "qqtotalparts",
                                                     required = false,
                                                     defaultValue = "-1") int totalParts,
                                             @RequestParam(value = "qqtotalfilesize",
                                                     required = false,
                                                     defaultValue = "-1") long totalFileSize,
                                             @RequestParam("id") int id) throws IOException {
    System.out.println(fileName);
    if (!fileName.equals("0")) {
      return this.projectsService.uploadPictures(file, id, fileName);
    }

    return this.projectsService.uploadImage(file, id);
  }

  @RequestMapping(value = "/getProjectPages")
  @ResponseBody
  public ResponsePagination getProjectPages(@RequestParam("name") String name,
                                            @RequestParam("userCategory") String userCategory,
                                            @RequestParam("city") String city) {
    return this.projectsService.getProjectsPages(name, userCategory, city);
  }

  @RequestMapping(value = "/checkProjectName")
  @ResponseBody
  public ResponseCheckProjectName checkProjectName(@RequestParam("name") String name) {
    return this.projectsService.checkProjectName(name);
  }

  @RequestMapping(value = "/project", method = RequestMethod.GET)
  @ResponseBody
  public Project getProject(@RequestParam("id") int id) {
    return this.projectsService.getProjectById(id);
  }

  @RequestMapping(value = "/sendMessageToAllMembers", method = RequestMethod.POST)
  public ResponseSuccessful sendMessageToAllMembers(@RequestParam("projectId") int projectId,
                                                    @RequestBody SentMessage message) {
    return this.projectsService.sendMessageToAllMembers(projectId, message);
  }

  @RequestMapping(value = "/sendAdvice", method = RequestMethod.POST)
  public ResponseSuccessful sendAdvice(@RequestBody SentAdvice advice) {
    return this.projectsService.sendAdvice(advice);
  }

  @RequestMapping(value = "/sendQuestion", method = RequestMethod.POST)
  public ResponseSuccessful sendQuestion(@RequestBody SentQuestion question) {
    return this.projectsService.sendQuestion(question);
  }

  @RequestMapping(value = "/editProject", method = RequestMethod.POST)
  public ResponseSuccessful editProject(@RequestBody EditedProject project) {
    return this.projectsService.editProject(project);
  }
}
