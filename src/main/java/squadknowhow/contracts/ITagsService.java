package squadknowhow.contracts;

import java.util.HashSet;
import java.util.List;

import squadknowhow.dbmodels.City;

public interface ITagsService {
  HashSet<String> getCities(String term);

  HashSet<String> getSkills(String term);

  HashSet<String> getInterests(String term);

  HashSet<String> getEmployedBy(String term);
}


