package squadknowhow.dbmodels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import squadknowhow.contracts.Model;

@Entity
@Table(name = "user_categories")
public class UserCategory implements Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "name")
  private String name;

  public UserCategory() {

  }

  public UserCategory(final String name) {
    this.setName(name);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UserCategory that = (UserCategory) o;

    return getId() == that.getId()
            && (getName() != null
            ? getName().equals(that.getName())
            : that.getName() == null);
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    return result;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public int getId() {
    return id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return this.getName();
  }
}
