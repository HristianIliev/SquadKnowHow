package squadknowhow.request.models;

public class ListEntry {
  private String title;
  private String description;
  private String dueDate;
  private int projectId;

  public ListEntry() {

  }

  public ListEntry(String title) {
    this.setTitle(title);
  }

  public ListEntry(String title, int projectId) {
    this.setTitle(title);
    this.setProjectId(projectId);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDueDate() {
    return dueDate;
  }

  public void setDueDate(String dueDate) {
    this.dueDate = dueDate;
  }

  public int getProjectId() {
    return projectId;
  }

  public void setProjectId(int projectId) {
    this.projectId = projectId;
  }
}
