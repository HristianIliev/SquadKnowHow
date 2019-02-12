package squadknowhow.dbmodels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.jsoup.Jsoup;
import squadknowhow.contracts.Model;
import squadknowhow.response.models.ProjectMember;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "projects")
public class ProjectShort implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "name")
  private String name;
  @Column(name = "description")
  private String description;
  @Column(name = "creator_id")
  private Integer creator;
  @Column(name = "city")
  private String city;
  @ManyToMany()
  @LazyCollection(LazyCollectionOption.FALSE)
  @JoinTable(name = "projects_users",
          joinColumns = {@JoinColumn(name = "project_id")},
          inverseJoinColumns = {
                  @JoinColumn(name = "user_id")})
  @JsonIgnore
  private List<User> members;
  @ManyToMany()
  @LazyCollection(LazyCollectionOption.FALSE)
  @JoinTable(name = "user_categories_projects",
          joinColumns = {
                  @JoinColumn(name = "project_id")},
          inverseJoinColumns = {@JoinColumn(name = "user_category_id")})
  private List<UserCategory> projectNeeds;
  @Column(name = "cover")
  private String cover;
  @Column(name = "latitude")
  private Double latitude;
  @Column(name = "longitude")
  private Double longitude;
  @Column(name = "times_backed")
  private int timesBacked;
  @Column(name = "isTopProject")
  private boolean isTopProject;

  @JsonProperty
  public List<ProjectMember> getProjectMembers() {
    List<ProjectMember> result = new ArrayList<>();
    for (int i = 0; i < this.getMembers().size(); i++) {
      result.add(new ProjectMember(this.getMembers().get(i).getId(),
              this.getMembers().get(i).getFirstName()
                      + " " + this.getMembers().get(i).getLastName(),
              this.getMembers().get(i).getFirstName(),
              this.getMembers().get(i).getLastName(),
              this.getMembers().get(i).getSkillset().getName(),
              this.getMembers().get(i).getEmail()));
    }

    return result;
  }

  @JsonProperty
  public String getStrippedDescription() {
    return Jsoup.parse(this.getDescription()).text();
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
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

  public Integer getCreator() {
    return creator;
  }

  public void setCreator(Integer creator) {
    this.creator = creator;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public List<UserCategory> getProjectNeeds() {
    return projectNeeds;
  }

  public void setProjectNeeds(List<UserCategory> projectNeeds) {
    this.projectNeeds = projectNeeds;
  }

  public String getCover() {
    return cover;
  }

  public void setCover(String cover) {
    this.cover = cover;
  }

  public List<User> getMembers() {
    return members;
  }

  public void setMembers(List<User> members) {
    this.members = members;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public int getTimesBacked() {
    return timesBacked;
  }

  public void setTimesBacked(int timesBacked) {
    this.timesBacked = timesBacked;
  }

  public boolean isTopProject() {
    return isTopProject;
  }

  public void setTopProject(boolean topProject) {
    isTopProject = topProject;
  }

  @JsonProperty
  public String getCreatorName() {
    List<User> users = this.getMembers().stream()
            .filter(u -> u.getId() == this.getCreator())
            .collect(Collectors.toList());

    return users.get(0).getFirstName();
  }
}
