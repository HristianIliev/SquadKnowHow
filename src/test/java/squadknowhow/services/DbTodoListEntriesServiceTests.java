package squadknowhow.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.Project;
import squadknowhow.dbmodels.TodoListEntry;
import squadknowhow.request.models.ListEntry;
import squadknowhow.utils.validators.IdValidator;
import squadknowhow.utils.validators.TodoListEntryValidator;
import squadknowhow.utils.validators.base.IValidator;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

public class DbTodoListEntriesServiceTests {
  @Mock
  IRepository<TodoListEntry> todoListEntriesRepositoryMock;
  @Mock
  IRepository<Project> projestRepositoryMock;

  private IValidator<Integer> idValidator;
  private IValidator<ListEntry> todoListEntryValidator;
  private DbTodoListEntriesService sut;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Before
  public void setUpValidators() {
    this.idValidator = new IdValidator();
    this.todoListEntryValidator = new TodoListEntryValidator();
    this.sut = new DbTodoListEntriesService(this.todoListEntriesRepositoryMock,
            this.projestRepositoryMock,
            this.idValidator,
            this.todoListEntryValidator);
  }

  @Test
  public void deleteItem_whenItemTitleIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ItemTitle is not valid");
    String itemTitle = "";

    sut.deleteItem(itemTitle);
  }

  @Test
  public void deleteItem_whenItemIdIsValid_shouldReturnTrue() {
    String itemTitle = "test";
    when(this.todoListEntriesRepositoryMock.getAll()).thenReturn(new ArrayList<>(Collections.singletonList(new TodoListEntry(itemTitle))));
    when(this.todoListEntriesRepositoryMock.delete(isA(TodoListEntry.class))).thenReturn(null);

    boolean actual = sut.deleteItem(itemTitle);

    Assert.assertTrue(actual);
  }

  @Test
  public void createItem_whenListEntryIsNull_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("TodoListEntry is not valid");
    ListEntry listEntry = null;

    sut.createItem(listEntry);
  }

  @Test
  public void createItem_whenListEntryTitleIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("TodoListEntry is not valid");
    ListEntry listEntry = new ListEntry("");

    sut.createItem(listEntry);
  }

  @Test
  public void createItem_whenListEntryProjectIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    ListEntry listEntry = new ListEntry("valid", 0);

    sut.createItem(listEntry);
  }

  @Test
  public void createItem_whenParametersAreValidButThereIsAlreadyAnItemWithTheSameName_shouldReturnTrue() {
    ListEntry listEntry = new ListEntry("valid", 1);
    when(this.todoListEntriesRepositoryMock.getAll()).thenReturn(new ArrayList<>(Collections.singletonList(new TodoListEntry("valid"))));
    when(this.projestRepositoryMock.getById(1)).thenReturn(null);
    when(this.todoListEntriesRepositoryMock.create(isA(TodoListEntry.class))).thenReturn(null);

    boolean actual = sut.createItem(listEntry);

    Assert.assertFalse(actual);
  }

  @Test
  public void createItem_whenParametersAreValid_shouldReturnTrue() {
    ListEntry listEntry = new ListEntry("valid", 1);
    when(this.projestRepositoryMock.getById(1)).thenReturn(null);
    when(this.todoListEntriesRepositoryMock.create(isA(TodoListEntry.class))).thenReturn(null);

    boolean actual = sut.createItem(listEntry);

    Assert.assertTrue(actual);
  }

  @Test
  public void markListItemAsDone_whenItemTitleIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ItemTitle is not valid");
    String itemTitle = "";

    sut.markListItemAsDone(itemTitle);
  }

  @Test
  public void markListItemAsDone_whenNoItemWithThatTitleHasBennFound_shouldReturnFalse() {
    String itemTitle = "valid";
    List<TodoListEntry> listEntries = new ArrayList<>();
    listEntries.add(new TodoListEntry("not valid"));
    when(this.todoListEntriesRepositoryMock.getAll()).thenReturn(listEntries);

    boolean actual = sut.markListItemAsDone(itemTitle);

    Assert.assertFalse(actual);
  }

  @Test
  public void markListItemAsDone_whenItemWithThatTitleHasBeenFound_shouldReturnTrue() {
    String itemTitle = "valid";
    List<TodoListEntry> listEntries = new ArrayList<>();
    listEntries.add(new TodoListEntry("valid"));
    when(this.todoListEntriesRepositoryMock.getAll()).thenReturn(listEntries);

    boolean actual = sut.markListItemAsDone(itemTitle);

    Assert.assertTrue(actual);
  }

  @Test
  public void markListItemAsUndone_whenItemTitleIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ItemTitle is not valid");
    String itemTitle = "";

    sut.markListItemAsUndone(itemTitle);
  }

  @Test
  public void markListItemAsUndone_whenNoItemWithThatTitleHasBennFound_shouldReturnFalse() {
    String itemTitle = "valid";
    List<TodoListEntry> listEntries = new ArrayList<>();
    listEntries.add(new TodoListEntry("not valid"));
    when(this.todoListEntriesRepositoryMock.getAll()).thenReturn(listEntries);

    boolean actual = sut.markListItemAsUndone(itemTitle);

    Assert.assertFalse(actual);
  }

  @Test
  public void markListItemAsUndone_whenItemWithThatTitleHasBeenFound_shouldReturnTrue() {
    String itemTitle = "valid";
    List<TodoListEntry> listEntries = new ArrayList<>();
    listEntries.add(new TodoListEntry("valid"));
    when(this.todoListEntriesRepositoryMock.getAll()).thenReturn(listEntries);

    boolean actual = sut.markListItemAsUndone(itemTitle);

    Assert.assertTrue(actual);
  }
}
