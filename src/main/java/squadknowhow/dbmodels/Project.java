package squadknowhow.dbmodels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
  @OneToMany(mappedBy = "project")
  @LazyCollection(LazyCollectionOption.FALSE)
  @JsonIgnore
  private List<Visit> visits;
  @Column(name = "city")
  private String city;
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
  @ManyToMany()
  @LazyCollection(LazyCollectionOption.FALSE)
  @JoinTable(name = "favorite_projects",
          joinColumns = {@JoinColumn(name = "project_id")},
          inverseJoinColumns = {
                  @JoinColumn(name = "user_id")})
  @JsonIgnore
  private List<User> favoritedUsers;
  @Column(name = "cover")
  private String cover;
  @Column(name = "youtube_link")
  private String youtubeLink;
  @Column(name = "picture1")
  private String picture1;
  @Column(name = "picture2")
  private String picture2;
  @Column(name = "picture3")
  private String picture3;
  @Column(name = "picture4")
  private String picture4;
  @Column(name = "picture5")
  private String picture5;
  @Column(name = "picture6")
  private String picture6;
  @Column(name = "picture7")
  private String picture7;
  @Column(name = "picture8")
  private String picture8;
  @Column(name = "picture9")
  private String picture9;
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
  @Column(name = "has_reserved_place")
  private boolean hasReservedPlace;
  @Column(name = "daysTop")
  private int daysTop;
  @Column(name = "latitude")
  private Double latitude;
  @Column(name = "longitude")
  private Double longitude;
  @Column(name = "powerpointEmbedCode")
  private String powerpointEmbedCode;
  @Column(name = "times_backed")
  private int timesBacked;
  @Column(name = "isTopProject")
  private boolean isTopProject;
  @ManyToMany()
  @LazyCollection(LazyCollectionOption.FALSE)
  @JoinTable(name = "backers",
          joinColumns = {
                  @JoinColumn(name = "project_id")},
          inverseJoinColumns = {@JoinColumn(name = "user_id")})
  @JsonIgnore
  private List<User> backers;
  @OneToMany(mappedBy = "project")
  @JsonManagedReference
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<Update> updates;
  @OneToMany(mappedBy = "project")
  @JsonManagedReference
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<WantedMember> wantedMembers;

  public Project() {

  }

  public Project(int id) {
    this.setId(id);
  }

  public Project(List<User> members) {
    this.setMembers(members);
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
                 String city,
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
  public String getStrippedDescription() {
    return Jsoup.parse(this.getDescription()).text();
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

  public List<User> getMembers() {
    return members;
  }

  public void setMembers(List<User> members) {
    this.members = members;
  }

  public String getCover() {
    return cover;
  }

  public void setCover(String cover) {
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

  public String getPicture1() {
    return picture1;
  }

  public void setPicture1(String picture1) {
    this.picture1 = picture1;
  }

  public String getPicture2() {
    return picture2;
  }

  public void setPicture2(String picture2) {
    this.picture2 = picture2;
  }

  public String getPicture3() {
    return picture3;
  }

  public void setPicture3(String picture3) {
    this.picture3 = picture3;
  }

  public String getPicture4() {
    return picture4;
  }

  public void setPicture4(String picture4) {
    this.picture4 = picture4;
  }

  public String getPicture5() {
    return picture5;
  }

  public void setPicture5(String picture5) {
    this.picture5 = picture5;
  }

  public String getPicture6() {
    return picture6;
  }

  public void setPicture6(String picture6) {
    this.picture6 = picture6;
  }

  public String getPicture7() {
    return picture7;
  }

  public void setPicture7(String picture7) {
    this.picture7 = picture7;
  }

  public String getPicture8() {
    return picture8;
  }

  public void setPicture8(String picture8) {
    this.picture8 = picture8;
  }

  public String getPicture9() {
    return picture9;
  }

  public void setPicture9(String picture9) {
    this.picture9 = picture9;
  }

  public Integer getCreator() {
    return creator;
  }

  public void setCreator(Integer creator) {
    this.creator = creator;
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
      result.append("Този проект не" +
              " се нуждае от никого вмомента");
    }

    return result.toString();
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
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
    if (percentage > 100) {
      rounded = "100";
    }

    return Double.parseDouble(rounded);
  }

  @JsonIgnore
  public boolean checkIfMember(int id) {
    for (User member :
            this.getMembers()) {
      if (member.getId() == id) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Project project = (Project) o;

    if (getId() != project.getId()) return false;
    return getName() != null
            ? getName().equals(project.getName())
            : project.getName() == null;
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    return result;
  }

  @JsonProperty
  public String getCreatorName() {
    List<User> users = this.getMembers().stream()
            .filter(u -> u.getId() == this.getCreator())
            .collect(Collectors.toList());

    return users.get(0).getFirstName();
  }

  public List<User> getFavoritedUsers() {
    return favoritedUsers;
  }

  public void setFavoritedUsers(List<User> favoritedUsers) {
    this.favoritedUsers = favoritedUsers;
  }

  public boolean isHasReservedPlace() {
    return hasReservedPlace;
  }

  public void setHasReservedPlace(boolean hasReservedPlace) {
    this.hasReservedPlace = hasReservedPlace;
  }

  public int getDaysTop() {
    return daysTop;
  }

  public void setDaysTop(int daysTop) {
    this.daysTop = daysTop;
  }

  public void setVisits(List<Visit> visits) {
    this.visits = visits;
  }

  public List<Visit> getVisits() {
    return this.visits;
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

  public String getPowerpointEmbedCode() {
    return powerpointEmbedCode;
  }

  public void setPowerpointEmbedCode(String powerpointEmbedCode) {
    this.powerpointEmbedCode = powerpointEmbedCode;
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

  public List<User> getBackers() {
    return backers;
  }

  public void setBackers(List<User> backers) {
    this.backers = backers;
  }

  public List<Update> getUpdates() {
    return updates;
  }

  public void setUpdates(List<Update> updates) {
    this.updates = updates;
  }

  public String getProjectLaunchedDate() {
    for (Update update :
            this.getUpdates()) {
      if (update.getType().equals("Initial update")) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date date = null;
        try {
          date = sdf.parse(update.getDate());
        } catch (ParseException e) {
          e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        assert date != null;
        calendar.setTime(date);
        return Integer.toString(calendar.get(Calendar.DAY_OF_MONTH))
                + " "
                + getMonthNameLong(calendar.get(Calendar.MONTH))
                + " "
                + calendar.get(Calendar.YEAR);
      }
    }

    return null;
  }

  private String getMonthNameLong(int month) {
    switch (month) {
      case 0:
        return "Януари";
      case 1:
        return "Февруари";
      case 2:
        return "Март";
      case 3:
        return "Април";
      case 4:
        return "Май";
      case 5:
        return "Юни";
      case 6:
        return "Юли";
      case 7:
        return "Август";
      case 8:
        return "Септември";
      case 9:
        return "Октомври";
      case 10:
        return "Ноември";
      case 11:
        return "Декември";
      default:
        return null;
    }
  }

  public List<WantedMember> getWantedMembers() {
    return wantedMembers;
  }

  public void setWantedMembers(List<WantedMember> wantedMembers) {
    this.wantedMembers = wantedMembers;
  }

  public boolean isThereWantedParameters(String projectNeed) {
    for (WantedMember wantedMember :
            this.getWantedMembers()) {
      if (wantedMember.getUserCategory().getName().equals(projectNeed)) {
        return true;
      }
    }

    return false;
  }

  public String getWantedMemberId(String projectNeed) {
    for (WantedMember wantedMember :
            this.getWantedMembers()) {
      if (wantedMember.getUserCategory().getName().equals(projectNeed)) {
        return "wanted-member_" + wantedMember.getId();
      }
    }

    return "";
  }

  @Override
  public String toString() {
    return "Project{" +
            "wantedMembers=" + wantedMembers +
            '}';
  }
}
