package squadknowhow.dbmodels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import squadknowhow.contracts.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "visits")
public class Visit implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @ManyToOne
  @JsonIgnore
  private Project project;
  @Column(name = "date")
  private String date;

  public Visit() {

  }

  public Visit(String date) {
    this.setDate(date);
  }

  public Visit(String date, Project project) {
    this.setDate(date);
    this.setProject(project);
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }
}
