package squadknowhow.response.models;

import java.util.List;

import squadknowhow.dbmodels.Group;
import squadknowhow.dbmodels.GroupShort;

public class ResponseGroups {
  private List<GroupShort> groups;

  public ResponseGroups(List<GroupShort> groups) {
    this.setGroups(groups);
  }

  public List<GroupShort> getGroups() {
    return groups;
  }

  public void setGroups(List<GroupShort> groups) {
    this.groups = groups;
  }
}
