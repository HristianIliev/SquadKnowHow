package squadknowhow.request.models;

import squadknowhow.dbmodels.City;
import squadknowhow.dbmodels.Company;
import squadknowhow.dbmodels.Interest;
import squadknowhow.dbmodels.Skill;

public class Registration {
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String description;
  private City city;
  private Skill[] skills;
  private String degree;
  private Interest[] interests;
  private String githubUsername;
  private String personalSite;
  private Company employer;
  private boolean unemployed;
  private Company[] previousEmployment;

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

  public Skill[] getSkills() {
    return skills;
  }

  public void setSkills(Skill[] skills) {
    this.skills = skills;
  }

  public String getDegree() {
    return degree;
  }

  public void setDegree(String degree) {
    this.degree = degree;
  }

  public Interest[] getInterests() {
    return interests;
  }

  public void setInterests(Interest[] interests) {
    this.interests = interests;
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

  public Company getEmployer() {
    return employer;
  }

  public void setEmployer(Company employer) {
    this.employer = employer;
  }

  public boolean isUnemployed() {
    return unemployed;
  }

  public void setUnemployed(boolean unemployed) {
    this.unemployed = unemployed;
  }

  public Company[] getPreviousEmployment() {
    return previousEmployment;
  }

  public void setPreviousEmployment(Company[] previousEmployment) {
    this.previousEmployment = previousEmployment;
  }
}
