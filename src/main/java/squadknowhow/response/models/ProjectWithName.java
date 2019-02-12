package squadknowhow.response.models;

public class ProjectWithName {
  private int id;
  private String name;

  public ProjectWithName() {

  }

  public ProjectWithName(int id, String name) {
    this.setId(id);
    this.setName(name);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ProjectWithName that = (ProjectWithName) o;

    if (getId() != that.getId()) return false;
    return getName() != null
            ? getName().equals(that.getName())
            : that.getName() == null;
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    return result;
  }
}
