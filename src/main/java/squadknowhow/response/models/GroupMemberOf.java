package squadknowhow.response.models;

public class GroupMemberOf {
  private int id;
  private String logo;
  private String name;
  private int membersCount;
  private int projectsCount;

  public GroupMemberOf(int id,
                       String logo,
                       String name,
                       int membersCount,
                       int projectsCount) {
    this.setId(id);
    this.setLogo(logo);
    this.setName(name);
    this.setMembersCount(membersCount);
    this.setProjectsCount(projectsCount);
  }

  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getMembersCount() {
    return membersCount;
  }

  public void setMembersCount(int membersCount) {
    this.membersCount = membersCount;
  }

  public int getProjectsCount() {
    return projectsCount;
  }

  public void setProjectsCount(int projectsCount) {
    this.projectsCount = projectsCount;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
