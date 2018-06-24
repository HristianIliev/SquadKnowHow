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
import squadknowhow.contracts.IGroupsService;
import squadknowhow.dbmodels.Group;
import squadknowhow.response.models.ResponseCheckGroupName;
import squadknowhow.response.models.ResponseGroupId;
import squadknowhow.response.models.ResponseGroups;
import squadknowhow.response.models.ResponsePagination;
import squadknowhow.response.models.ResponseUpload;

@RestController
@RequestMapping("/api")
public class GroupsController {
  private IGroupsService groupsService;

  @Autowired
  public GroupsController(IGroupsService groupsService) {
    this.groupsService = groupsService;
  }

  @RequestMapping(value = "/createGroup", method = RequestMethod.POST)
  @ResponseBody
  public ResponseGroupId createGroup(@RequestBody Group group,
                                     @RequestParam("creatorId") int creatorId) {
    System.out.println(group.getName());
    return this.groupsService.createGroup(group, creatorId);
  }

  @RequestMapping(value = "/uploadLogoPicture", method = RequestMethod.POST)
  @ResponseBody
  public ResponseUpload uploadGroupLogo(@RequestParam("qqfile") MultipartFile file,
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
    return this.groupsService.uploadImage(file, id);
  }

  @RequestMapping(value = "/group", method = RequestMethod.GET)
  @ResponseBody
  public Group getGroup(@RequestParam("id") int id) {
    return this.groupsService.getGroupById(id);
  }

  @RequestMapping(value = "/getGroupPages")
  @ResponseBody
  public ResponsePagination getGroupPages(@RequestParam("name") String name) {
    return this.groupsService.getGroupPages(name);
  }

  @RequestMapping(value = "/checkGroupName")
  @ResponseBody
  public ResponseCheckGroupName checkGroupName(@RequestParam("name") String name) {
    return this.groupsService.checkGroupName(name);
  }

  @RequestMapping(value = "/groups")
  @ResponseBody
  public ResponseGroups getGroups(@RequestParam("page") int page,
                                  @RequestParam("name") String name) {
    return new ResponseGroups(this.groupsService.getGroups(page, name));
  }

  @RequestMapping(value = "/addGroupMember", method = RequestMethod.GET)
  public boolean addGroupMember(@RequestParam("groupId") int groupId,
                                @RequestParam("userId") int userId) {
    return this.groupsService.addGroupMember(groupId, userId);
  }
}
