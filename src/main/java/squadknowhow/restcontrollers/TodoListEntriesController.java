package squadknowhow.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import squadknowhow.contracts.ITodoListEntriesService;
import squadknowhow.request.models.ListEntry;

@RestController
@RequestMapping("/api")
public class TodoListEntriesController {
  private final ITodoListEntriesService todoListEntriesService;

  @Autowired
  public TodoListEntriesController(ITodoListEntriesService todoListEntriesService) {
    this.todoListEntriesService = todoListEntriesService;
  }

  @RequestMapping(value = "/deleteItem", method = RequestMethod.DELETE)
  public boolean deleteItem(@RequestParam("itemTitle") String itemTitle) {
    return this.todoListEntriesService.deleteItem(itemTitle);
  }

  @RequestMapping(value = "/createItem", method = RequestMethod.POST)
  public boolean createItem(@RequestBody ListEntry todoListEntry) {
    return this.todoListEntriesService.createItem(todoListEntry);
  }

  @RequestMapping(value = "/markListItemAsDone", method = RequestMethod.GET)
  public boolean markListItemAsDone(@RequestParam("itemTitle") String itemTitle) {
    return this.todoListEntriesService.markListItemAsDone(itemTitle);
  }

  @RequestMapping(value = "/markListItemAsUndone", method = RequestMethod.GET)
  public boolean markListItemAsUndone(@RequestParam("itemTitle") String itemTitle) {
    return this.todoListEntriesService.markListItemAsUndone(itemTitle);
  }
}
