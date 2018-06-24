package squadknowhow.response.models;

import java.util.List;

import squadknowhow.dbmodels.Group;

public class ResponseGroups {
  private List<Group> groups;

  public ResponseGroups(List<Group> groups) {
    this.setGroups(groups);
  }

  public List<Group> getGroups() {
    return groups;
  }

  public void setGroups(List<Group> groups) {
    this.groups = groups;
  }
}
