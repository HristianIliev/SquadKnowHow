package squadknowhow.dbmodels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import squadknowhow.contracts.Model;
import squadknowhow.response.models.ProjectMember;

@Entity
@Table(name = "projects")
public class Project implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "name")
  private String name;
  @Column(name = "description")
  private String description;
  @Column(name = "goal1")
  private String goal1;
  @Column(name = "goal2")
  private String goal2;
  @Column(name = "goal3")
  private String goal3;
  // @ManyToOne
  // @JsonBackReference
  @Column(name = "creator_id")
  private Integer creator;
  @Column(name = "github_page")
  private String githubPage;
  @ManyToOne
  private City city;
  @ManyToMany()
  @LazyCollection(LazyCollectionOption.FALSE)
  @JoinTable(name = "user_categories_projects",
          joinColumns = {
                  @JoinColumn(name = "project_id")},
          inverseJoinColumns = {@JoinColumn(name = "user_category_id")})
  private List<UserCategory> projectNeeds;
  @ManyToMany()
  @LazyCollection(LazyCollectionOption.FALSE)
  @JoinTable(name = "projects_users",
          joinColumns = {@JoinColumn(name = "project_id")},
          inverseJoinColumns = {
                  @JoinColumn(name = "user_id")})
  @JsonIgnore
  private List<User> members;
  @Lob
  @Column(name = "cover")
  private byte[] cover;
  @Column(name = "youtube_link")
  private String youtubeLink;
  @Lob
  @Column(name = "picture1")
  private byte[] picture1;
  @Lob
  @Column(name = "picture2")
  private byte[] picture2;
  @Lob
  @Column(name = "picture3")
  private byte[] picture3;
  @Lob
  @Column(name = "picture4")
  private byte[] picture4;
  @Lob
  @Column(name = "picture5")
  private byte[] picture5;
  @Lob
  @Column(name = "picture6")
  private byte[] picture6;
  @Lob
  @Column(name = "picture7")
  private byte[] picture7;
  @Lob
  @Column(name = "picture8")
  private byte[] picture8;
  @Lob
  @Column(name = "picture9")
  private byte[] picture9;
  @Column(name = "visits")
  private int visits;
  @OneToMany(mappedBy = "project")
  @JsonManagedReference
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<Advice> advices;
  @OneToMany(mappedBy = "project")
  @JsonManagedReference
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<Question> questions;
  @OneToMany(mappedBy = "project")
  @JsonManagedReference
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<TodoListEntry> listEntries;
  @Column(name = "telephone")
  private String telephone;
  @Column(name = "needed_money")
  private double neededMoney;
  @Column(name = "received_money")
  private double receivedMoney;
  @Column(name = "needs_money")
  private boolean needsMoney;

  public Project() {

  }

  public Project(int id) {
    this.setId(id);
  }

  public Project(String name) {
    this.setName(name);
  }

  public Project(String name, String description) {
    this.setName(name);
    this.setDescription(description);
  }

  public Project(String name, String description, String goal1) {
    this.setName(name);
    this.setDescription(description);
    this.setGoal1(goal1);
  }

  public Project(String name,
                 String description,
                 String goal1,
                 City city,
                 List<UserCategory> projectNeeds) {
    this.setName(name);
    this.setDescription(description);
    this.setGoal1(goal1);
    this.setCity(city);
    this.setProjectNeeds(projectNeeds);
  }

  public Project(int creator, List<User> members) {
    this.setCreator(creator);
    this.setMembers(members);
  }

  public Project(int creator, String name) {
    this.setCreator(creator);
    this.setName(name);
  }

  public Project(int creator, List<User> members, String name) {
    this.setCreator(creator);
    this.setMembers(members);
    this.setName(name);
  }

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

  public String getGoal1() {
    return goal1;
  }

  public void setGoal1(String goal1) {
    this.goal1 = goal1;
  }

  public String getGoal3() {
    return goal3;
  }

  public void setGoal3(String goal3) {
    this.goal3 = goal3;
  }

  public String getGithubPage() {
    return githubPage;
  }

  public void setGithubPage(String githubPage) {
    this.githubPage = githubPage;
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

  public List<User> getMembers() {
    return members;
  }

  public void setMembers(List<User> members) {
    this.members = members;
  }

  public byte[] getCover() {
    return cover;
  }

  @JsonIgnore
  public String getBase64Image() {
    return Base64.encodeBase64String(this.getCover());
  }

  public void setCover(byte[] cover) {
    this.cover = cover;
  }

  public String getYoutubeLink() {
    return youtubeLink;
  }

  public void setYoutubeLink(String youtubeLink) {
    this.youtubeLink = youtubeLink;
  }

  public String getGoal2() {
    return goal2;
  }

  public void setGoal2(String goal2) {
    this.goal2 = goal2;
  }

  public byte[] getPicture1() {
    return picture1;
  }

  @JsonIgnore
  public String getBase64Picture1() {
    return Base64.encodeBase64String(this.getPicture1());
  }

  public void setPicture1(byte[] picture1) {
    this.picture1 = picture1;
  }

  public byte[] getPicture2() {
    return picture2;
  }

  @JsonIgnore
  public String getBase64Picture2() {
    return Base64.encodeBase64String(this.getPicture2());
  }

  public void setPicture2(byte[] picture2) {
    this.picture2 = picture2;
  }

  public byte[] getPicture3() {
    return picture3;
  }

  @JsonIgnore
  public String getBase64Picture3() {
    return Base64.encodeBase64String(this.getPicture3());
  }

  public void setPicture3(byte[] picture3) {
    this.picture3 = picture3;
  }

  public byte[] getPicture4() {
    return picture4;
  }

  @JsonIgnore
  public String getBase64Picture4() {
    return Base64.encodeBase64String(this.getPicture4());
  }

  public void setPicture4(byte[] picture4) {
    this.picture4 = picture4;
  }

  public byte[] getPicture5() {
    return picture5;
  }

  @JsonIgnore
  public String getBase64Picture5() {
    return Base64.encodeBase64String(this.getPicture5());
  }

  public void setPicture5(byte[] picture5) {
    this.picture5 = picture5;
  }

  public byte[] getPicture6() {
    return picture6;
  }

  @JsonIgnore
  public String getBase64Picture6() {
    return Base64.encodeBase64String(this.getPicture6());
  }

  public void setPicture6(byte[] picture6) {
    this.picture6 = picture6;
  }

  public byte[] getPicture7() {
    return picture7;
  }

  @JsonIgnore
  public String getBase64Picture7() {
    return Base64.encodeBase64String(this.getPicture7());
  }

  public void setPicture7(byte[] picture7) {
    this.picture7 = picture7;
  }

  public byte[] getPicture8() {
    return picture8;
  }

  @JsonIgnore
  public String getBase64Picture8() {
    return Base64.encodeBase64String(this.getPicture8());
  }

  public void setPicture8(byte[] picture8) {
    this.picture8 = picture8;
  }

  public byte[] getPicture9() {
    return picture9;
  }

  @JsonIgnore
  public String getBase64Picture9() {
    return Base64.encodeBase64String(this.getPicture9());
  }

  public void setPicture9(byte[] picture9) {
    this.picture9 = picture9;
  }

  public Integer getCreator() {
    return creator;
  }

  public void setCreator(Integer creator) {
    this.creator = creator;
  }

  public int getVisits() {
    return visits;
  }

  public void setVisits(int visits) {
    this.visits = visits;
  }

  public List<Advice> getAdvices() {
    return advices;
  }

  public void setAdvices(List<Advice> advices) {
    this.advices = advices;
  }

  public List<TodoListEntry> getListEntries() {
    return listEntries;
  }

  public void setListEntries(List<TodoListEntry> listEntries) {
    this.listEntries = listEntries;
  }

  public List<Question> getQuestions() {
    return questions;
  }

  public void setQuestions(List<Question> questions) {
    this.questions = questions;
  }

  @JsonIgnore
  public void printProjectNeeds() {
    for (UserCategory projectNeed : this.projectNeeds) {
      System.out.println(projectNeed.getName());
    }
  }

  @JsonIgnore
  public String getProjectNeedsStr() {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < this.getProjectNeeds().size(); i++) {
      result.append(this.getProjectNeeds().get(i).getName());
      if (i != this.getProjectNeeds().size() - 1) {
        result.append(", ");
      }
    }

    if (this.getProjectNeeds().size() == 0) {
      result.append("Този проект не се нуждае от никого вмомента");
    }

    return result.toString();
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  @Override
  public String toString() {
    return "Project [id=" + id + ", name=" + name + ", description="
            + description + ", goal1=" + goal1 + ", goal2="
            + getGoal2() + ", goal3=" + goal3 + ", githubPage="
            + githubPage + ", city=" + city + ", projectNeeds="
            + projectNeeds + ", members=" + members + ", visits=" + visits + "]";
  }

  public double getNeededMoney() {
    return neededMoney;
  }

  public void setNeededMoney(double neededMoney) {
    this.neededMoney = neededMoney;
  }

  public double getReceivedMoney() {
    return receivedMoney;
  }

  public void setReceivedMoney(double receivedMoney) {
    this.receivedMoney = receivedMoney;
  }

  public boolean isNeedsMoney() {
    return needsMoney;
  }

  public void setNeedsMoney(boolean needsMoney) {
    this.needsMoney = needsMoney;
  }

  @JsonIgnore
  public double getPercentageReceived() {
    double decimal = this.getReceivedMoney() / this.getNeededMoney();
    double percentage = decimal * 100;
    String rounded = String.format("%.2f", percentage);
    return Double.parseDouble(rounded);
  }
}
