package squadknowhow.response.models;

public class GroupMemberOf {
  private int id;
  private byte[] logo;
  private String name;
  private int membersCount;
  private int projectsCount;

  /**
   * Constructor for the unit tests.
   *
   * @param id            id
   * @param logo          logo
   * @param name          name
   * @param membersCount  membersCount
   * @param projectsCount projectsCount
   */
  public GroupMemberOf(int id, byte[] logo, String name, int membersCount, int projectsCount) {
    this.setId(id);
    this.setLogo(logo);
    this.setName(name);
    this.setMembersCount(membersCount);
    this.setProjectsCount(projectsCount);
  }

  public byte[] getLogo() {
    return logo;
  }

  public void setLogo(byte[] logo) {
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
