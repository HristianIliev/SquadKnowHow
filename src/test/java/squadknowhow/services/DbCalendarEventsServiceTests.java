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
import squadknowhow.dbmodels.CalendarEvent;
import squadknowhow.response.models.ResponseSuccessful;
import squadknowhow.utils.validators.CalendarEventValidator;
import squadknowhow.utils.validators.IdValidator;
import squadknowhow.utils.validators.base.IValidator;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

public class DbCalendarEventsServiceTests {
  @Mock
  private IRepository<CalendarEvent> calendarEventsRepository;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  private IValidator<Integer> idValidator;
  private IValidator<CalendarEvent> calendarEventValidator;
  private DbCalendarEventsService sut;

  @Before
  public void setUpDependencies() {
    this.idValidator = new IdValidator();
    this.calendarEventValidator = new CalendarEventValidator();
    sut = new DbCalendarEventsService(this.calendarEventsRepository,
            this.idValidator,
            this.calendarEventValidator);
  }

  @Test
  public void getEvents_whenProjectIdIsLesThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int projectId = 0;

    sut.getEvents(projectId);
  }

  @Test
  public void getEvents_whenParametersAreValid_shouldReturnTheEventsThatMatchThatProjectId() {
    int projectId = 1;
    List<CalendarEvent> expected = new ArrayList<>();
    CalendarEvent expectedEvent = new CalendarEvent();
    expectedEvent.setId(1);
    expectedEvent.setTitle("title");
    expectedEvent.setStart("whatever");
    expectedEvent.setEnd("whatevet");
    expectedEvent.setAllDay(true);
    expectedEvent.setProjectId(1);
    expected.add(expectedEvent);
    List<CalendarEvent> toReturn = new ArrayList<>();
    CalendarEvent toAdd = new CalendarEvent();
    toAdd.setId(1);
    toAdd.setTitle("title");
    toAdd.setStart("whatever");
    toAdd.setEnd("whatevet");
    toAdd.setAllDay(true);
    toAdd.setProjectId(projectId);
    toReturn.add(toAdd);
    CalendarEvent toAdd2 = new CalendarEvent();
    toAdd2.setId(1);
    toAdd2.setTitle("title");
    toAdd2.setStart("whatever");
    toAdd2.setEnd("whatevet");
    toAdd2.setAllDay(true);
    toAdd2.setProjectId(2);
    toReturn.add(toAdd2);
    when(this.calendarEventsRepository.getAll()).thenReturn(toReturn);

    List<CalendarEvent> actual = sut.getEvents(projectId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void createEvent_whenProjectIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int projectId = 0;
    CalendarEvent calendarEvent = new CalendarEvent();
    calendarEvent.setTitle("valid");
    calendarEvent.setStart("whatever");

    sut.createEvent(projectId, calendarEvent);
  }

  @Test
  public void createEvent_whenCalendarEventIsNull_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("CalendarEvent is not valid");
    int projectId = 1;
    CalendarEvent calendarEvent = null;

    sut.createEvent(projectId, calendarEvent);
  }

  @Test
  public void createEvent_whenCalendarEventTitleIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("CalendarEvent is not valid");
    int projectId = 1;
    CalendarEvent calendarEvent = new CalendarEvent();
    calendarEvent.setTitle("");
    calendarEvent.setStart("whatever");

    sut.createEvent(projectId, calendarEvent);
  }

  @Test
  public void createEvent_whenCalendarEventStartIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("CalendarEvent is not valid");
    int projectId = 1;
    CalendarEvent calendarEvent = new CalendarEvent();
    calendarEvent.setTitle("valid");
    calendarEvent.setStart("");

    sut.createEvent(projectId, calendarEvent);
  }

  @Test
  public void createEvent_whenParametersAreValid_shouldReturnTheCalendarEvent() {
    int projectId = 1;
    CalendarEvent expected = new CalendarEvent();
    expected.setTitle("valid");
    expected.setStart("valid");
    expected.setProjectId(projectId);
    when(this.calendarEventsRepository.create(isA(CalendarEvent.class))).thenReturn(expected);

    CalendarEvent actual = sut.createEvent(projectId, expected);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void updateEvent_whenCalendarEventIsNull_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("CalendarEvent is not valid");
    CalendarEvent calendarEvent = null;

    sut.updateEvent(calendarEvent);
  }

  @Test
  public void updateEvent_whenCalendarEventTitleIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("CalendarEvent is not valid");
    CalendarEvent calendarEvent = new CalendarEvent();
    calendarEvent.setTitle("");
    calendarEvent.setStart("whatever");

    sut.updateEvent(calendarEvent);
  }

  @Test
  public void updateEvent_whenCalendarEventStartIsEmpty_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("CalendarEvent is not valid");
    CalendarEvent calendarEvent = new CalendarEvent();
    calendarEvent.setTitle("valid");
    calendarEvent.setStart("");

    sut.updateEvent(calendarEvent);
  }

  @Test
  public void updateEvent_whenParametersAreValid_shouldReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    CalendarEvent calendarEvent = new CalendarEvent();
    calendarEvent.setId(1);
    calendarEvent.setTitle("valid");
    calendarEvent.setStart("whatever");
    calendarEvent.setAllDay(true);
    calendarEvent.setEnd("whatever");
    when(this.calendarEventsRepository.getById(calendarEvent.getId())).thenReturn(new CalendarEvent());
    when(this.calendarEventsRepository.update(isA(CalendarEvent.class))).thenReturn(null);

    ResponseSuccessful actual = sut.updateEvent(calendarEvent);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void deleteEvent_whenEventIdIsLessThan1_shouldThrowInvalidParameterException() {
    expectedEx.expect(InvalidParameterException.class);
    expectedEx.expectMessage("ProjectId is not valid");
    int eventId = 0;

    sut.deleteEvent(eventId);
  }

  @Test
  public void deleteEvent_whenParametersAreValid_shouldReturnResponseSuccessfullWithTrue() {
    ResponseSuccessful expected = new ResponseSuccessful(true);
    int eventId = 1;
    when(this.calendarEventsRepository.getById(eventId)).thenReturn(new CalendarEvent());

    ResponseSuccessful actual = sut.deleteEvent(eventId);

    Assert.assertEquals(expected, actual);
  }
}
