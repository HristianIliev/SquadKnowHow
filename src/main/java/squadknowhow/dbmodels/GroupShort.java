package squadknowhow.dbmodels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "groups")
public class GroupShort implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "name")
  private String name;
  @Column(name = "description")
  private String description;
  @Column(name = "logo")
  private String logo;
  @Column(name = "group_type")
  private String groupType;
  @ManyToMany()
  @LazyCollection(LazyCollectionOption.FALSE)
  @JoinTable(name = "groups_users",
          joinColumns = {@JoinColumn(name = "group_id")},
          inverseJoinColumns = {
                  @JoinColumn(name = "user_id")})
  private List<User> members;

  public GroupShort() {

  }

  public GroupShort(int id) {
    this.setId(id);
  }

  public GroupShort(String name) {
    this.setName(name);
  }

  public GroupShort(List<User> members) {
    this.setMembers(members);
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

  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }

  public String getGroupType() {
    return groupType;
  }

  public void setGroupType(String groupType) {
    this.groupType = groupType;
  }

  @JsonIgnore
  public List<User> getMembers() {
    return members;
  }

  public void setMembers(List<User> members) {
    this.members = members;
  }

  @JsonProperty
  public int getMembersCount() {
    return this.members.size();
  }

  @JsonProperty
  public int getProjectsCount() {
    int result = 0;
    for (int i = 0; i < this.getMembers().size(); i++) {
      result += this.getMembers().get(i).getProjects().size();
    }

    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    GroupShort that = (GroupShort) o;

    if (getId() != that.getId()) return false;
    if (getName() != null
            ? !getName().equals(that.getName())
            : that.getName() != null) return false;
    if (getDescription() != null
            ? !getDescription().equals(that.getDescription())
            : that.getDescription() != null)
      return false;
    if (getGroupType() != null
            ? !getGroupType().equals(that.getGroupType())
            : that.getGroupType() != null)
      return false;
    return getMembers() != null
            ? getMembers().equals(that.getMembers())
            : that.getMembers() == null;
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + (getDescription() != null
            ? getDescription().hashCode() : 0);
    result = 31 * result;
    result = 31 * result + (getGroupType() != null
            ? getGroupType().hashCode() : 0);
    result = 31 * result + (getMembers() != null ? getMembers().hashCode() : 0);
    return result;
  }
}
