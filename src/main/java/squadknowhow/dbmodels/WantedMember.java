package squadknowhow.dbmodels;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import squadknowhow.contracts.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "wanted_members")
public class WantedMember implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @ManyToOne
  @JsonBackReference
  private Project project;
  @ManyToOne
  private City city;
  @Column(name = "education")
  private String education;
  @ManyToMany()
  @LazyCollection(LazyCollectionOption.FALSE)
  @JoinTable(name = "skills_wanted_members",
          joinColumns = {@JoinColumn(name = "wanted_member_id")},
          inverseJoinColumns = {
                  @JoinColumn(name = "skill_id")})
  private List<Skill> skills;
  @ManyToMany()
  @LazyCollection(LazyCollectionOption.FALSE)
  @JoinTable(name = "languages_wanted_members",
          joinColumns = {@JoinColumn(name = "wanted_member_id")},
          inverseJoinColumns = {
                  @JoinColumn(name = "language_id")})
  private List<Language> languages;
  @ManyToOne
  private UserCategory userCategory;

  public WantedMember() {

  }

  public WantedMember(int id) {
    this.setId(id);
  }

  public WantedMember(UserCategory userCategory) {
    this.setUserCategory(userCategory);
  }

  public WantedMember(int id,
                      Project project,
                      City city,
                      String education,
                      List<Skill> skills,
                      List<Language> languages,
                      UserCategory userCategory) {
    this.setId(id);
    this.setProject(project);
    this.setCity(city);
    this.setEducation(education);
    this.setSkills(skills);
    this.setLanguages(languages);
    this.setUserCategory(userCategory);
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

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public String getEducation() {
    return education;
  }

  public void setEducation(String education) {
    this.education = education;
  }

  public List<Skill> getSkills() {
    return skills;
  }

  public void setSkills(List<Skill> skills) {
    this.skills = skills;
  }

  public List<Language> getLanguages() {
    return languages;
  }

  public void setLanguages(List<Language> languages) {
    this.languages = languages;
  }

  public UserCategory getUserCategory() {
    return userCategory;
  }

  public void setUserCategory(UserCategory userCategory) {
    this.userCategory = userCategory;
  }

  @Override
  public String toString() {
    return "WantedMember{" +
            "id=" + id +
            ", city=" + city +
            ", education='" + education + '\'' +
            ", skills=" + skills +
            ", languages=" + languages +
            ", userCategory=" + userCategory +
            '}';
  }

  public boolean isEmpty() {
    return this.getUserCategory() == null &&
            this.getSkills() == null &&
            this.getCity() == null &&
            this.getSkills() == null &&
            this.getEducation() == null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    WantedMember that = (WantedMember) o;
    return getId() == that.getId() &&
            Objects.equals(getCity(), that.getCity()) &&
            Objects.equals(getEducation(), that.getEducation()) &&
            Objects.equals(getSkills(), that.getSkills()) &&
            Objects.equals(getLanguages(), that.getLanguages()) &&
            Objects.equals(getUserCategory(), that.getUserCategory());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(),
            getCity(),
            getEducation(),
            getSkills(),
            getLanguages(),
            getUserCategory());
  }
}
