package squadknowhow.contracts;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import squadknowhow.dbmodels.Auction;
import squadknowhow.dbmodels.Project;
import squadknowhow.dbmodels.ProjectShort;
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
import squadknowhow.response.models.ResponseSuccessful;
import squadknowhow.response.models.ResponseUpload;
import squadknowhow.response.models.ResponseVisits;
import squadknowhow.response.models.UserProjects;

public interface IProjectsService {
  List<ProjectShort> getProjects(int page,
                                 String name,
                                 String userCategory,
                                 String city,
                                 boolean isByMap,
                                 double latitude,
                                 double longitude,
                                 int radius);

  ResponseProjectId createProject(CreateProject project,
                                  int creatorId);

  ResponseUpload uploadImage(MultipartFile multipart,
                             int id) throws IOException;

  ResponseUpload uploadPictures(MultipartFile file,
                                int id,
                                String fileName) throws IOException;

  ResponsePagination getProjectsPages(String name,
                                      String userCategory,
                                      String city,
                                      boolean isByMap,
                                      double latitude,
                                      double longitude,
                                      int radius);

  ResponseCheckProjectName checkProjectName(String name);

  Project getProjectById(int id);

  Project getProjectByName(String projectName);

  ResponseSuccessful sendMessageForApproval(int projectId,
                                            int newMemberId,
                                            int creatorId);

  ResponseSuccessful addProjectMember(String projectName,
                                      int newMemberId,
                                      int messageId,
                                      boolean isInvite);

  ResponseSuccessful sendRejectMessage(int recipientId,
                                       String projectName,
                                       int messageToDeleteId,
                                       int creatorId,
                                       boolean isInvite);

  UserProjects getProjectsOfUser(int userId);

  ResponseSuccessful deleteProject(int projectId,
                                   List<DeleteProjectInfo> deleteProjectInfos,
                                   boolean isCompleted);

  ResponseSuccessful removeProjectMember(int projectId,
                                         int memberId);

  ResponseSuccessful addVisit(int projectId);

  ResponseSuccessful sendMessageToAllMembers(int projectId,
                                             SentMessage message);

  ResponseSuccessful sendAdvice(SentAdvice advice);

  ResponseSuccessful sendQuestion(SentQuestion question);

  ResponseProjectId editProject(CreateProject project);

  List<ProjectWithName> getAvailableProjectsForInvite(int ownerId,
                                                      String skillset);

  ResponseSuccessful sendInviteMessage(int projectId,
                                       int recipient,
                                       int sender);

  boolean reportProject(String category, int id);

  boolean addFavorite(int userId, int projectId);

  boolean removeFavorite(int userId, int projectId);

  boolean isUserMember(User user, Auction auction);

  int getWorkingOnProject(int projectId);

  String getDescription(int projectId);

  ResponseVisits getChartData(int projectId,
                              Calendar calendar) throws ParseException;

  List<Coordinate> getCoordinates();

  boolean addPowerpointEmbedCode(int projectId,
                                 String link);

  String getEmbedCode(int projectId);

  List<Project> getFavouriteProjects();

  Update createNewUpdate(int projectId,
                         String title,
                         String content,
                         String date,
                         String type);

  WantedMember getWantedMember(int id);

  User deleteUser(int id);

  List<WantedMember> getWantedMembers(int id);

  List<UserCategory> getProjectNeeds(int id);

  List<FineUploaderImage> getProjectImages(int projectId);

  int deleteProjectFile(int projectId, int pictureNum);

  IChatbotResponse getPotentialCandidates(String projectName);
}
