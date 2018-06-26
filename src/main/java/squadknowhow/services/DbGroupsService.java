package squadknowhow.services;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import squadknowhow.contracts.IGroupsService;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.Group;
import squadknowhow.dbmodels.GroupShort;
import squadknowhow.dbmodels.User;
import squadknowhow.response.models.ResponseCheckGroupName;
import squadknowhow.response.models.ResponseGroupId;
import squadknowhow.response.models.ResponsePagination;
import squadknowhow.response.models.ResponseUpload;
import squadknowhow.utils.FileUtils;
import squadknowhow.utils.validators.base.IValidator;

@Service
public class DbGroupsService implements IGroupsService {
  private final IRepository<GroupShort> groupsShortRepository;
  private IValidator<Integer> idValidator;
  private IValidator<Group> groupsValidator;
  private IRepository<Group> groupsRepository;
  private IRepository<User> usersRepository;

  @Autowired
  public DbGroupsService(IRepository<Group> groupsRepository,
                         IRepository<GroupShort> groupsShortRepository,
                         IRepository<User> usersRepository,
                         IValidator<Group> groupsValidator,
                         IValidator<Integer> idValidator) {
    this.groupsRepository = groupsRepository;
    this.groupsShortRepository = groupsShortRepository;
    this.usersRepository = usersRepository;
    this.groupsValidator = groupsValidator;
    this.idValidator = idValidator;
  }

  @Override
  public ResponseGroupId createGroup(Group group, int creatorId) {
    if (!this.groupsValidator.isValid(group)) {
      throw new InvalidParameterException("Group is not valid");
    }

    if (!this.idValidator.isValid(creatorId)) {
      throw new InvalidParameterException("CreatorId is not valid");
    }

    Group groupToInsert = new Group();
    groupToInsert.setName(group.getName());
    groupToInsert.setDescription(group.getDescription());
    groupToInsert.setGroupType(group.getGroupType());

    List<User> membersToInsert = new ArrayList<>();

    User creator = this.usersRepository.getById(creatorId);
    membersToInsert.add(creator);
    groupToInsert.setMembers(membersToInsert);

    this.groupsRepository.create(groupToInsert);

    return new ResponseGroupId(groupToInsert.getId());
  }

  @Override
  public ResponseUpload uploadImage(MultipartFile multipart, int id) throws IOException {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    File file = FileUtils.convertToFile(multipart);
    byte[] bytesFile = FileUtils.toByteArray(file);

    Group group = this.groupsRepository.getById(id);

    group.setLogo(bytesFile);
    this.groupsRepository.update(group);

    if (file != null) {
      file.delete();
    }

    return new ResponseUpload(true);
  }

  @Override
  public Group getGroupById(int id) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    return this.groupsRepository.getById(id);
  }

  @Override
  public ResponsePagination getGroupPages(String name) {
    List<Group> groups = this.groupsRepository.getAll();

    groups = groups.stream()
            .filter(group -> group.getName().toLowerCase().contains(name.toLowerCase()))
            .collect(Collectors.toList());

    int numberOfPages = (int) Math.ceil(groups.size() / 20.0);

    return new ResponsePagination(numberOfPages, groups.size());
  }

  @Override
  public ResponseCheckGroupName checkGroupName(String name) {
    Group group = this.groupsRepository.getAll().stream()
            .filter(gr -> gr.getName().equals(name))
            .findFirst()
            .orElse(null);

    if (group != null) {
      return new ResponseCheckGroupName(400);
    }

    return new ResponseCheckGroupName(200);
  }

  @Override
  public List<GroupShort> getGroups(int page, String name) {
    if (page < 1) {
      return null;
    }

    List<GroupShort> groups = this.groupsShortRepository.getAll();
    groups = groups.stream()
            .filter(group -> group.getName().toLowerCase().contains(name.toLowerCase()))
            .collect(Collectors.toList());

    int fromIndex = (page - 1) * 20;
    int toIndex;
    if (groups.size() < 20) {
      toIndex = groups.size();
    } else {
      toIndex = fromIndex + 20;
    }

    return groups.subList(fromIndex, toIndex);
  }

  @Override
  public boolean addGroupMember(int groupId, int userId) {
    if (!this.idValidator.isValid(groupId)) {
      throw new InvalidParameterException("GroupId is not valid");
    } else if (!this.idValidator.isValid(userId)) {
      throw new InvalidParameterException("UserId is not valid");
    }

    Group group = this.groupsRepository.getById(groupId);
    User newMember = this.usersRepository.getById(userId);
    group.getMembers().add(newMember);
    this.groupsRepository.update(group);

    return true;
  }
}
