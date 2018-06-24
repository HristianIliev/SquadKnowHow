package squadknowhow.contracts;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import squadknowhow.dbmodels.Project;
import squadknowhow.dbmodels.ProjectShort;
import squadknowhow.request.models.EditedProject;
import squadknowhow.request.models.SentAdvice;
import squadknowhow.request.models.SentMessage;
import squadknowhow.request.models.SentQuestion;
import squadknowhow.response.models.ResponseCheckProjectName;
import squadknowhow.response.models.ResponsePagination;
import squadknowhow.response.models.ResponseProjectId;
import squadknowhow.response.models.ResponseSuccessful;
import squadknowhow.response.models.ResponseUpload;
import squadknowhow.response.models.UserProjects;

public interface IProjectsService {

  List<ProjectShort> getProjects(int page, String name, String userCategory, String city);

  ResponseProjectId createProject(Project project, int creatorId);

  ResponseUpload uploadImage(MultipartFile multipart, int id) throws IOException;

  ResponseUpload uploadPictures(MultipartFile file, int id, String fileName) throws IOException;

  ResponsePagination getProjectsPages(String name, String userCategory, String city);

  ResponseCheckProjectName checkProjectName(String name);

  Project getProjectById(int id);

  Project getProjectByName(String projectName);

  ResponseSuccessful sendMessageForApproval(int projectId, int newMemberId, int creatorId);

  ResponseSuccessful addProjectMember(String projectName, int newMemberId, int messageId);

  ResponseSuccessful sendRejectMessage(int recipientId,
                                       String projectName,
                                       int messageToDeleteId,
                                       int creatorId);

  UserProjects getProjectsOfUser(int userId);

  ResponseSuccessful deleteProject(int projectId);

  ResponseSuccessful removeProjectMember(int projectId, int memberId);

  ResponseSuccessful addVisit(int projectId);

  ResponseSuccessful sendMessageToAllMembers(int projectId, SentMessage message);

  ResponseSuccessful sendAdvice(SentAdvice advice);

  ResponseSuccessful sendQuestion(SentQuestion question);

  ResponseSuccessful editProject(EditedProject project);
}
