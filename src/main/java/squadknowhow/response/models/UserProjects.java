package squadknowhow.response.models;

import java.util.List;

import squadknowhow.dbmodels.Project;

public class UserProjects {
  private List<Project> createdProjects;
  private List<Project> memberInProjects;

  public UserProjects(List<Project> createdProjects, List<Project> memberInProjects) {
    this.setCreatedProjects(createdProjects);
    this.setMemberInProjects(memberInProjects);
  }

  public List<Project> getCreatedProjects() {
    return createdProjects;
  }

  public void setCreatedProjects(List<Project> createdProjects) {
    this.createdProjects = createdProjects;
  }

  public List<Project> getMemberInProjects() {
    return memberInProjects;
  }

  public void setMemberInProjects(List<Project> memberInProjects) {
    this.memberInProjects = memberInProjects;
  }
}
