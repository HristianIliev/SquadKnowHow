package squadknowhow.request.models;

public class EditedProject {
  private int id;
  private String name;
  private String description;
  private String goal1;
  private String goal2;
  private String goal3;
  private String githubPage;
  private String youtubeLink;

  public EditedProject() {

  }

  public EditedProject(int id) {
    this.setId(id);
  }

  public EditedProject(int id, String name) {
    this.setId(id);
    this.setName(name);
  }

  /**
   * Constructor for the class with purpose unit tests.
   *
   * @param id          id
   * @param name        name
   * @param description description
   */
  public EditedProject(int id, String name, String description) {
    this.setId(id);
    this.setName(name);
    this.setDescription(description);
  }

  /**
   * Constructor made for the class with purpose unit tests.
   *
   * @param id          id
   * @param name        name
   * @param description description
   * @param goal1       goal1
   */
  public EditedProject(int id, String name, String description, String goal1) {
    this.setId(id);
    this.setName(name);
    this.setDescription(description);
    this.setGoal1(goal1);
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getGoal1() {
    return goal1;
  }

  public void setGoal1(String goal1) {
    this.goal1 = goal1;
  }

  public String getGoal2() {
    return goal2;
  }

  public void setGoal2(String goal2) {
    this.goal2 = goal2;
  }

  public String getGoal3() {
    return goal3;
  }

  public void setGoal3(String goal3) {
    this.goal3 = goal3;
  }

  public String getGithubPage() {
    return githubPage;
  }

  public void setGithubPage(String githubPage) {
    this.githubPage = githubPage;
  }

  public String getYoutubeLink() {
    return youtubeLink;
  }

  public void setYoutubeLink(String youtubeLink) {
    this.youtubeLink = youtubeLink;
  }
}
