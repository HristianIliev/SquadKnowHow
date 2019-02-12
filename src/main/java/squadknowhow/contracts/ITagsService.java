package squadknowhow.contracts;

import squadknowhow.request.models.Parameter;

import java.util.HashSet;
import java.util.List;

public interface ITagsService {
  HashSet<String> getCities(String term);

  HashSet<String> getSkills(String term);

  HashSet<String> getInterests(String term);

  HashSet<String> getEmployedBy(String term);

  HashSet<String> getLanguages(String term);

  List<String> getEmails(String term);

  boolean checkIfTagsAreCorrect(List<Parameter> parameters);
}


