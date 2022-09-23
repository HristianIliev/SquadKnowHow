package squadknowhow.request.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jsoup.Jsoup;
import squadknowhow.dbmodels.Advice;
import squadknowhow.dbmodels.Question;
import squadknowhow.dbmodels.TodoListEntry;
import squadknowhow.dbmodels.Update;
import squadknowhow.dbmodels.User;
import squadknowhow.dbmodels.UserCategory;
import squadknowhow.dbmodels.Visit;
import squadknowhow.dbmodels.WantedMember;
import squadknowhow.response.models.ProjectMember;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CreateProject {
  private int id;
  private String name;
  private String description;
  private String goal1;
  private String goal2;
  private String goal3;
  private Integer creator;
  private String githubPage;
  private List<Visit> visits;
  private String city;
  private List<UserCategory> projectNeeds;
  private List<User> members;
  private List<User> favoritedUsers;
  private String cover;
  private String youtubeLink;
  private String picture1;
  private String picture2;
  private String picture3;
  private String picture4;
  private String picture5;
  private String picture6;
  private String picture7;
  private String picture8;
  private String picture9;
  private List<Advice> advices;
  private List<Question> questions;
  private List<TodoListEntry> listEntries;
  private String telephone;
  private double neededMoney;
  private double receivedMoney;
  private boolean needsMoney;
  private boolean hasReservedPlace;
  private int daysTop;
  private Double latitude;
  private Double longitude;
  private String powerpointEmbedCode;
  private int timesBacked;
  private boolean isTopProject;
  private List<User> backers;
  private List<Update> updates;
  private List<WantedMember> wantedMembers;
  private List<Parameter> parameters;

  public CreateProject() {

  }

  public CreateProject(int id) {
    this.setId(id);
  }

  public CreateProject(String name, int id) {
    this.setId(id);
    this.setName(name);
  }

  public CreateProject(int id, String name, String description) {
    this.setId(id);
    this.setName(name);
    this.setDescription(description);
  }

  public CreateProject(int id, String name, String description, String goal1) {
    this.setId(id);
    this.setName(name);
    this.setDescription(description);
    this.setGoal1(goal1);
  }

  public CreateProject(String name) {
    this.setName(name);
  }

  public CreateProject(String name, String description) {
    this.setName(name);
    this.setDescription(description);
  }

  public CreateProject(String name, String description, String goal1) {
    this.setName(name);
    this.setDescription(description);
    this.setGoal1(goal1);
  }

  public CreateProject(String name,
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

  public CreateProject(int creator, List<User> members) {
    this.setCreator(creator);
    this.setMembers(members);
  }

  public CreateProject(int creator, String name) {
    this.setCreator(creator);
    this.setName(name);
  }

  public CreateProject(int creator, List<User> members, String name) {
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
      result.append("This project doesn't need anyone at the moment");
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
            + projectNeeds
            + ", members=" + members + ", visits=" + visits + "]";
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
    for (User member
            : this.getMembers()) {
      if (member.getId() == id) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    squadknowhow.dbmodels.Project project = (squadknowhow.dbmodels.Project) o;

    if (getId() != project.getId()) {
      return false;
    }
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
    for (Update update
            : this.getUpdates()) {
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
                + " " + getMonthNameLong(calendar.get(Calendar.MONTH))
                + " " + calendar.get(Calendar.YEAR);
      }
    }

    return null;
  }

  private String getMonthNameLong(int month) {
    switch (month) {
      case 0:
        return "January";
      case 1:
        return "February";
      case 2:
        return "March";
      case 3:
        return "April";
      case 4:
        return "May";
      case 5:
        return "June";
      case 6:
        return "July";
      case 7:
        return "August";
      case 8:
        return "September";
      case 9:
        return "October";
      case 10:
        return "November";
      case 11:
        return "December";
      default:
        return null;
    }
  }

  public List<Parameter> getParameters() {
    return parameters;
  }

  public void setParameters(List<Parameter> parameters) {
    this.parameters = parameters;
  }

  public List<WantedMember> getWantedMembers() {
    return wantedMembers;
  }

  public void setWantedMembers(List<WantedMember> wantedMembers) {
    this.wantedMembers = wantedMembers;
  }
}
