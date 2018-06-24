package squadknowhow.dbmodels;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import squadknowhow.contracts.Model;

@Entity
@Table(name = "calendar_events")
public class CalendarEvent implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "title")
  private String title;
  @Column(name = "start")
  private String start;
  @Column(name = "end")
  private String end;
  @Column(name = "all_day")
  private boolean allDay;
  @Column(name = "project_id")
  @JsonIgnore
  private int projectId;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getStart() {
    return start;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
    this.end = end;
  }

  public boolean isAllDay() {
    return allDay;
  }

  public void setAllDay(boolean allDay) {
    this.allDay = allDay;
  }

  public int getProjectId() {
    return projectId;
  }

  public void setProjectId(int projectId) {
    this.projectId = projectId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CalendarEvent that = (CalendarEvent) o;

    if (getId() != that.getId()) {
      return false;
    }
    if (isAllDay() != that.isAllDay()) {
      return false;
    }
    if (getProjectId() != that.getProjectId()) {
      return false;
    }
    if (getTitle() != null ? !getTitle().equals(that.getTitle()) : that.getTitle() != null) {
      return false;
    }
    if (getStart() != null ? !getStart().equals(that.getStart()) : that.getStart() != null) {
      return false;
    }
    return getEnd() != null ? getEnd().equals(that.getEnd()) : that.getEnd() == null;
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
    result = 31 * result + (getStart() != null ? getStart().hashCode() : 0);
    result = 31 * result + (getEnd() != null ? getEnd().hashCode() : 0);
    result = 31 * result + (isAllDay() ? 1 : 0);
    result = 31 * result + getProjectId();
    return result;
  }
}
