package squadknowhow.services;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import squadknowhow.contracts.IRepository;
import squadknowhow.contracts.ITagsService;
import squadknowhow.dbmodels.City;
import squadknowhow.dbmodels.Company;
import squadknowhow.dbmodels.Interest;
import squadknowhow.dbmodels.Skill;
import squadknowhow.utils.HibernateUtils;

@Service
public class DbTagsService implements ITagsService {
  private IRepository<City> citiesRepository;
  private IRepository<Skill> skillsRepository;
  private IRepository<Interest> interestsRepository;
  private IRepository<Company> companiesRepository;

  @Autowired
  public DbTagsService(IRepository<City> citiesRepository,
                       IRepository<Skill> skillsRepository,
                       IRepository<Interest> interestsRepository,
                       IRepository<Company> companiesRepository) {
    this.citiesRepository = citiesRepository;
    this.skillsRepository = skillsRepository;
    this.interestsRepository = interestsRepository;
    this.companiesRepository = companiesRepository;
  }

  @Override
  public HashSet<String> getCities(String term) {
    if (term.length() < 2) {
      throw new InvalidParameterException("Term is not valid");
    }

    List<City> cities = this.citiesRepository.getAll().stream()
            .filter(city -> city.getName().toLowerCase()
                    .contains(term.toLowerCase()))
            .collect(Collectors.toList());
    HashSet<String> result = new HashSet<>();
    for (City city : cities) {
      result.add(city.getName());
    }

    if (result.size() == 0) {
      result.add("Не съществува такъв резултат");
    }

    return result;
  }

  @Override
  public HashSet<String> getSkills(String term) {
    if (term.length() < 2) {
      throw new InvalidParameterException("Term is not valid");
    }

    List<Skill> skills = this.skillsRepository.getAll().stream()
            .filter(skill -> skill.getName().toLowerCase()
                    .contains(term.toLowerCase()))
            .collect(Collectors.toList());
    HashSet<String> result = new HashSet<>();
    for (Skill skill : skills) {
      result.add(skill.getName());
    }

    if (result.size() == 0) {
      result.add("Не съществува такъв резултат");
    }

    return result;
  }

  @Override
  public HashSet<String> getInterests(String term) {
    if (term.length() < 2) {
      throw new InvalidParameterException("Term is not valid");
    }

    List<Interest> interests = this.interestsRepository.getAll().stream()
            .filter(interest -> interest.getName().toLowerCase()
                    .contains(term.toLowerCase()))
            .collect(Collectors.toList());
    HashSet<String> result = new HashSet<>();
    for (Interest interest : interests) {
      result.add(interest.getName());
    }

    if (result.size() == 0) {
      result.add("Не съществува такъв резултат");
    }

    return result;
  }

  @Override
  public HashSet<String> getEmployedBy(String term) {
    if (term.length() < 2) {
      throw new InvalidParameterException("Term is not valid");
    }

    List<Company> companies = this.companiesRepository.getAll().stream()
            .filter(company -> company.getName().toLowerCase()
                    .contains(term.toLowerCase()))
            .collect(Collectors.toList());
    HashSet<String> result = new HashSet<>();
    for (Company company : companies) {
      result.add(company.getName());
    }

    if (result.size() == 0) {
      result.add("Не съществува такъв резултат");
    }

    return result;
  }

}
