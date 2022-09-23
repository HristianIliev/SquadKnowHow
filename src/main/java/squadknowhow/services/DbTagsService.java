package squadknowhow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import squadknowhow.contracts.IRepository;
import squadknowhow.contracts.ITagsService;
import squadknowhow.dbmodels.City;
import squadknowhow.dbmodels.Company;
import squadknowhow.dbmodels.Interest;
import squadknowhow.dbmodels.Language;
import squadknowhow.dbmodels.Skill;
import squadknowhow.dbmodels.User;
import squadknowhow.request.models.Parameter;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DbTagsService implements ITagsService {
  private final IRepository<Language> languagesRepository;
  private final IRepository<City> citiesRepository;
  private final IRepository<Skill> skillsRepository;
  private final IRepository<Interest> interestsRepository;
  private final IRepository<Company> companiesRepository;
  private final IRepository<User> usersRepositroy;

  @Autowired
  public DbTagsService(IRepository<City> citiesRepository,
                       IRepository<Skill> skillsRepository,
                       IRepository<Interest> interestsRepository,
                       IRepository<Company> companiesRepository,
                       IRepository<Language> languagesRepository,
                       IRepository<User> usersRepository) {
    this.citiesRepository = citiesRepository;
    this.skillsRepository = skillsRepository;
    this.interestsRepository = interestsRepository;
    this.companiesRepository = companiesRepository;
    this.languagesRepository = languagesRepository;
    this.usersRepositroy = usersRepository;
  }

  @Override
  public List<String> getEmails(String term) {
    if (term.length() < 2) {
      throw new InvalidParameterException("Term is not valid");
    }

    List<String> emails = this.usersRepositroy.getAll().stream()
            .filter(user -> user.getEmail().toLowerCase()
                    .contains(term.toLowerCase()))
            .map(User::getEmail)
            .collect(Collectors.toList());

    if (emails.size() == 0) {
      emails.add("Не съществува такъв резултат");
    }

    return emails;
  }

  @Override
  public boolean checkIfTagsAreCorrect(List<Parameter> parameters) {
    for (Parameter parameter
            : parameters) {
      List<String> values = parameter.getValues();
      switch (parameter.getParamCategory()) {
        case "skills":
          List<Skill> skills = new ArrayList<>();
          for (String skill
                  : values) {
            System.out.println(skill);
            if (!isSkillCorrect(skill)) {
              return false;
            }

            for (Skill skill1
                    : skills) {
              if (skill1.getName().equals(skill)) {
                return false;
              }
            }

            skills.add(new Skill(skill));
          }
          break;
        case "languages":
          List<Language> languages = new ArrayList<>();
          for (String language
                  : values) {
            if (!isLanguageCorrect(language)) {
              return false;
            }

            for (Language language1
                    : languages) {
              if (language1.getName().equals(language)) {
                return false;
              }
            }

            languages.add(new Language(language));
          }
          break;
        case "city":
          for (String city
                  : values) {
            System.out.println(city);
            if (!isCityCorrect(city)) {
              return false;
            }
          }
          break;
        default:
          break;
      }
    }

    System.out.println("Returning true");
    return true;
  }

  private boolean isSkillCorrect(String skill) {
    Skill result = this.skillsRepository.getAll().stream()
            .filter(s -> s.getName().equals(skill))
            .findFirst()
            .orElse(null);

    return result != null;
  }

  private boolean isLanguageCorrect(String language) {
    Language result = this.languagesRepository.getAll().stream()
            .filter(l -> l.getName().equals(language))
            .findFirst()
            .orElse(null);

    return result != null;
  }

  private boolean isCityCorrect(String city) {
    City result = this.citiesRepository.getAll().stream()
            .filter(c -> c.getName().equals(city))
            .findFirst()
            .orElse(null);

    return result != null;
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
      result.add("No such city");
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
      result.add("No such skill");
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
      result.add("No such interest");
    }

    return result;
  }

  @Override
  public HashSet<String> getLanguages(String term) {
    if (term.length() < 2) {
      throw new InvalidParameterException("Term is not valid");
    }

    List<Language> languages = this.languagesRepository.getAll().stream()
            .filter(language -> language.getName().toLowerCase()
                    .contains(term.toLowerCase()))
            .collect(Collectors.toList());
    HashSet<String> result = new HashSet<>();
    for (Language language : languages) {
      result.add(language.getName());
    }

    if (result.size() == 0) {
      result.add("No such language");
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
      result.add("No such employment");
    }

    return result;
  }
}
