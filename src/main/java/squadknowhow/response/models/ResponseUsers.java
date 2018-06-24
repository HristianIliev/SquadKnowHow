package squadknowhow.response.models;

import java.util.List;

import squadknowhow.dbmodels.User;
import squadknowhow.dbmodels.UserShort;

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
