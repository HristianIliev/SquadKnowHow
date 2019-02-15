package squadknowhow.contracts;

import squadknowhow.request.models.ListEntry;

public interface ITodoListEntriesService {
  boolean deleteItem(String itemTitle);

  boolean createItem(ListEntry todoListEntry);

  boolean markListItemAsDone(String itemTitle);

  boolean markListItemAsUndone(String itemTitle);
}
