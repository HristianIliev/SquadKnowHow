package squadknowhow.response.models;

import squadknowhow.dbmodels.ProjectShort;

import java.util.List;

public class ResponseProjects {
  private List<ProjectShort> projects;

  public ResponseProjects(List<ProjectShort> projects) {
    this.setProjects(projects);
  }

  public List<ProjectShort> getProjects() {
    return projects;
  }

  public void setProjects(List<ProjectShort> projects) {
    this.projects = projects;
  }
}
