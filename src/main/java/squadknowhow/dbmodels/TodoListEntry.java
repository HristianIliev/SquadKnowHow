package squadknowhow.dbmodels;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import squadknowhow.contracts.Model;

@Entity
@Table(name = "todo_list_entries")
public class TodoListEntry implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "title")
  private String title;
  @Column(name = "description")
  private String description;
  @Column(name = "due_date")
  private String dueDate;
  @Column(name = "done")
  private boolean done;
  @ManyToOne
  @JsonBackReference
  private Project project;

  public TodoListEntry() {

  }

  public TodoListEntry(String title) {
    this.setTitle(title);
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
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

  public boolean isDone() {
    return done;
  }

  public void setDone(boolean done) {
    this.done = done;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }
}
