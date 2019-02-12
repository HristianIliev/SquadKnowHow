package squadknowhow.dbmodels;

import squadknowhow.contracts.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "interests")
public class Interest implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "name")
  private String name;

  public Interest() {

  }

  public Interest(String name) {
    this.setName(name);
  }

  public Interest(int id, String name) {
    this.setId(id);
    this.setName(name);
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Interest interest = (Interest) o;

    if (getId() != interest.getId()) {
      return false;
    }
    return getName() != null
            ? getName().equals(interest.getName())
            : interest.getName() == null;
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Interest [id=" + id + ", name=" + name + "]";
  }
}
