package squadknowhow.restcontrollers;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import squadknowhow.contracts.ITagsService;

@RestController
@RequestMapping("/api")
public class TagsController {
  private ITagsService tagsService;

  @Autowired
  public TagsController(ITagsService tagsService) {
    this.tagsService = tagsService;
  }

  @RequestMapping(value = "/getSkills", method = RequestMethod.GET)
  public HashSet<String> getSkills(@RequestParam("term") String term) {
    return this.tagsService.getSkills(term);
  }

  @RequestMapping(value = "/getInterests", method = RequestMethod.GET)
  public HashSet<String> getInterests(@RequestParam("term") String term) {
    return this.tagsService.getInterests(term);
  }

  @RequestMapping(value = "/getCities", method = RequestMethod.GET)
  public HashSet<String> getCities(@RequestParam("term") String term) {
    return this.tagsService.getCities(term);
  }

  @RequestMapping(value = "/getEmployedBy", method = RequestMethod.GET)
  public HashSet<String> getEmployedBy(@RequestParam("term") String term) {
    return this.tagsService.getEmployedBy(term);
  }
}
