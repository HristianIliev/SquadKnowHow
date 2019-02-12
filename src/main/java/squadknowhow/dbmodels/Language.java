package squadknowhow.dbmodels;

import squadknowhow.contracts.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "languages")
public class Language implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "name")
  private String name;

  public Language() {

  }

  public Language(String name) {
    this.setName(name);
  }

  public Language(int id, String name) {
    this.setId(id);
    this.setName(name);
  }

  public int getId() {
    return id;
  }

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
  public String toString() {
    return "Language [id=" + id + ", name=" + name + "]";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Language city = (Language) o;

    return getId() == city.getId()
            && (getName() != null
            ? getName().equals(city.getName()) : city.getName() == null);
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    return result;
  }
}
