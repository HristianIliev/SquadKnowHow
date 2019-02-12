package squadknowhow.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import squadknowhow.contracts.ITagsService;
import squadknowhow.request.models.Parameter;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TagsController {
  private ITagsService tagsService;

  @Autowired
  public TagsController(ITagsService tagsService) {
    this.tagsService = tagsService;
  }

  @RequestMapping(value = "/getEmails", method = RequestMethod.GET)
  public List<String> getEmails(@RequestParam("term") String term) {
    return this.tagsService.getEmails(term);
  }

  @RequestMapping(value = "/getSkills", method = RequestMethod.GET)
  public HashSet<String> getSkills(@RequestParam("term") String term) {
    return this.tagsService.getSkills(term);
  }

  @RequestMapping(value = "/getInterests", method = RequestMethod.GET)
  public HashSet<String> getInterests(@RequestParam("term") String term) {
    return this.tagsService.getInterests(term);
  }

  @RequestMapping(value = "/getLanguages", method = RequestMethod.GET)
  public HashSet<String> getLanguages(@RequestParam("term") String term) {
    return this.tagsService.getLanguages(term);
  }

  @RequestMapping(value = "/getCities", method = RequestMethod.GET)
  public HashSet<String> getCities(@RequestParam("term") String term) {
    return this.tagsService.getCities(term);
  }

  @RequestMapping(value = "/getEmployedBy", method = RequestMethod.GET)
  public HashSet<String> getEmployedBy(@RequestParam("term") String term) {
    return this.tagsService.getEmployedBy(term);
  }

  @RequestMapping(value = "/checkIfNeedsAreCorrect",
          method = RequestMethod.POST)
  @ResponseBody
  public boolean checkIfNeedsAreCorrect(
          @RequestBody List<Parameter> parameters) {
    return this.tagsService.checkIfTagsAreCorrect(parameters);
  }
}
