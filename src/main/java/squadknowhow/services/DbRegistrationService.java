package squadknowhow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import squadknowhow.contracts.IRegistrationService;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.City;
import squadknowhow.dbmodels.Company;
import squadknowhow.dbmodels.Interest;
import squadknowhow.dbmodels.Language;
import squadknowhow.dbmodels.Skill;
import squadknowhow.dbmodels.User;
import squadknowhow.dbmodels.UserCategory;
import squadknowhow.request.models.EditedUser;
import squadknowhow.response.models.ResponseRegister;
import squadknowhow.response.models.ResponseUpload;
import squadknowhow.utils.FileUtils;
import squadknowhow.utils.validators.base.IValidator;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Service
public class DbRegistrationService implements IRegistrationService {
  private static final String IS_LOCAL = System.getenv("IS_LOCAL");
  private static final String EMAIL_PASS = System.getenv("EMAIL_PASS");

  private final IValidator<Integer> idValidator;
  private final IRepository<User> usersRepository;
  private final IRepository<City> citiesRepository;
  private final IRepository<Skill> skillsRepository;
  private final IRepository<Interest> interestsRepository;
  private final IRepository<UserCategory> userCategoriesRepository;
  private final IRepository<Company> companiesRepository;
  private final IValidator<User> userValidator;
  private final PasswordEncoder passwordEncoder;
  private final IRepository<Language> languagesRepository;
  private final IValidator<EditedUser> editedUserValidator;

  @Autowired
  public DbRegistrationService(IRepository<User> usersRepository,
                               IRepository<City> citiesRepository,
                               IRepository<Skill> skillsRepository,
                               IRepository<Interest> interestsRepository,
                               IRepository<UserCategory>
                                       userCategoriesRepository,
                               IRepository<Company> companiesRepository,
                               IValidator<Integer> idValidator,
                               IValidator<User> userValidator,
                               PasswordEncoder passwordEncoder,
                               IRepository<Language> languagesRepository,
                               IValidator<EditedUser> editedUserValidator) {
    this.usersRepository = usersRepository;
    this.citiesRepository = citiesRepository;
    this.skillsRepository = skillsRepository;
    this.interestsRepository = interestsRepository;
    this.userCategoriesRepository = userCategoriesRepository;
    this.companiesRepository = companiesRepository;
    this.idValidator = idValidator;
    this.userValidator = userValidator;
    this.passwordEncoder = passwordEncoder;
    this.languagesRepository = languagesRepository;
    this.editedUserValidator = editedUserValidator;
  }

  @Override
  public ResponseUpload uploadImage(MultipartFile multipart,
                                    int id) throws IOException {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    String path = FileUtils.convertToFilepath(multipart);
    int indexOf = path.indexOf("/static");
    if (indexOf != -1) {
      path = path.substring(path.indexOf("/static"));
    }

    User user = this.usersRepository.getById(id);

    user.setImage(path);
    this.usersRepository.update(user);

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
  public boolean areTagsCorrect(User tags) {
    if (tags.getCity().getName() != null
            && !Objects.equals(tags.getCity().getName(), "")) {
      if (this.getCity(tags.getCity()) == null) {
        return false;
      }
    }

    if (tags.getEmployer().getName() != null
            && !Objects.equals(tags.getEmployer().getName(), "")) {
      if (this.getCompany(tags.getEmployer()) == null) {
        return false;
      }
    }

    List<Skill> alreadyUsed = new ArrayList<>();
    for (int i = 0; i < tags.getSkills().size(); i++) {
      if (tags.getSkills().get(i).getName() != null
              && !Objects.equals(tags.getSkills().get(i).getName(), "")) {
        Skill skill = this.getSkill(tags.getSkills().get(i));
        if (skill == null) {
          return false;
        }

        for (Skill used
                : alreadyUsed) {
          if (used.getId() == skill.getId()) {
            return false;
          }
        }

        alreadyUsed.add(skill);
      }
    }

    List<Interest> alreadyUsedInterests = new ArrayList<>();
    for (int i = 0; i < tags.getInterests().size(); i++) {
      if (tags.getInterests().get(i).getName() != null
              && !Objects.equals(tags.getInterests().get(i).getName(), "")) {
        Interest interest = this.getInterest(tags.getInterests().get(i));
        if (interest == null) {
          return false;
        }

        for (Interest used
                : alreadyUsedInterests) {
          if (used.getId() == interest.getId()) {
            return false;
          }
        }

        alreadyUsedInterests.add(interest);
      }
    }

    List<Language> alreadyUsedLanguages = new ArrayList<>();
    for (int i = 0; i < tags.getLanguages().size(); i++) {
      if (tags.getLanguages().get(i).getName() != null
              && !Objects.equals(tags.getLanguages().get(i).getName(), "")) {
        Language language = this.getLanguage(tags.getLanguages().get(i));
        if (language == null) {
          return false;
        }

        for (Language used
                : alreadyUsedLanguages) {
          if (used.getId() == language.getId()) {
            return false;
          }
        }

        alreadyUsedLanguages.add(language);
      }
    }

    List<Company> alreadyUsedCompanies = new ArrayList<>();
    for (int i = 0; i < tags.getInterests().size(); i++) {
      if (tags.getPreviousEmployment().get(i).getName() != null
              && !Objects.equals(tags.getPreviousEmployment().get(i).getName(), "")) {
        Company company = this.getCompany(tags.getPreviousEmployment().get(i));
        if (company == null) {
          return false;
        }

        for (Company used
                : alreadyUsedCompanies) {
          if (used.getId() == company.getId()) {
            return false;
          }
        }

        alreadyUsedCompanies.add(company);
      }
    }

    return true;
  }

  @Override
  public ResponseRegister attemptRegister(User newUser) {
    if (!this.userValidator.isValid(newUser)) {
      throw new InvalidParameterException("NewUser is not valid");
    }

    User userToInsert = new User();
    userToInsert.setOnline(false);
    userToInsert.setFirstName(newUser.getFirstName());
    userToInsert.setLastName(newUser.getLastName());
    userToInsert.setEmail(newUser.getEmail());
    userToInsert.setPassword(
            this.passwordEncoder.encode(newUser.getPassword()));
    userToInsert.setDescription(newUser.getDescription());
    if (newUser.getCity().getName() != null
            && !Objects.equals(newUser.getCity().getName(), "")) {
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

    List<Language> languagesToInsert = new ArrayList<>();
    for (int i = 0; i < newUser.getLanguages().size(); i++) {
      System.out.println(newUser.getLanguages().get(i).getName());
      if (newUser.getLanguages().get(i).getName() != null
              && !Objects.equals(newUser.getLanguages().get(i).getName(), "")) {
        Language languageToInsert = this.getLanguage(newUser.getLanguages().get(i));
        languagesToInsert.add(languageToInsert);
      }
    }

    userToInsert.setLanguages(languagesToInsert);

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
    userToInsert.setMoney(0.00);
    userToInsert.setSomeoneIsCalling(0);
    userToInsert.setOnlyAudio(false);
    userToInsert.setRating(0.0);
    userToInsert.setCommunication(0);
    userToInsert.setInitiative(0);
    userToInsert.setLeadership(0);
    userToInsert.setInnovation(0);
    userToInsert.setResponsibility(0);
    userToInsert.setJobsPercent(0);
    userToInsert.setRecommendations(0);
    userToInsert.setImage("/static/all-images/Portrait_Placeholder.png");
    userToInsert.setActivated(false);
    userToInsert.setActivationKey(this.createActivationKey(userToInsert.getEmail()));
    Date now = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    userToInsert.setDateCreated(sdf.format(now));

    this.usersRepository.create(userToInsert);

    this.sendActivationEmail(userToInsert.getEmail(), userToInsert.getActivationKey());

    return new ResponseRegister(userToInsert.getId());
  }

  private String createActivationKey(String email) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] messageDigest = md.digest(email.getBytes());
      BigInteger no = new BigInteger(1, messageDigest);

      // Convert message digest into hex value
      String hashtext = no.toString(16);
      while (hashtext.length() < 32) {
        hashtext = "0" + hashtext;
      }
      return hashtext;
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    }
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

  private Language getLanguage(Language language) {
    return this.languagesRepository.getAll().stream()
            .filter(lang -> lang.getName().equals(language.getName()))
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

  // Sends email to the user with the code.
  @Override
  public boolean forgotPassword(String email) {
    User user = this.getUserByEmail(email);

    Properties props = new Properties();
    props.put("mail.smtp.host", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.auth", "true");
    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(
                "hristian00i.dev@gmail.com",
                EMAIL_PASS);
      }
    });
    try {
      MimeMessage msg = new MimeMessage(session);
      InternetAddress[] address = InternetAddress.parse(email, true);
      msg.setRecipients(javax.mail.Message.RecipientType.TO, address);
      msg.setSubject("Сменяне на парола");
      msg.setSentDate(new Date());
      if (IS_LOCAL.equals("false")) {
        msg.setText("Ако искаш да промениш паролата"
                + " си последвай следващия линк: "
                + "https://squadknowhow.herokuapp.com/reset-password/"
                + user.getId());
      } else {
        msg.setText("Ако искаш да промениш паролата"
                + " си последвай следващия линк: "
                + "localhost:3000/reset-password/"
                + user.getId());
      }
      msg.setHeader("XPriority", "1");
      Transport.send(msg);
      System.out.println("Mail has been sent successfully");
    } catch (MessagingException mex) {
      System.out.println("Unable to send an email" + mex);
    }

    return true;
  }

  @Override
  public boolean resetPassword(String password, int id) {
    User user = this.getUserById(id);
    user.setPassword(this.passwordEncoder.encode(password));

    this.usersRepository.update(user);

    return true;
  }

  // Makes correction to the profile.
  @Override
  public boolean editProfile(EditedUser user) {
    if (!this.idValidator.isValid(user.getId())) {
      throw new InvalidParameterException("User id is not valid");
    } else if (!this.editedUserValidator.isValid(user)) {
      throw new InvalidParameterException("User is not valid");
    }

    User userToEdit = this.usersRepository.getById(user.getId());
    userToEdit.setFirstName(user.getFirstName());
    userToEdit.setLastName(user.getLastName());
    userToEdit.setDescription(user.getDescription());
    userToEdit.setEmail(user.getEmail());
    userToEdit.setPassword(this.passwordEncoder.encode(user.getPassword()));

    this.usersRepository.update(userToEdit);

    return true;
  }

  @Override
  public void sendActivationEmail(String email, String activationKey) {
    User user = this.getUserByEmail(email);

    Properties props = new Properties();
    props.put("mail.smtp.host", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.auth", "true");
    Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        "hristian00i.dev@gmail.com",
                        EMAIL_PASS);
              }
            });
    try {
      MimeMessage msg = new MimeMessage(session);
      InternetAddress[] address = InternetAddress.parse(email, true);
      msg.setRecipients(javax.mail.Message.RecipientType.TO, address);
      msg.setSubject("Активиране на акаунт");
      msg.setSentDate(new Date());
      if (IS_LOCAL.equals("false")) {
        msg.setText("За да активираш акаунта"
                + " си кликни на следния линк: "
                + "https://squadknowhow.herokuapp.com/"
                + "activation?activationKey="
                + activationKey);
      } else {
        msg.setText("За да активираш акаунта"
                + " си кликни на следния линк: "
                + "localhost:3000/activation?activationKey="
                + activationKey);
      }
      msg.setHeader("XPriority", "1");
      Transport.send(msg);
      System.out.println("Mail has been sent successfully");
    } catch (MessagingException mex) {
      System.out.println("Unable to send an email" + mex);
    }
  }

  @Override
  public boolean checkActivationKey(String activationKey) {
    User user = this.usersRepository.getAll().stream()
            .filter(u -> u.getActivationKey().equals(activationKey))
            .findFirst()
            .orElse(null);

    if (user != null) {
      if (!user.isActivated()) {
        user.setActivated(true);
        this.usersRepository.update(user);
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  @Override
  public boolean changeOtherInfo(int id, User newInfo) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    User userToChange = this.usersRepository.getById(id);

    if (newInfo.getCity().getName() != null
            && !Objects.equals(newInfo.getCity().getName(), "")) {
      userToChange.setCity(this.getCity(newInfo.getCity()));
    } else {
      userToChange.setCity(null);
    }

    switch (Integer.parseInt(newInfo.getDegree())) {
      case 1:
        userToChange.setDegree("В училище");
        break;
      case 2:
        userToChange.setDegree("Професионален бакалавър");
        break;
      case 3:
        userToChange.setDegree("Бакалавър");
        break;
      case 4:
        userToChange.setDegree("Магистър");
        break;
      case 5:
        userToChange.setDegree("Доктор");
        break;
      default:
        userToChange.setDegree("В училище");
    }

    userToChange.setPersonalSite(newInfo.getPersonalSite());
    if (newInfo.getEmployer().getName() != null
            && !Objects.equals(newInfo.getEmployer().getName(), "")) {
      userToChange.setEmployer(this.getCompany(newInfo.getEmployer()));
    } else {
      userToChange.setEmployer(null);
    }

    userToChange.setUnemployed(newInfo.isUnemployed());

    List<Skill> skillsToInsert = new ArrayList<>();
    for (int i = 0; i < newInfo.getSkills().size(); i++) {
      if (newInfo.getSkills().get(i).getName() != null
              && !Objects.equals(newInfo.getSkills().get(i).getName(), "")) {
        Skill skillToInsert = this.getSkill(newInfo.getSkills().get(i));
        skillsToInsert.add(skillToInsert);
      }
    }

    if (skillsToInsert.size() != 0) {
      userToChange.setSkills(skillsToInsert);
    } else {
      userToChange.setSkills(null);
    }

    List<Interest> interestsToInsert = new ArrayList<>();
    for (int i = 0; i < newInfo.getInterests().size(); i++) {
      if (newInfo.getInterests().get(i).getName() != null
              && !Objects.equals(newInfo.getInterests().get(i).getName(), "")) {
        Interest interestToInsert = this.getInterest(newInfo.getInterests().get(i));
        interestsToInsert.add(interestToInsert);
      }
    }

    if (interestsToInsert.size() != 0) {
      userToChange.setInterests(interestsToInsert);
    } else {
      userToChange.setInterests(null);
    }

    List<Language> languagesToInsert = new ArrayList<>();
    for (int i = 0; i < newInfo.getLanguages().size(); i++) {
      System.out.println(newInfo.getLanguages().get(i).getName());
      if (newInfo.getLanguages().get(i).getName() != null
              && !Objects.equals(newInfo.getLanguages().get(i).getName(), "")) {
        Language languageToInsert = this.getLanguage(newInfo.getLanguages().get(i));
        languagesToInsert.add(languageToInsert);
      }
    }

    if (languagesToInsert.size() != 0) {
      userToChange.setLanguages(languagesToInsert);
    } else {
      userToChange.setLanguages(null);
    }

    List<Company> previousEmploymentToInsert = new ArrayList<>();
    for (int i = 0; i < newInfo.getInterests().size(); i++) {
      if (newInfo.getPreviousEmployment().get(i).getName() != null
              && !Objects.equals(newInfo.getPreviousEmployment().get(i).getName(), "")) {
        Company companyToInsert = this.getCompany(newInfo.getPreviousEmployment().get(i));
        previousEmploymentToInsert.add(companyToInsert);
      }
    }

    if (previousEmploymentToInsert.size() != 0) {
      userToChange.setPreviousEmployment(previousEmploymentToInsert);
    } else {
      userToChange.setPreviousEmployment(null);
    }

    this.usersRepository.update(userToChange);
    return true;
  }

  @Override
  public boolean checkIfPassIsCorrect(int id, String password) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    return this.passwordEncoder.matches(password,
            this.usersRepository.getById(id).getPassword());
  }

  @Override
  public boolean changePassword(int id, String newPass) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    User user = this.usersRepository.getById(id);
    user.setPassword(this.passwordEncoder.encode(newPass));
    this.usersRepository.update(user);

    return true;
  }

  private User getUserByEmail(String email) {
    return usersRepository.getAll()
            .stream()
            .filter(u -> u.getEmail().equals(email))
            .findFirst()
            .orElse(null);
  }

  private User getUserById(int id) {
    if (!this.idValidator.isValid(id)) {
      throw new InvalidParameterException("Id is not valid");
    }

    return this.usersRepository.getById(id);
  }
}
