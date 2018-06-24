package squadknowhow.dbmodels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import squadknowhow.contracts.Model;
import squadknowhow.response.models.ProjectMember;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

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
  @ManyToOne
  private City city;
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
  @Lob
  @Column(name = "cover")
  private byte[] cover;

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

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public List<UserCategory> getProjectNeeds() {
    return projectNeeds;
  }

  public void setProjectNeeds(List<UserCategory> projectNeeds) {
    this.projectNeeds = projectNeeds;
  }

  public byte[] getCover() {
    return cover;
  }

  public void setCover(byte[] cover) {
    this.cover = cover;
  }

  public List<User> getMembers() {
    return members;
  }

  public void setMembers(List<User> members) {
    this.members = members;
  }
}
