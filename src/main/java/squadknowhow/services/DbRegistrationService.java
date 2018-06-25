package squadknowhow.services;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import squadknowhow.contracts.IRegistrationService;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.City;
import squadknowhow.dbmodels.Company;
import squadknowhow.dbmodels.Interest;
import squadknowhow.dbmodels.Skill;
import squadknowhow.dbmodels.User;
import squadknowhow.dbmodels.UserCategory;
import squadknowhow.response.models.ResponseRegister;
import squadknowhow.response.models.ResponseUpload;
import squadknowhow.utils.FileUtils;
import squadknowhow.utils.validators.base.IValidator;

@Service
public class DbRegistrationService implements IRegistrationService {
  private final IValidator<Integer> idValidator;
  private final IRepository<User> usersRepository;
  private final IRepository<City> citiesRepository;
  private final IRepository<Skill> skillsRepository;
  private final IRepository<Interest> interestsRepository;
  private final IRepository<UserCategory> userCategoriesRepository;
  private final IRepository<Company> companiesRepository;
  private final IValidator<User> userValidator;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public DbRegistrationService(IRepository<User> usersRepository,
                               IRepository<City> citiesRepository,
                               IRepository<Skill> skillsRepository,
                               IRepository<Interest> interestsRepository,
                               IRepository<UserCategory> userCategoriesRepository,
                               IRepository<Company> companiesRepository,
                               IValidator<Integer> idValidator,
                               IValidator<User> userValidator,
                               PasswordEncoder passwordEncoder) {
    this.usersRepository = usersRepository;
    this.citiesRepository = citiesRepository;
    this.skillsRepository = skillsRepository;
    this.interestsRepository = interestsRepository;
    this.userCategoriesRepository = userCategoriesRepository;
    this.companiesRepository = companiesRepository;
    this.idValidator = idValidator;
    this.userValidator = userValidator;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public ResponseUpload uploadImage(MultipartFile multipart, int id) throws IOException {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    File file = FileUtils.convertToFile(multipart);
    byte[] bytesFile = FileUtils.toByteArray(file);

    User user = this.usersRepository.getById(id);

    user.setImage(bytesFile);
    this.usersRepository.update(user);

    if (file != null) {
      file.delete();
    }

    return new ResponseUpload(true);
  }

  @Override
  public boolean isEmailFree(String email) {
    User user = this.usersRepository.getAll().stream()
            .filter(u -> u.getEmail().equals(email))
            .findFirst()
            .orElse(null);

    return user == null;
  }

  @Override
  public ResponseRegister attemptRegister(User newUser) {
    if (!this.userValidator.isValid(newUser)) {
      throw new InvalidParameterException("NewUser is not valid");
    }

    User userToInsert = new User();
    userToInsert.setFirstName(newUser.getFirstName());
    userToInsert.setLastName(newUser.getLastName());
    userToInsert.setEmail(newUser.getEmail());
    userToInsert.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
    userToInsert.setDescription(newUser.getDescription());
    if (newUser.getCity().getName() != null && !Objects.equals(newUser.getCity().getName(), "")) {
      userToInsert.setCity(this.getCity(newUser.getCity()));
    }

    userToInsert.setSkillset(this.getSkillset(newUser.getSkillset()));
    userToInsert.setDegree(newUser.getDegree());
    userToInsert.setGithubUsername(newUser.getGithubUsername());
    userToInsert.setPersonalSite(newUser.getPersonalSite());
    if (newUser.getEmployer().getName() != null
            && !Objects.equals(newUser.getEmployer().getName(), "")) {
      userToInsert.setEmployer(this.getCompany(newUser.getEmployer()));
    }

    userToInsert.setUnemployed(newUser.isUnemployed());

    List<Skill> skillsToInsert = new ArrayList<>();
    for (int i = 0; i < newUser.getSkills().size(); i++) {
      if (newUser.getSkills().get(i).getName() != null
              && !Objects.equals(newUser.getSkills().get(i).getName(), "")) {
        Skill skillToInsert = this.getSkill(newUser.getSkills().get(i));
        skillsToInsert.add(skillToInsert);
      }
    }

    userToInsert.setSkills(skillsToInsert);

    List<Interest> interestsToInsert = new ArrayList<>();
    for (int i = 0; i < newUser.getInterests().size(); i++) {
      if (newUser.getInterests().get(i).getName() != null
              && !Objects.equals(newUser.getInterests().get(i).getName(), "")) {
        Interest interestToInsert = this.getInterest(newUser.getInterests().get(i));
        interestsToInsert.add(interestToInsert);
      }
    }

    userToInsert.setInterests(interestsToInsert);

    List<Company> previousEmploymentToInsert = new ArrayList<>();
    for (int i = 0; i < newUser.getInterests().size(); i++) {
      if (newUser.getPreviousEmployment().get(i).getName() != null
              && !Objects.equals(newUser.getPreviousEmployment().get(i).getName(), "")) {
        Company companyToInsert = this.getCompany(newUser.getPreviousEmployment().get(i));
        previousEmploymentToInsert.add(companyToInsert);
      }
    }

    userToInsert.setPreviousEmployment(previousEmploymentToInsert);
    userToInsert.setWorkSample(newUser.getWorkSample());
    userToInsert.setNeedsTour(true);

    this.usersRepository.create(userToInsert);

    return new ResponseRegister(userToInsert.getId());
  }

  private UserCategory getSkillset(UserCategory skillset) {
    return this.userCategoriesRepository.getAll().stream()
            .filter(uc -> uc.getName().equals(skillset.getName())).findFirst()
            .orElse(null);
  }

  private Interest getInterest(Interest interest) {
    return this.interestsRepository.getAll().stream()
            .filter(in -> in.getName().equals(interest.getName()))
            .findFirst()
            .orElse(null);
  }

  private Skill getSkill(Skill skill) {
    return this.skillsRepository.getAll().stream()
            .filter(sk -> sk.getName().equals(skill.getName()))
            .findFirst()
            .orElse(null);
  }

  private Company getCompany(Company employer) {
    return this.companiesRepository.getAll().stream()
            .filter(comp -> comp.getName().equals(employer.getName()))
            .findFirst()
            .orElse(null);
  }

  private City getCity(City city) {
    return this.citiesRepository.getAll().stream()
            .filter(cit -> cit.getName().equals(city.getName()))
            .findFirst()
            .orElse(null);
  }
}
