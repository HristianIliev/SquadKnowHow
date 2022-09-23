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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "updates")
public class Update implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "title")
  private String title;
  @Column(name = "content")
  private String content;
  @Column(name = "date")
  private String date;
  @Column(name = "type")
  private String type;
  @ManyToOne
  @JsonBackReference
  private Project project;

  public Update() {

  }

  public Update(int id) {
    this.setId(id);
  }

  public Update(String title,
                String content,
                String date,
                String type,
                Project project) {
    this.setTitle(title);
    this.setContent(content);
    this.setDate(date);
    this.setType(type);
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  @JsonIgnore
  public String getUpdateDate() {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    Date date = null;
    try {
      date = sdf.parse(this.getDate());
    } catch (ParseException e) {
      e.printStackTrace();
    }

    Calendar calendar = Calendar.getInstance();
    assert date != null;
    calendar.setTime(date);
    return Integer.toString(calendar.get(Calendar.DAY_OF_MONTH))
            + " "
            + getMonthNameShort(calendar.get(Calendar.MONTH))
            + " "
            + calendar.get(Calendar.YEAR);
  }

  @JsonIgnore
  public Date getDateObj() {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    Date date = null;
    try {
      return sdf.parse(this.getDate());
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return null;
  }

  private String getMonthNameShort(int month) {
    switch (month) {
      case 0:
        return "Jan";
      case 1:
        return "Feb";
      case 2:
        return "Mar";
      case 3:
        return "Apr";
      case 4:
        return "May";
      case 5:
        return "Jun";
      case 6:
        return "Jul";
      case 7:
        return "Aug";
      case 8:
        return "Sep";
      case 9:
        return "Oct";
      case 10:
        return "Nov";
      case 11:
        return "Dec";
      default:
        return null;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Update update = (Update) o;
    return getId() == update.getId() &&
            Objects.equals(getTitle(), update.getTitle()) &&
            Objects.equals(getContent(), update.getContent()) &&
            Objects.equals(getDate(), update.getDate()) &&
            Objects.equals(getType(), update.getType());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(),
            getTitle(),
            getContent(),
            getDate(),
            getType());
  }
}
