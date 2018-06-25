package squadknowhow.services;

import java.security.InvalidParameterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import squadknowhow.contracts.IRepository;
import squadknowhow.contracts.ITodoListEntriesService;
import squadknowhow.dbmodels.Project;
import squadknowhow.dbmodels.TodoListEntry;
import squadknowhow.request.models.ListEntry;
import squadknowhow.utils.validators.base.IValidator;

@Service
public class DbTodoListEntriesService implements ITodoListEntriesService {
  private final IRepository<Project> projectsRepository;
  private final IValidator<Integer> idValidator;
  private final IValidator<ListEntry> todoListEntryValidator;
  private final IRepository<TodoListEntry> todoListEntriesRepository;

  @Autowired
  public DbTodoListEntriesService(IRepository<TodoListEntry> todoListEntriesRepository,
                                  IRepository<Project> projectsRepository,
                                  IValidator<Integer> idValidator,
                                  IValidator<ListEntry> todoListEntryValidator) {
    this.todoListEntriesRepository = todoListEntriesRepository;
    this.projectsRepository = projectsRepository;
    this.idValidator = idValidator;
    this.todoListEntryValidator = todoListEntryValidator;
  }

  @Override
  public boolean deleteItem(int itemId) {
    if (!this.idValidator.isValid(itemId)) {
      throw new InvalidParameterException("ItemId is not valid");
    }

    this.todoListEntriesRepository.delete(this.todoListEntriesRepository.getById(itemId));
    return true;
  }

  @Override
  public boolean createItem(ListEntry todoListEntry) {
    if (!this.todoListEntryValidator.isValid(todoListEntry)) {
      throw new InvalidParameterException("TodoListEntry is not valid");
    } else if (!this.idValidator.isValid(todoListEntry.getProjectId())) {
      throw new InvalidParameterException("ProjectId is not valid");
    }

    TodoListEntry todoListEntryToInsert = new TodoListEntry();
    todoListEntryToInsert.setTitle(todoListEntry.getTitle());
    todoListEntryToInsert.setDescription(todoListEntry.getDescription());
    todoListEntryToInsert.setDueDate(todoListEntry.getDueDate());
    todoListEntryToInsert.setDone(false);
    todoListEntryToInsert.setProject(this.projectsRepository.getById(todoListEntry.getProjectId()));
    this.todoListEntriesRepository.create(todoListEntryToInsert);
    return true;
  }

  @Override
  public boolean markListItemAsDone(String itemTitle) {
    if (itemTitle.isEmpty()) {
      throw new InvalidParameterException("ItemTitle is not valid");
    }

    TodoListEntry item = this.todoListEntriesRepository.getAll().stream()
            .filter(it -> it.getTitle().equals(itemTitle))
            .findFirst()
            .orElse(null);
    if (item != null) {
      item.setDone(true);
      this.todoListEntriesRepository.update(item);
      return true;
    }

    return false;
  }

  @Override
  public boolean markListItemAsUndone(String itemTitle) {
    if (itemTitle.isEmpty()) {
      throw new InvalidParameterException("ItemTitle is not valid");
    }

    TodoListEntry item = this.todoListEntriesRepository.getAll().stream()
            .filter(it -> it.getTitle().equals(itemTitle))
            .findFirst()
            .orElse(null);
    if (item != null) {
      item.setDone(false);
      this.todoListEntriesRepository.update(item);
      return true;
    }

    return false;
  }
}
