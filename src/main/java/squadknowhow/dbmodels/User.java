package squadknowhow.dbmodels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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
import squadknowhow.response.models.GroupMemberOf;

@Entity
@Table(name = "users")
public class User implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "email")
  private String email;
  @Column(name = "password")
  private String password;
  @Column(name = "first_name")
  private String firstName;
  @Column(name = "last_name")
  private String lastName;
  @Column(name = "description")
  private String description;
  @ManyToOne
  private City city;
  @ManyToOne
  private UserCategory skillset;
  @Column(name = "github_username")
  private String githubUsername;
  @Column(name = "work_sample")
  private String workSample;
  @Column(name = "personal_site")
  private String personalSite;
  @Column(name = "unemployed")
  private boolean unemployed;
  @Column(name = "degree")
  private String degree;
  @ManyToOne
  private Company employer;
  @ManyToMany()
  @LazyCollection(LazyCollectionOption.FALSE)
  @JoinTable(name = "skills_users",
          joinColumns = {@JoinColumn(name = "user_id")},
          inverseJoinColumns = {
                  @JoinColumn(name = "skill_id")})
  private List<Skill> skills;
  @ManyToMany()
  @LazyCollection(LazyCollectionOption.FALSE)
  @JoinTable(name = "interests_users",
          joinColumns = {@JoinColumn(name = "user_id")},
          inverseJoinColumns = {
                  @JoinColumn(name = "interest_id")})
  private List<Interest> interests;
  @ManyToMany()
  @LazyCollection(LazyCollectionOption.FALSE)
  @JoinTable(name = "previous_employments_users", joinColumns = {
          @JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "company_id")})
  private List<Company> previousEmployment;
  @Lob
  @Column(name = "image")
  private byte[] image;
  @ManyToMany()
  @LazyCollection(LazyCollectionOption.FALSE)
  @JoinTable(name = "projects_users",
          joinColumns = {@JoinColumn(name = "user_id")},
          inverseJoinColumns = {
                  @JoinColumn(name = "project_id")})
  private List<Project> projects;
  @ManyToMany()
  @LazyCollection(LazyCollectionOption.FALSE)
  @JoinTable(name = "groups_users",
          joinColumns = {@JoinColumn(name = "user_id")},
          inverseJoinColumns = {
                  @JoinColumn(name = "group_id")})
  @JsonIgnore
  private List<Group> groups;
  @OneToMany(mappedBy = "recipient")
  @JsonManagedReference
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<Message> messages;
  @OneToMany(mappedBy = "sender")
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<Message> sentMessages;
  @OneToMany(mappedBy = "recipient")
  @JsonManagedReference
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<Notification> notifications;
  @OneToMany(mappedBy = "sender")
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<Notification> sentNotifications;
  @OneToMany(mappedBy = "sender")
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<Advice> sentAdvices;
  @Column(name = "needs_tour")
  private Boolean needsTour;

  public User() {

  }

  public User(int id) {
    this.setId(id);
  }

  public User(int id, String firstName) {
    this.setId(id);
    this.setFirstName(firstName);
  }

  public User(String email) {
    this.setEmail(email);
  }

  public User(String email, String password) {
    this.setEmail(email);
    this.setPassword(password);
  }

  public User(int id, String email, String password) {
    this.setId(id);
    this.setEmail(email);
    this.setPassword(password);
  }

  public User(String firstName, String lastName, int id) {
    this.setFirstName(firstName);
    this.setLastName(lastName);
    this.setId(id);
  }

  public User(String firstName,
              String lastName,
              String description,
              String email,
              String password) {
    this.setFirstName(firstName);
    this.setLastName(lastName);
    this.setDescription(description);
    this.setEmail(email);
    this.setPassword(password);
  }

  public User(String firstName, String lastName, UserCategory skillset) {
    this.setFirstName(firstName);
    this.setLastName(lastName);
    this.setSkillset(skillset);
  }

  @JsonProperty
  public List<GroupMemberOf> getGroupMemberOf() {
    List<GroupMemberOf> result = new ArrayList<GroupMemberOf>();
    for (int i = 0; i < getGroups().size(); i++) {
      int projectsCount = 0;
      for (int j = 0; j < getGroups().get(i).getMembers().size(); j++) {
        projectsCount += getGroups().get(i).getMembers().get(j).getProjects().size();
      }

      GroupMemberOf toAdd = new GroupMemberOf(this.getGroups().get(i).getId(),
              this.getGroups().get(i).getLogo(),
              this.getGroups().get(i).getName(),
              this.getGroups().get(i).getMembers().size(), projectsCount);

      result.add(toAdd);
    }

    return result;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public String getGithubUsername() {
    return githubUsername;
  }

  public void setGithubUsername(String githubUsername) {
    this.githubUsername = githubUsername;
  }

  public String getPersonalSite() {
    return personalSite;
  }

  public void setPersonalSite(String personalSite) {
    this.personalSite = personalSite;
  }

  public boolean isUnemployed() {
    return unemployed;
  }

  public void setUnemployed(boolean unemployed) {
    this.unemployed = unemployed;
  }

  public String getDegree() {
    return degree;
  }

  public void setDegree(String degree) {
    this.degree = degree;
  }

  public Company getEmployer() {
    return employer;
  }

  public void setEmployer(Company employer) {
    this.employer = employer;
  }

  public List<Skill> getSkills() {
    return skills;
  }

  public void setSkills(List<Skill> skills) {
    this.skills = skills;
  }

  public List<Interest> getInterests() {
    return interests;
  }

  public void setInterests(List<Interest> interests) {
    this.interests = interests;
  }

  public List<Company> getPreviousEmployment() {
    return previousEmployment;
  }

  public void setPreviousEmployment(List<Company> previousEmployment) {
    this.previousEmployment = previousEmployment;
  }

  public byte[] getImage() {
    return image;
  }

  @JsonIgnore
  public String getBase64Image() {
    return Base64.encodeBase64String(this.getImage());
  }

  public void setImage(byte[] image) {
    this.image = image;
  }

  public String getWorkSample() {
    return workSample;
  }

  public void setWorkSample(String workSample) {
    this.workSample = workSample;
  }

  @Override
  public String toString() {
    return "User [id=" + id + ", email="
            + email + ", password=" + password
            + ", firstName=" + firstName + ", lastName="
            + lastName + ", description="
            + description + ", city=" + city
            + ", githubUsername=" + githubUsername
            + ", workSample=" + workSample
            + ", personalSite=" + personalSite
            + ", unemployed=" + unemployed + ", degree="
            + degree + ", employer=" + employer
            + ", skills=" + skills + ", interests=" + interests
            + ", previousEmployment=" + previousEmployment
            + ", skillset=" + skillset + "]";
  }

  public UserCategory getSkillset() {
    return skillset;
  }

  public void setSkillset(UserCategory skillset) {
    this.skillset = skillset;
  }

  public List<Project> getProjects() {
    return projects;
  }

  public void setProjects(List<Project> projects) {
    this.projects = projects;
  }

  public List<Group> getGroups() {
    return groups;
  }

  public void setGroups(List<Group> groups) {
    this.groups = groups;
  }

  public List<Message> getMessages() {
    return messages;
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

  public List<Message> getSentMessages() {
    return sentMessages;
  }

  public void setSentMessages(List<Message> sentMessages) {
    this.sentMessages = sentMessages;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    User other = (User) obj;
    return id == other.id;
  }

  public List<Notification> getNotifications() {
    return notifications;
  }

  public void setNotifications(List<Notification> notifications) {
    this.notifications = notifications;
  }

  public List<Notification> getSentNotifications() {
    return sentNotifications;
  }

  public void setSentNotifications(List<Notification> sentNotifications) {
    this.sentNotifications = sentNotifications;
  }

  public List<Advice> getSentAdvices() {
    return sentAdvices;
  }

  public void setSentAdvices(List<Advice> sentAdvices) {
    this.sentAdvices = sentAdvices;
  }

  public Boolean isNeedsTour() {
    return needsTour;
  }

  public void setNeedsTour(Boolean needsTour) {
    this.needsTour = needsTour;
  }

  @JsonIgnore
  public String getProfileInterests() {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < this.getInterests().size(); i++) {
      result.append(this.getInterests().get(i).getName());
      if (i != this.getInterests().size() - 1) {
        result.append(", ");
      }
    }

    if (this.getInterests().size() == 0) {
      result.append("Няма въведени интереси");
    }

    return result.toString();
  }

  @JsonIgnore
  public String getProfileSkills() {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < this.getSkills().size(); i++) {
      result.append(this.getSkills().get(i).getName());
      if (i != this.getSkills().size() - 1) {
        result.append(", ");
      }
    }

    if (this.getSkills().size() == 0) {
      result.append("Няма въведени умения");
    }

    return result.toString();
  }

  @JsonIgnore
  public String getPreviousEmploymentString() {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < this.getPreviousEmployment().size(); i++) {
      result.append(this.getPreviousEmployment().get(i).getName());
      if (i != this.getPreviousEmployment().size() - 1) {
        result.append(", ");
      }
    }

    if (this.getPreviousEmployment().size() == 0) {
      result.append("Няма предишни работни места");
    }

    return result.toString();
  }

  @JsonIgnore
  public List<Project> getCreatedProjects() {
    List<Project> result = this.getProjects().stream()
            .filter(pr -> pr.getCreator() == this.getId())
            .collect(Collectors.toList());
    Collections.reverse(result);
    return result;
  }

  @JsonIgnore
  public List<Project> getMemberOfProjects() {
    List<Project> result = this.getProjects().stream()
            .filter(pr -> pr.getCreator() != this.getId())
            .collect(Collectors.toList());
    Collections.reverse(result);
    return result;
  }

  @JsonIgnore
  public List<Message> getMessagesReverse() {
    List<Message> result = this.getMessages();
    Collections.reverse(result);
    return result;
  }

  @JsonIgnore
  public List<Message> getSentMessagesReverse() {
    List<Message> result = this.getSentMessages();
    Collections.reverse(result);
    return result;
  }
}