package squadknowhow.dbmodels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import squadknowhow.contracts.Model;

@Entity
@Table(name = "skills")
public class Skill implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "name")
  private String name;

  public Skill() {

  }

  public Skill(String name) {

    this.setName(name);
  }

  public Skill(int id, String name) {
    this.setId(id);
    this.setName(name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Skill skill = (Skill) o;

    if (getId() != skill.getId()) {
      return false;
    }
    return getName() != null
            ? getName().equals(skill.getName())
            : skill.getName() == null;
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    return result;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Skill [id=" + id + ", name=" + name + "]";
  }

  public String getName() {
    return this.name;
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

}
