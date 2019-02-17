package squadknowhow.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import squadknowhow.contracts.IChatbotResponse;
import squadknowhow.contracts.IProjectsService;
import squadknowhow.dbmodels.Project;
import squadknowhow.dbmodels.Update;
import squadknowhow.dbmodels.User;
import squadknowhow.dbmodels.UserCategory;
import squadknowhow.dbmodels.WantedMember;
import squadknowhow.request.models.CreateProject;
import squadknowhow.request.models.DeleteProjectInfo;
import squadknowhow.request.models.SentAdvice;
import squadknowhow.request.models.SentMessage;
import squadknowhow.request.models.SentQuestion;
import squadknowhow.response.models.Coordinate;
import squadknowhow.response.models.FineUploaderImage;
import squadknowhow.response.models.ProjectWithName;
import squadknowhow.response.models.ResponseCheckProjectName;
import squadknowhow.response.models.ResponsePagination;
import squadknowhow.response.models.ResponseProjectId;
import squadknowhow.response.models.ResponseProjects;
import squadknowhow.response.models.ResponseSuccessful;
import squadknowhow.response.models.ResponseUpload;
import squadknowhow.response.models.ResponseVisits;
import squadknowhow.response.models.UserProjects;
import squadknowhow.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProjectsController {
  private IProjectsService projectsService;

  @Autowired
  public ProjectsController(IProjectsService projectsService) {
    this.projectsService = projectsService;
  }

  @RequestMapping(value = "/getPotentialCandidates", method = RequestMethod.GET)
  @ResponseBody
  public IChatbotResponse getPotentialCandidates(@RequestParam("projectName") String projectName) {
    System.out.println(projectName);
    return this.projectsService.getPotentialCandidates(projectName);
  }

  @RequestMapping(value = "/deleteProjectFile/{uuid}", method = RequestMethod.DELETE)
  @ResponseBody
  public int deleteProjectFile(@RequestParam("id") int projectId,
                               @RequestParam("pictureNum") int pictureNum,
                               @PathVariable String uuid) {
    return this.projectsService.deleteProjectFile(projectId, pictureNum);
  }

  @RequestMapping(value = "/getProjectImages", method = RequestMethod.GET)
  @ResponseBody
  public List<FineUploaderImage> getProjectImages(@RequestParam("id") int projectId) {
    return this.projectsService.getProjectImages(projectId);
  }

  @RequestMapping(value = "/getProjectNeeds", method = RequestMethod.GET)
  @ResponseBody
  public List<UserCategory> getUserCategories(@RequestParam("id") int id) {
    return this.projectsService.getProjectNeeds(id);
  }

  @RequestMapping(value = "/getWantedMembers", method = RequestMethod.GET)
  @ResponseBody
  public List<WantedMember> getWantedMembers(@RequestParam("id") int id) {
    return this.projectsService.getWantedMembers(id);
  }

  @RequestMapping(value = "/wantedMember", method = RequestMethod.GET)
  @ResponseBody
  public WantedMember wantedMember(@RequestParam("id") int id) {
    return this.projectsService.getWantedMember(id);
  }

  @RequestMapping(value = "/getProjectInformation", method = RequestMethod.GET)
  @ResponseBody
  public Project getProjectInformation(@RequestParam("projectId") int projectId) {
    return this.projectsService.getProjectById(projectId);
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

  @RequestMapping(value = "/deleteProject", method = RequestMethod.POST)
  @ResponseBody
  public ResponseSuccessful deleteProject(@RequestParam("projectId") int projectId,
                                          @RequestParam("isCompleted") boolean isCompleted,
                                          @RequestBody List<DeleteProjectInfo> deleteProjectInfos) {
    return this.projectsService.deleteProject(projectId, deleteProjectInfos, isCompleted);
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
                                             @RequestParam("messageId") int messageId,
                                             @RequestParam("isInvite") boolean isInvite) {
    return this.projectsService.addProjectMember(projectName, newMemberId, messageId, isInvite);
  }

  @RequestMapping(value = "/sendRejectMessage")
  @ResponseBody
  public ResponseSuccessful sendRejectMessage(@RequestParam("newMemberId") int newMemberId,
                                              @RequestParam("projectName") String projectName,
                                              @RequestParam("messageId") int messageId,
                                              @RequestParam("creatorId") int creatorId,
                                              @RequestParam("isInvite") boolean isInvite) {
    return this.projectsService.sendRejectMessage(newMemberId, projectName, messageId, creatorId, isInvite);
  }

  @RequestMapping(value = "/sendMessageForApproval")
  @ResponseBody
  public ResponseSuccessful sendMessageForApproval(@RequestParam("projectId") int projectId,
                                                   @RequestParam("newMemberId") int newMemberId,
                                                   @RequestParam("creatorId") int creatorId) {
    return this.projectsService.sendMessageForApproval(projectId,
            newMemberId,
            creatorId);
  }

  @RequestMapping(value = "/sendInviteMessage")
  @ResponseBody
  public ResponseSuccessful sendInviteMessage(@RequestParam("projectId") int projectId,
                                              @RequestParam("recipient") int recipient,
                                              @RequestParam("sender") int sender) {
    return this.projectsService.sendInviteMessage(projectId, recipient, sender);
  }

  @RequestMapping(value = "/createProject", method = RequestMethod.POST)
  @ResponseBody
  public ResponseProjectId createProject(@RequestBody CreateProject project,
                                         @RequestParam("creatorId") int creatorId) {
    System.out.println(project.getCity());
    System.out.println(project.getLatitude());
    System.out.println(project.getLongitude());
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
                                            @RequestParam("city") String city,
                                            @RequestParam("isByMap") boolean isByMap,
                                            @RequestParam("latitude") double latitude,
                                            @RequestParam("longitude") double longitude,
                                            @RequestParam("radius") int radius) {
    return this.projectsService.getProjectsPages(name, userCategory, city, isByMap, latitude, longitude, radius);
  }

  @RequestMapping(value = "/projects")
  @ResponseBody
  public ResponseProjects getProjects(@RequestParam("page") int page,
                                      @RequestParam("name") String name,
                                      @RequestParam("userCategory") String userCategory,
                                      @RequestParam("city") String city,
                                      @RequestParam("isByMap") boolean isByMap,
                                      @RequestParam("latitude") double latitude,
                                      @RequestParam("longitude") double longitude,
                                      @RequestParam("radius") int radius) {
    return new ResponseProjects(this.projectsService.getProjects(page, name, userCategory, city, isByMap, latitude, longitude, radius));
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
  public ResponseProjectId editProject(@RequestBody CreateProject project) {
    return this.projectsService.editProject(project);
  }

  @RequestMapping(value = "/getAvailableProjectsForInvite", method = RequestMethod.GET)
  @ResponseBody
  public List<ProjectWithName> getAvailableProjectsForInvite(@RequestParam("ownerId") int ownerId,
                                                             @RequestParam("skillset") String skillset) {
    return this.projectsService.getAvailableProjectsForInvite(ownerId, skillset);
  }

  @RequestMapping(value = "/reportProject", method = RequestMethod.GET)
  @ResponseBody
  public boolean reportProject(@RequestParam("category") String category,
                               @RequestParam("projectId") int id) {
    return this.projectsService.reportProject(category, id);
  }

  @RequestMapping(value = "/addFavorite", method = RequestMethod.GET)
  public void addFavorite(@RequestParam("userId") int userId,
                          @RequestParam("projectId") int projectId) {
    this.projectsService.addFavorite(userId, projectId);
  }

  @RequestMapping(value = "/removeFavorite", method = RequestMethod.GET)
  public void removeFavorite(@RequestParam("userId") int userId,
                             @RequestParam("projectId") int projectId) {
    this.projectsService.removeFavorite(userId, projectId);
  }

  @RequestMapping(value = "/getChartData", method = RequestMethod.GET)
  @ResponseBody
  public ResponseVisits getChartData(@RequestParam("projectId") int projectId) throws ParseException {
    return this.projectsService.getChartData(projectId, Calendar.getInstance());
  }

  @RequestMapping(value = "/createNewUpdate", method = RequestMethod.GET)
  @ResponseBody
  public Update createNewUpdate(@RequestParam("projectId") int projectId,
                                @RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam("date") String date,
                                @RequestParam("type") String type) {
    return this.projectsService.createNewUpdate(projectId, title, content, date, type);
  }

  @RequestMapping(value = "/getCoordinates", method = RequestMethod.GET)
  @ResponseBody
  public List<Coordinate> getCoordinates() {
    return this.projectsService.getCoordinates();
  }

  @RequestMapping(value = "/addPowerpointEmbedCode", method = RequestMethod.GET)
  @ResponseBody
  public boolean addPowerpointEmbedCode(@RequestParam("projectId") int projectId,
                                        @RequestParam("link") String link) {
    return this.projectsService.addPowerpointEmbedCode(projectId, link);
  }

  @RequestMapping(value = "/getEmbedCode", method = RequestMethod.GET)
  @ResponseBody
  public String getEmbedCode(@RequestParam("projectId") int projectId) {
    return this.projectsService.getEmbedCode(projectId);
  }

  @RequestMapping(value = "/deleteUser", method = RequestMethod.DELETE)
  @ResponseBody
  public User deleteUser(@RequestParam("id") int id) {
    return this.projectsService.deleteUser(id);
  }


  @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
  @ResponseBody
  public String handleTinyMCEUpload(@RequestParam("files") MultipartFile files[]) throws IOException {
    System.out.println("uploading MultipartFile " + files.length);
    String filePath = FileUtils.convertToFilepath(files[0]);
//    String result = uploadFilesFromTinyMCE("tinyMCE", files, false);
//    System.out.println(result);
    return "{\"location\":\"" + filePath + "\"}";

  }

  private String uploadFilesFromTinyMCE(String prefix,
                                        MultipartFile files[],
                                        boolean isMain) {
    System.out.println("uploading" + prefix);
    try {
      String folder = "./src/main/webapp/static/all-images/";
      StringBuffer result = new StringBuffer();
      byte[] bytes = null;
      result.append("Uploading of File(s) ");

      for (int i = 0; i < files.length; i++) {
        if (!files[i].isEmpty()) {

          try {
            boolean created = false;

            try {
              File theDir = new File(folder);
              theDir.mkdir();
              created = true;
            } catch (SecurityException se) {
              se.printStackTrace();
            }
            if (created) {
              System.out.println("DIR created");
            }
            String path = "";
            path = folder + files[i].getOriginalFilename();
            File destination = new File(path);
            System.out.println("--> " + destination);
            files[i].transferTo(destination);
            result.append(files[i].getOriginalFilename() + " Succsess. ");
          } catch (Exception e) {
            throw new RuntimeException("Product Image saving failed", e);
          }

        } else
          result.append(files[i].getOriginalFilename() + " Failed. ");

      }

      return result.toString();

    } catch (Exception e) {
      return "Error Occured while uploading files." + " => " + e.getMessage();
    }
  }
}
