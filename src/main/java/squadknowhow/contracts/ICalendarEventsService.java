package squadknowhow.contracts;

import java.util.List;

import squadknowhow.dbmodels.CalendarEvent;
import squadknowhow.response.models.ResponseSuccessful;

public interface ICalendarEventsService {
  List<CalendarEvent> getEvents(int projectId);

  CalendarEvent createEvent(int projectId, CalendarEvent calendarEvent);

  ResponseSuccessful updateEvent(CalendarEvent calendarEvent);

  ResponseSuccessful deleteEvent(int eventId);
}
