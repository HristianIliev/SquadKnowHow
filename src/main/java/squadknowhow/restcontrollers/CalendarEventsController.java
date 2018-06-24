package squadknowhow.restcontrollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import squadknowhow.contracts.ICalendarEventsService;
import squadknowhow.dbmodels.CalendarEvent;

import squadknowhow.response.models.ResponseSuccessful;


/**
 * Rest Controller for the calendar events in
 * project-page-admin and project-page-member html pages.
 */
@RestController
@RequestMapping("/api")
public class CalendarEventsController {
  /**
   * The service that handles the business logic.
   */
  private final ICalendarEventsService calendarEventsService;

  /**
   * Constructor for the Controller.
   *
   * @param calendarEventsServiceDependency The service
   *                                        that handles the logic.
   */
  @Autowired
  public CalendarEventsController(
          final ICalendarEventsService calendarEventsServiceDependency) {
    this.calendarEventsService = calendarEventsServiceDependency;
  }

  /**
   * Returns all calendar events for the parsed projectId.
   *
   * @param projectId The projectId by
   *                  which the events will be filtered.
   * @return List of all the calendar events.
   */
  @RequestMapping(value = "/getEvents", method = RequestMethod.GET)
  public List<CalendarEvent> getEvents(
          final @RequestParam("projectId") int projectId) {
    return this.calendarEventsService.getEvents(projectId);
  }

  /**
   * Method that handles the creation of new events.
   *
   * @param projectId     The projectId to insert in the calendar event.
   * @param calendarEvent The calendar event to be inserted.
   * @return The created CalendarEvent.
   */
  @RequestMapping(value = "/createEvent", method = RequestMethod.POST)
  public CalendarEvent createEvent(
          final @RequestParam("projectId") int projectId,
          final @RequestBody CalendarEvent calendarEvent) {
    return this.calendarEventsService.createEvent(projectId, calendarEvent);
  }

  /**
   * Method that handles the update of a certain event.
   *
   * @param calendarEvent The calendar event to get updated.
   * @return If the update was successful.
   */
  @RequestMapping(value = "/updateEvent", method = RequestMethod.POST)
  public ResponseSuccessful updateEvent(
          final @RequestBody CalendarEvent calendarEvent) {
    return this.calendarEventsService.updateEvent(calendarEvent);
  }

  /**
   * Method that handles the delete of a certain event.
   *
   * @param eventId The id of the event to be deleted.
   * @return If the delete was successful.
   */
  @RequestMapping(value = "/deleteEvent", method = RequestMethod.DELETE)
  public ResponseSuccessful deleteEvent(
          final @RequestParam("eventId") int eventId) {
    return this.calendarEventsService.deleteEvent(eventId);
  }
}
