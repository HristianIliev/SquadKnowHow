package squadknowhow.request.models;

import java.util.List;

public class Parameter {
  private String category;
  private String paramCategory;
  private List<String> values;

  public Parameter() {

  }

  public Parameter(String category, String paramCategory, List<String> values) {
    this.setCategory(category);
    this.setParamCategory(paramCategory);
    this.setValues(values);
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }

  public String getParamCategory() {
    return paramCategory;
  }

  public void setParamCategory(String paramCategory) {
    this.paramCategory = paramCategory;
  }

  @Override
  public String toString() {
    StringBuilder values = new StringBuilder();
    for (String value
            : this.getValues()) {
      values.append(value).append(", ");
    }

    return "Parameter{"
            + "category='" + category + '\''
            + ", paramCategory='" + paramCategory
            + '\''
            + ", values=" + values.toString()
            + '}';
  }
}
