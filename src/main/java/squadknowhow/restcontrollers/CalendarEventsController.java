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

@RestController
@RequestMapping("/api")
public class CalendarEventsController {
  private final ICalendarEventsService calendarEventsService;

  @Autowired
  public CalendarEventsController(ICalendarEventsService calendarEventsServiceDependency) {
    this.calendarEventsService = calendarEventsServiceDependency;
  }

  @RequestMapping(value = "/getEvents", method = RequestMethod.GET)
  public List<CalendarEvent> getEvents(@RequestParam("projectId") int projectId) {
    return this.calendarEventsService.getEvents(projectId);
  }

  @RequestMapping(value = "/createEvent", method = RequestMethod.POST)
  public CalendarEvent createEvent(@RequestParam("projectId") int projectId,
                                   @RequestBody CalendarEvent calendarEvent) {
    return this.calendarEventsService.createEvent(projectId, calendarEvent);
  }

  @RequestMapping(value = "/updateEvent", method = RequestMethod.POST)
  public ResponseSuccessful updateEvent(@RequestBody CalendarEvent calendarEvent) {
    return this.calendarEventsService.updateEvent(calendarEvent);
  }

  @RequestMapping(value = "/deleteEvent", method = RequestMethod.DELETE)
  public ResponseSuccessful deleteEvent(@RequestParam("eventId") int eventId) {
    return this.calendarEventsService.deleteEvent(eventId);
  }
}
