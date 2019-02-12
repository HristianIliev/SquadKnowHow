package squadknowhow.dbmodels;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import squadknowhow.contracts.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "notes")
public class Note implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "name")
  private String name;
  @Column(name = "description")
  private String description;
  @ManyToOne
  @JsonBackReference
  private User user;
  @Column(name = "project_id")
  private int projectId;

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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Note note = (Note) o;
    return getId() == note.getId()
            && Objects.equals(getName(), note.getName())
            && Objects.equals(getDescription(), note.getDescription());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName(), getDescription());
  }

  public int getProjectId() {
    return projectId;
  }

  public void setProjectId(int projectId) {
    this.projectId = projectId;
  }

  @JsonIgnore
  public String createProjectLink() {
    String result = "/project/" + this.getProjectId();
    for (Project project
            : user.getProjects()) {
      if (project.getId() == this.getProjectId()
              && project.getCreator() == user.getId()) {
        result = "/project-admin/" + project.getId();
        break;
      } else if (project.getId() == this.getProjectId()
              && project.getCreator() != user.getId()) {
        result = "/project-member/" + project.getId();
      }
    }

    return result;
  }
}
