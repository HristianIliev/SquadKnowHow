package squadknowhow.response.models;

import squadknowhow.dbmodels.UserShort;

import java.util.List;

public class ResponseUsers {
  private List<UserShort> users;

  public ResponseUsers(List<UserShort> users) {
    this.setUsers(users);
  }

  public List<UserShort> getUsers() {
    return users;
  }

  public void setUsers(List<UserShort> users) {
    this.users = users;
  }
}
