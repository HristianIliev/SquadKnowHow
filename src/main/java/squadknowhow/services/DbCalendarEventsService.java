package squadknowhow.services;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import squadknowhow.contracts.ICalendarEventsService;
import squadknowhow.contracts.IRepository;
import squadknowhow.dbmodels.CalendarEvent;
import squadknowhow.response.models.ResponseSuccessful;

import squadknowhow.utils.validators.base.IValidator;

@Service
public class DbCalendarEventsService implements ICalendarEventsService {
  private final IRepository<CalendarEvent> calendarEventsRepository;
  private final IValidator<Integer> idValidator;
  private final IValidator<CalendarEvent> calendarEventValidator;

  /**
   * Constructor for the Service.
   *
   * @param calendarEventsRepository HibernateRepository for the calendarEvents.
   * @param idValidator              Validator for id parameters.
   * @param calendarEventValidator   Validator for Calendar Event parameters.
   */
  @Autowired
  public DbCalendarEventsService(IRepository<CalendarEvent> calendarEventsRepository,
                                 IValidator<Integer> idValidator,
                                 IValidator<CalendarEvent> calendarEventValidator) {
    this.calendarEventsRepository = calendarEventsRepository;
    this.idValidator = idValidator;
    this.calendarEventValidator = calendarEventValidator;
  }

  @Override
  public List<CalendarEvent> getEvents(int projectId) {
    if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    }

    return this.calendarEventsRepository.getAll()
            .stream()
            .filter(event -> event.getProjectId() == projectId)
            .collect(Collectors.toList());
  }

  @Override
  public CalendarEvent createEvent(int projectId, CalendarEvent calendarEvent) {
    if (!this.idValidator.isValid(projectId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    } else if (!this.calendarEventValidator.isValid(calendarEvent)) {
      throw new InvalidParameterException("CalendarEvent is not valid");
    }

    calendarEvent.setProjectId(projectId);
    return this.calendarEventsRepository.create(calendarEvent);
  }

  @Override
  public ResponseSuccessful updateEvent(CalendarEvent calendarEvent) {
    if (!this.calendarEventValidator.isValid(calendarEvent)) {
      throw new InvalidParameterException("CalendarEvent is not valid");
    }

    CalendarEvent calendarEventToUpdate = this.calendarEventsRepository
            .getById(calendarEvent.getId());
    calendarEventToUpdate.setStart(calendarEvent.getStart());
    calendarEventToUpdate.setAllDay(calendarEvent.isAllDay());
    calendarEventToUpdate.setEnd(calendarEvent.getEnd());
    this.calendarEventsRepository.update(calendarEventToUpdate);

    return new ResponseSuccessful(true);
  }

  @Override
  public ResponseSuccessful deleteEvent(int eventId) {
    if (!this.idValidator.isValid(eventId)) {
      throw new InvalidParameterException("ProjectId is not valid");
    }

    this.calendarEventsRepository.delete(this.calendarEventsRepository.getById(eventId));

    return new ResponseSuccessful(true);
  }
}
