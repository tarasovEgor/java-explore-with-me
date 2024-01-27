package ru.practicum.ewm.event.service.impl;

import com.querydsl.core.types.Predicate;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import ru.practicum.client.HttpClient;
import ru.practicum.dto.RequestDataDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.mapper.EventRequestMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.QEvent;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.LocationRepository;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exception.BadRequestMethodException;
import ru.practicum.ewm.exception.category.CategoryDoesNotExistException;
import ru.practicum.ewm.exception.event.*;
import ru.practicum.ewm.exception.request.ParticipationRequestDoesNotExistException;
import ru.practicum.ewm.exception.request.ParticipationRequestLimitIsFullException;
import ru.practicum.ewm.exception.user.UserDoesNotExistException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.request.repository.ParticipationRequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.utils.QPredicates;
import ru.practicum.ewm.validation.EventValidation;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.*;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository requestRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private HttpClient httpClient;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository,
                            LocationRepository locationRepository,
                            UserRepository userRepository,
                            CategoryRepository categoryRepository,
                            ParticipationRequestRepository requestRepository) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
    }

    // ---------------------   PRIVATE   --------------------- //

    @Override
    public Event saveEventPrivate(EventDto eventDto, long userId) {
        Event event = EventMapper.toEvent(eventDto);

        Optional<User> initiator = userRepository.findById(userId);
        if (initiator.isPresent() && initiator.get().getClass().equals(User.class)) {

            Optional<Category> category = categoryRepository.findById(eventDto.getCategory());
            if (category.isPresent() && category.get().getClass().equals(Category.class)) {

                if (event.getPaid() == null) {
                    event.setPaid(false);
                }
                if (event.getRequestModeration() == null) {
                    event.setRequestModeration(true);
                }
                if (event.getParticipantLimit() == null) {
                    event.setParticipantLimit(0);
                }

                if (EventValidation.isEventDateValidForSave(event.getEventDate())) {

                    String createdOn = now().format(formatter);

                    event.setInitiator(initiator.get());
                    event.setCategory(category.get());
                    event.setCreatedOn(createdOn);
                    event.setState(State.PENDING);

                    locationRepository.save(eventDto.getLocation());

                    return eventRepository.save(event);
//                    return ResponseEntity
//                            .status(HttpStatus.CREATED)
//                            .body(eventRepository.save(event));

                } else {

                    throw new InvalidEventDateException("Invalid event date.");
//                    return ResponseEntity
//                            .status(HttpStatus.BAD_REQUEST)
//                            .body(new ApiError(
//                                    "400",
//                                    "Bad Request.",
//                                    "Invalid event date."
//                            ));
                }

            } else {

                throw new CategoryDoesNotExistException("Category doesn't exist.");
//                return ResponseEntity
//                        .status(HttpStatus.NOT_FOUND)
//                        .body(new ApiError(
//                                "404",
//                                "Not Found.",
//                                "Category doesn't exist."
//                        ));

            }

        } else {

            throw new UserDoesNotExistException("User doesn't exist.");
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(new ApiError(
//                            "404",
//                            "Not Found.",
//                            "User doesn't exist."
//                    ));

        }

    }

    @Override
    public List<EventShortDto> getAllEventsByUserPrivate(long userId, int from, int size) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent() && user.get().getClass().equals(User.class)) {

            return EventMapper.toEventShortDto(
                    eventRepository.findAllByInitiator(
                            user.get(),
                            PageRequest.of(from, size)
                    )
            );

//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(EventMapper.toEventShortDto(
//                                    eventRepository.findAllByInitiator(
//                                            user.get(),
//                                            PageRequest.of(from, size)
//                                    )
//                            )
//                    );

        } else {

            throw new UserDoesNotExistException("User doesn't exist.");
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(new ApiError(
//                            "400",
//                            "Not Found.",
//                            "User doesn't exist."
//                    ));
        }
    }

    @Override
    public Event getEventByEventIdAndUserIdPrivate(long userId, long eventId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Event> event;

        if (user.isPresent() && user.get().getClass().equals(User.class)) {

            event = eventRepository.findByIdAndInitiator(eventId, user.get());
            if (event.isPresent() && event.get().getClass().equals(Event.class)) {

                return event.get();
//                return ResponseEntity
//                        .status(HttpStatus.OK)
//                        .body(event.get());

            } else {

                throw new EventDoesNotExistException("Event doesn't exist.");
//                return ResponseEntity
//                        .status(HttpStatus.NOT_FOUND)
//                        .body(new ApiError(
//                                "404",
//                                "Not Found.",
//                                "Event doesn't exist."
//                        ));
            }

        } else {

            throw new UserDoesNotExistException("User doesn't exist.");
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(new ApiError(
//                            "404",
//                            "Not Found.",
//                            "User doesn't exist."
//                    ));
        }
    }

    @Override
    public Event patchEventByEventIdAndUserIdPrivate(UpdateEventUserDto updateEventUserDto,
                                                                      long userId, long eventId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent() && user.get().getClass().equals(User.class)) {

            Optional<Event> event = eventRepository.findById(eventId);
            if (event.isPresent() && event.get().getClass().equals(Event.class)) {

                if (event.get().getState().equals(State.PENDING)
                        || event.get().getState().equals(State.CANCELED)) {

                    event.get().setState(State.PENDING);

                    if (updateEventUserDto.getAnnotation() != null) {
                        event.get().setAnnotation(updateEventUserDto.getAnnotation());
                    }

                    if (updateEventUserDto.getCategory() != null) {

                        Optional<Category> category = categoryRepository.findById(updateEventUserDto.getCategory());

                        if (category.isPresent() && category.get().getClass().equals(Category.class)) {

                            event.get().setCategory(category.get());

                        } else {

                            throw new CategoryDoesNotExistException("Category doesn't exist.");
//                            return ResponseEntity
//                                    .status(HttpStatus.NOT_FOUND)
//                                    .body(new ApiError(
//                                            "404",
//                                            "Not Found.",
//                                            "Category doesn't exist."
//                                    ));

                        }

                    }

                    if (updateEventUserDto.getDescription() != null) {
                        event.get().setDescription(updateEventUserDto.getDescription());
                    }

                    if (updateEventUserDto.getEventDate() != null) {

                        if (EventValidation.isEventDateValidForUpdate(
                                event.get().getEventDate(),
                                updateEventUserDto.getEventDate())) {

                            event.get().setEventDate(updateEventUserDto.getEventDate());

                        } else {

                            throw new InvalidEventDateException("Invalid event date.");
//                            return ResponseEntity
//                                    .status(HttpStatus.BAD_REQUEST)
//                                    .body(new ApiError(
//                                            "400",
//                                            "Bad Request.",
//                                            "Invalid event date."
//                                    ));
                        }

                    }

                    if (updateEventUserDto.getLocation() != null) {
                        event.get().setLocation(updateEventUserDto.getLocation());
                    }

                    if (updateEventUserDto.getPaid() != null) {
                        event.get().setPaid(updateEventUserDto.getPaid());
                    }

                    if (updateEventUserDto.getParticipantLimit() != null) {
                        event.get().setParticipantLimit(updateEventUserDto.getParticipantLimit());
                    }

                    if (updateEventUserDto.getRequestModeration() != null) {
                        event.get().setRequestModeration(updateEventUserDto.getRequestModeration());
                    }

                    if (updateEventUserDto.getStateAction() != null) {

                        if (updateEventUserDto.getStateAction()
                                .equals(String.valueOf(UserStateActions.CANCEL_REVIEW))) {

                            event.get().setState(State.CANCELED);

                        } else if (!updateEventUserDto.getStateAction()
                                .equals(String.valueOf(UserStateActions.SEND_TO_REVIEW))) {

                            throw new InvalidStateActionException("Invalid state action.");
//                            return ResponseEntity
//                                        .status(HttpStatus.BAD_REQUEST)
//                                        .body(new ApiError(
//                                                "400",
//                                                "Bad Request.",
//                                                "Invalid state action."
//                                        ));

                        }
                    }

                    if (updateEventUserDto.getTitle() != null) {
                        event.get().setTitle(updateEventUserDto.getTitle());
                    }

                    return eventRepository.save(event.get());
//                    return ResponseEntity
//                            .status(HttpStatus.OK)
//                            .body(eventRepository.save(event.get()));

                } else {

                    throw new InvalidEventStateException("Event is not in the right state.");

//                    return ResponseEntity
//                            .status(HttpStatus.CONFLICT)
//                            .body(new ApiError(
//                                    "409",
//                                    "Conflict.",
//                                    "Event is not in the right state."
//                            ));
                }

            } else {

                throw new EventDoesNotExistException("Event doesn't exist.");
//                return ResponseEntity
//                        .status(HttpStatus.NOT_FOUND)
//                        .body(new ApiError(
//                                "404",
//                                "Not Found.",
//                                "Event doesn't exist."
//                        ));

            }

        } else {

            throw new UserDoesNotExistException("User doesn't exist.");
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(new ApiError(
//                            "404",
//                            "Not Found.",
//                            "User doesn't exist."
//                    ));
        }
    }

    @Override
    public List<ParticipationRequestDto> getUserEventRequestByUserIdAndEventIdPrivate(long userId, long eventId) {
        Optional<User> requester = userRepository.findById(userId);
        if (requester.isPresent() && requester.get().getClass().equals(User.class)) {

            Optional<Event> event = eventRepository.findById(eventId);
            if (event.isPresent() && event.get().getClass().equals(Event.class)) {

                Optional<List<ParticipationRequest>> participationRequests =
                        requestRepository.findAllByEventId(event.get().getId());

                if (!participationRequests.get().isEmpty()) {

                    return ParticipationRequestMapper
                            .toParticipationRequestDto(participationRequests.get());
//                    return ResponseEntity
//                            .status(HttpStatus.OK)
//                            .body(ParticipationRequestMapper
//                                    .toParticipationRequestDto(participationRequests.get())
//                            );

                } else {

                    throw new ParticipationRequestDoesNotExistException("Participation requests don't exist.");
//                    return ResponseEntity
//                            .status(HttpStatus.NOT_FOUND)
//                            .body(new ApiError(
//                                    "404",
//                                    "Not Found.",
//                                    "Participant requests don't exist."
//                            ));

                }

            } else {

                throw new EventDoesNotExistException("Event doesn't exist.");
//                return ResponseEntity
//                        .status(HttpStatus.NOT_FOUND)
//                        .body(new ApiError(
//                                "404",
//                                "Not Found.",
//                                "Event doesn't exist."
//                        ));
            }

        } else {

            throw new UserDoesNotExistException("User doesn't exist.");
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(new ApiError(
//                            "404",
//                            "Not Found.",
//                            "User doesn't exist."
//                    ));
        }
    }

    @Override
    public  EventRequestStatusUpdateResult patchUserEventRequestStatusByUserIdAndEventIdPrivate(
            EventRequestStatusUpdateRequest updateRequest, long userId, long eventId
    ) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent() && user.get().getClass().equals(User.class)) {
            Optional<Event> event = eventRepository.findById(eventId);

            if (event.isPresent() && event.get().getClass().equals(Event.class)) {

                if (event.get().getParticipantLimit().equals(event.get().getConfirmedRequests())
                        && event.get().getParticipantLimit() != 0) {

                    throw new ParticipationRequestLimitIsFullException("Participation request limit is full.");
//                    return ResponseEntity
//                            .status(HttpStatus.CONFLICT)
//                            .body(new ApiError(
//                                    "409",
//                                    "Conflict.",
//                                    "Participant limit is full."
//                            ));

                }

                Optional<List<ParticipationRequest>> requests =
                        requestRepository.findAllByIdIn(updateRequest.getRequestIds());

                if (requests.isPresent()
                        && requests.get().size() == updateRequest.getRequestIds().length) {

                    if (updateRequest.getStatus().equals(Status.CONFIRMED)) {

                        for (int i = 0; i < requests.get().size(); i++) {

                            if (requests.get().get(i).getStatus().equals(Status.PENDING)
                                    && event.get().getParticipantLimit() > event.get().getConfirmedRequests()) {

                                requests.get().get(i).setStatus(Status.CONFIRMED);
                                requestRepository.save(requests.get().get(i));

                            }
                        }

                        event.get().setConfirmedRequests(event.get().getConfirmedRequests() + requests.get().size());
                        eventRepository.save(event.get());

                        return EventRequestMapper
                                .toEventRequestStatusUpdateResult(requests.get());
//                        return ResponseEntity
//                                .status(HttpStatus.OK)
//                                .body(EventRequestMapper
//                                        .toEventRequestStatusUpdateResult(requests.get()));

                    } else if (updateRequest.getStatus().equals(Status.REJECTED)) {

                        for (int i = 0; i < requests.get().size(); i++) {
                            if (requests.get().get(i).getStatus().equals(Status.PENDING)
                                    && event.get().getParticipantLimit() > event.get().getConfirmedRequests()) {
                                requests.get().get(i).setStatus(Status.REJECTED);
                                requestRepository.save(requests.get().get(i));
                            }
                        }

                        return EventRequestMapper
                                .toEventRequestStatusUpdateResult(requests.get());

//                        return ResponseEntity
//                                .status(HttpStatus.OK)
//                                .body(EventRequestMapper
//                                        .toEventRequestStatusUpdateResult(requests.get()));

                    } else {

                        throw new BadRequestMethodException("Method not allowed.");
//                        return ResponseEntity
//                                .status(HttpStatus.BAD_REQUEST)
//                                .body(new ApiError(
//                                        "400",
//                                        "Bad Request.",
//                                        "Method not allowed."
//                                ));

                    }

                } else {

                    throw new ParticipationRequestDoesNotExistException("Participation requests don't exist.");
//                    return ResponseEntity
//                            .status(HttpStatus.NOT_FOUND)
//                            .body(new ApiError(
//                                    "404",
//                                    "Not Found.",
//                                    "Requests do not exist."
//                            ));

                }

            } else {

                throw new EventDoesNotExistException("Event doesn't exist.");
//                return ResponseEntity
//                        .status(HttpStatus.NOT_FOUND)
//                        .body(new ApiError(
//                                "404",
//                                "Not Found.",
//                                "Event doesn't exist."
//                        ));

            }

        } else {

            throw new UserDoesNotExistException("User doesn't exist.");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiError(
//                            "404",
//                            "Not Found.",
//                            "User doesn't exist."
//                    ));
        }
    }

    // ---------------------   PUBLIC   --------------------- //

    @Override
    public List<Event> getAllEventsPublic(HttpServletRequest request, String text, long[] categories,
                                                     Boolean paid, String rangeStart, String rangeEnd,
                                                     Boolean onlyAvailable, String sort, int from, int size) {
        Predicate predicate = QPredicates.builder()
                .add(text, QEvent.event.annotation::containsIgnoreCase)
                .add(text, QEvent.event.description::containsIgnoreCase)
                .add(paid, QEvent.event.paid::eq)
                .add(rangeStart, QEvent.event.eventDate::goe)
                .add(rangeEnd, QEvent.event.eventDate::loe)
                .buildAnd();

        RequestDataDto requestDataDto = new RequestDataDto(
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                String.valueOf(LocalDateTime.now())
        );

        Page<Event> result;

        List<Long> categoryIds;
        List<Event> eventsByCat;
        List<EventWithLDT> eventsByCatLTD;

        if (predicate == null) {

            Page<Event> events = eventRepository.findAll(PageRequest.of(from, size));

            httpClient.saveRequestData(requestDataDto);

            return events.getContent();
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(events.getContent());
        }

        if (categories.length != 0) {

            categoryIds = Arrays
                    .stream(categories)
                    .boxed()
                    .collect(Collectors.toList());

            if (onlyAvailable != null && onlyAvailable) {

                if (sort != null && sort.equals(String.valueOf(SortTypes.EVENT_DATE))) {

                    result = eventRepository.findAll(
                            predicate,
                            PageRequest.of(from, size)
                    );

                    List<EventWithLDT> foundEvents = EventMapper.toEventWithLDT(result.getContent());

                    eventsByCatLTD = foundEvents.stream()
                            .distinct()
                            .filter(x -> x.getConfirmedRequests() < x.getParticipantLimit())
                            .filter(x -> categoryIds.contains(
                                            x.getCategory().getId()
                                    )
                            )
                            .sorted(Comparator.comparing(EventWithLDT::getEventDate).reversed())
                            .collect(Collectors.toList());

                    httpClient.saveRequestData(requestDataDto);

                    return EventMapper.toEventList(eventsByCatLTD);
//                    return ResponseEntity
//                            .status(HttpStatus.OK)
//                            .body(eventsByCatLTD);

                } else if (sort != null && sort.equals(String.valueOf(SortTypes.VIEWS))) {

                    result = eventRepository.findAll(
                            predicate,
                            PageRequest.of(from, size)
                    );

                    eventsByCat = result.getContent().stream()
                            .distinct()
                            .filter(x -> x.getConfirmedRequests() < x.getParticipantLimit())
                            .filter(x -> categoryIds.contains(
                                            x.getCategory().getId()
                                    )
                            )
                            .sorted(Comparator.comparing(Event::getViews).reversed())
                            .collect(Collectors.toList());

                    httpClient.saveRequestData(requestDataDto);

//                    return ResponseEntity
//                            .status(HttpStatus.OK)
//                            .body(eventsByCat);
                    return eventsByCat;

                } else {

                    result = eventRepository.findAll(
                            predicate,
                            PageRequest.of(from, size)
                    );

                    eventsByCat = result.getContent().stream()
                            .distinct()
                            .filter(x -> x.getConfirmedRequests() < x.getParticipantLimit())
                            .filter(x -> categoryIds.contains(
                                            x.getCategory().getId()
                                    )
                            )
                            .collect(Collectors.toList());

                    httpClient.saveRequestData(requestDataDto);

                    return eventsByCat;
//                    return ResponseEntity
//                            .status(HttpStatus.OK)
//                            .body(eventsByCat);
                }

            } else {

                if (sort != null && sort.equals(String.valueOf(SortTypes.EVENT_DATE))) {

                    LocalDateTime start = parse(rangeStart, formatter);
                    LocalDateTime end = parse(rangeEnd, formatter);

                    if (start.isAfter(end)) {

                        throw new InvalidEventDateException("Start date can't come before the end date.");

//                        return ResponseEntity
//                                .status(HttpStatus.BAD_REQUEST)
//                                .body(new ApiError(
//                                        "400",
//                                        "Bad Request.",
//                                        "Start date can't come before the end date."
//                                ));
                    }

                    result = eventRepository.findByEventDateAfter(rangeStart, PageRequest.of(from, size));

                    List<Event> foundEvents = result.getContent();

                    eventsByCat = foundEvents.stream()
                            .distinct()
                            .filter(x -> categoryIds.contains(
                                            x.getCategory().getId()
                                    )
                            )
                            .collect(Collectors.toList());

                    httpClient.saveRequestData(requestDataDto);

                    return eventsByCat;
//                    return ResponseEntity
//                            .status(HttpStatus.OK)
//                            .body(eventsByCat);

                } else if (sort != null && sort.equals(String.valueOf(SortTypes.VIEWS))) {

                    result = eventRepository.findAll(
                            predicate,
                            PageRequest.of(from, size)
                    );

                    eventsByCat = result.getContent().stream()
                            .distinct()
                            .filter(x -> categoryIds.contains(
                                            x.getCategory().getId()
                                    )
                            )
                            .sorted(Comparator.comparing(Event::getViews).reversed())
                            .collect(Collectors.toList());

                    httpClient.saveRequestData(requestDataDto);

                    return eventsByCat;
//                    return ResponseEntity
//                            .status(HttpStatus.OK)
//                            .body(eventsByCat);
                } else {

                    result = eventRepository.findAll(
                            predicate,
                            PageRequest.of(from, size)
                    );

                    eventsByCat = result.getContent().stream()
                            .distinct()
                            .filter(x -> categoryIds.contains(
                                            x.getCategory().getId()
                                    )
                            )
                            .collect(Collectors.toList());

                    httpClient.saveRequestData(requestDataDto);

                    return eventsByCat;
//                    return ResponseEntity
//                            .status(HttpStatus.OK)
//                            .body(eventsByCat);
                }
            }

        } else {

            if (rangeEnd == null && rangeStart == null && onlyAvailable != null) {

                if (sort != null && sort.equals(String.valueOf(SortTypes.EVENT_DATE))) {

                    result = eventRepository.findAll(
                            predicate,
                            PageRequest.of(from, size)
                    );

                    List<EventWithLDT> foundEvents = EventMapper.toEventWithLDT(result.getContent());

                    eventsByCatLTD = foundEvents.stream()
                            .distinct()
                            .filter(x -> x.getConfirmedRequests() < x.getParticipantLimit())
                            .filter(x -> x.getEventDate().isAfter(LocalDateTime.now()))
                            .sorted(Comparator.comparing(EventWithLDT::getEventDate).reversed())
                            .collect(Collectors.toList());

                    httpClient.saveRequestData(requestDataDto);

//                    return ResponseEntity
//                            .status(HttpStatus.OK)
//                            .body(eventsByCatLTD);
                    return EventMapper.toEventList(eventsByCatLTD);

                } else if (sort != null && sort.equals(String.valueOf(SortTypes.VIEWS))) {

                    result = eventRepository.findAll(
                            predicate,
                            PageRequest.of(from, size)
                    );

                    List<EventWithLDT> foundEvents = EventMapper.toEventWithLDT(result.getContent());

                    eventsByCatLTD = foundEvents.stream()
                            .distinct()
                            .filter(x -> x.getConfirmedRequests() < x.getParticipantLimit())
                            .filter(x -> x.getEventDate().isAfter(LocalDateTime.now()))
                            .sorted(Comparator.comparing(EventWithLDT::getViews).reversed())
                            .collect(Collectors.toList());

                    httpClient.saveRequestData(requestDataDto);

//                    return ResponseEntity
//                            .status(HttpStatus.OK)
//                            .body(eventsByCatLTD);
                    return EventMapper.toEventList(eventsByCatLTD);

                } else {

                    result = eventRepository.findAll(
                            predicate,
                            PageRequest.of(from, size)
                    );

                    List<EventWithLDT> foundEvents = EventMapper.toEventWithLDT(result.getContent());

                    eventsByCatLTD = foundEvents.stream()
                            .distinct()
                            .filter(x -> x.getConfirmedRequests() < x.getParticipantLimit())
                            .filter(x -> x.getEventDate().isAfter(LocalDateTime.now()))
                            .collect(Collectors.toList());

                    httpClient.saveRequestData(requestDataDto);

//                    return ResponseEntity
//                            .status(HttpStatus.OK)
//                            .body(eventsByCatLTD);
                    return EventMapper.toEventList(eventsByCatLTD);
                }

            } else if (onlyAvailable != null) {

                LocalDateTime start = parse(rangeStart, formatter);
                LocalDateTime end = parse(rangeEnd, formatter);

                if (start.isAfter(end)) {

                    throw new InvalidEventDateException("Start date can't come before the end date.");
//                    return ResponseEntity
//                            .status(HttpStatus.BAD_REQUEST)
//                            .body(new ApiError(
//                                    "400",
//                                    "Bad Request.",
//                                    "Start date can't come before the end date."
//                            ));
                }

                if (sort != null && sort.equals(String.valueOf(SortTypes.EVENT_DATE))) {

                    result = eventRepository.findAll(
                            predicate,
                            PageRequest.of(from, size)
                    );

                    List<EventWithLDT> foundEvents = EventMapper.toEventWithLDT(result.getContent());

                    eventsByCatLTD = foundEvents.stream()
                            .distinct()
                            .sorted(Comparator.comparing(EventWithLDT::getEventDate).reversed())
                            .collect(Collectors.toList());

                    httpClient.saveRequestData(requestDataDto);

                    return EventMapper.toEventList(eventsByCatLTD);
//                    return ResponseEntity
//                            .status(HttpStatus.OK)
//                            .body(eventsByCatLTD);

                } else if (sort != null && sort.equals(String.valueOf(SortTypes.VIEWS))) {

                    result = eventRepository.findAll(
                            predicate,
                            PageRequest.of(from, size)
                    );

                    List<EventWithLDT> foundEvents = EventMapper.toEventWithLDT(result.getContent());

                    eventsByCatLTD = foundEvents.stream()
                            .distinct()
                            .sorted(Comparator.comparing(EventWithLDT::getViews).reversed())
                            .collect(Collectors.toList());

                    httpClient.saveRequestData(requestDataDto);

                    return EventMapper.toEventList(eventsByCatLTD);
//                    return ResponseEntity
//                            .status(HttpStatus.OK)
//                            .body(eventsByCatLTD);

                } else {

                    result = eventRepository.findAll(
                            predicate,
                            PageRequest.of(from, size)
                    );

                    List<EventWithLDT> foundEvents = EventMapper.toEventWithLDT(result.getContent());

                    eventsByCatLTD = foundEvents.stream()
                            .distinct()
                            .collect(Collectors.toList());

                    httpClient.saveRequestData(requestDataDto);

                    return EventMapper.toEventList(eventsByCatLTD);
//                    return ResponseEntity
//                            .status(HttpStatus.OK)
//                            .body(eventsByCatLTD);

                }

            } else {

                httpClient.saveRequestData(requestDataDto);

                return eventRepository
                        .findAll(predicate, PageRequest.of(from, size))
                        .getContent();
//                return ResponseEntity
//                        .status(HttpStatus.OK)
//                        .body(eventRepository.findAll(predicate, PageRequest.of(from, size)));

            }
        }
    }

    @Override
    public Event getEventByIdPublic(long eventId, HttpServletRequest request) {
        Optional<Event> event = eventRepository.findById(eventId);

        RequestDataDto requestDataDto = new RequestDataDto(
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                String.valueOf(LocalDateTime.now())
        );

        if (event.isPresent() && event.get().getClass().equals(Event.class)) {

            if (event.get().getState().equals(State.PUBLISHED)) {

                event.get().setViews(event.get().getViews() + 1);

                httpClient.saveRequestData(requestDataDto);

                return event.get();
//                return ResponseEntity
//                        .status(HttpStatus.OK)
//                        .body(event.get());

            } else {

                throw new EventHasNotBeenPublishedYetException("Event hasn't been published.");
//                return ResponseEntity
//                        .status(HttpStatus.NOT_FOUND)
//                        .body(new ApiError(
//                                "404",
//                                "Not Found.",
//                                "Event hasn't been published."
//                        ));

            }

        } else {

            throw new EventDoesNotExistException("Event doesn't exist.");
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(new ApiError(
//                            "404",
//                            "Not Found.",
//                            "Event doesn't exist."
//                    ));
        }
    }

    // ---------------------   ADMIN   --------------------- //

    @Override
    public List<Event> getAllEventsAdmin(HttpServletRequest request, long[] users,
                                                    String[] statesStr, long[] categories,
                                                    String rangeStart, String rangeEnd,
                                                    int from, int size) {
        Page<Event> result;
        List<Event> events;

        Predicate predicate = QPredicates.builder()
                .add(rangeStart, QEvent.event.eventDate::gt)
                .add(rangeEnd, QEvent.event.eventDate::lt)
                .buildAnd();

        if (users == null) {
            users = new long[]{};
        }
        if (categories == null) {
            categories = new long[]{};
        }
        if (statesStr == null) {
            statesStr = new String[]{};
        }

        if (predicate == null) {

            Page<Event> foundEvents = eventRepository.findAll(PageRequest.of(from, size));

            return foundEvents.getContent();
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(foundEvents.getContent());
        }

        Optional<List<Long>> userIds = Optional.of(Arrays.stream(users)
                .boxed().collect(Collectors.toList()));

        Optional<List<Long>> categoryIds = Optional.of(Arrays.stream(categories)
                .boxed().collect(Collectors.toList()));

        Optional<List<String>> states = Optional.of(Arrays.stream(statesStr)
                .collect(Collectors.toList()));

        if (!userIds.get().isEmpty() && !categoryIds.get().isEmpty() && !states.get().isEmpty()) {

            result = eventRepository.findAll(
                    predicate,
                    PageRequest.of(from, size)
            );

            events = result.getContent().stream()
                    .distinct()
                    .filter(x -> userIds.get().contains(
                                    x.getInitiator().getId()
                            )
                    )
                    .filter(x -> categoryIds.get().contains(
                                    x.getCategory().getId()
                            )
                    )
                    .filter(x -> states.get().contains(
                                     String.valueOf(x.getState())
                            )
                    )
                    .collect(Collectors.toList());

            return events;
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(events);

        } else if (!userIds.get().isEmpty() && !states.get().isEmpty()) {

            result = eventRepository.findAll(
                    predicate,
                    PageRequest.of(from, size)
            );

            events = result.getContent().stream()
                    .distinct()
                    .filter(x -> userIds.get().contains(
                                    x.getInitiator().getId()
                            )
                    )
                    .filter(x -> states.get().contains(
                                     String.valueOf(x.getState())
                            )
                    )
                    .collect(Collectors.toList());

            return events;
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(events);

        } else if (!userIds.get().isEmpty() && !categoryIds.get().isEmpty()) {

            result = eventRepository.findAll(
                    predicate,
                    PageRequest.of(from, size)
            );

            events = result.getContent().stream()
                    .distinct()
                    .filter(x -> userIds.get().contains(
                                    x.getInitiator().getId()
                            )
                    )
                    .filter(x -> categoryIds.get().contains(
                                    x.getCategory().getId()
                            )
                    )
                    .collect(Collectors.toList());

            return events;
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(events);

        } else if (!states.get().isEmpty() && !categoryIds.get().isEmpty()) {

            result = eventRepository.findAll(
                    predicate,
                    PageRequest.of(from, size)
            );

            events = result.getContent().stream()
                    .distinct()
                    .filter(x -> states.get().contains(
                                    String.valueOf(x.getState())
                            )
                    )
                    .filter(x -> categoryIds.get().contains(
                                    x.getCategory().getId()
                            )
                    )
                    .collect(Collectors.toList());

            return events;
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(events);

        } else if (!userIds.get().isEmpty()) {

            result = eventRepository.findAll(
                    predicate,
                    PageRequest.of(from, size)
            );

            events = result.getContent().stream()
                    .distinct()
                    .filter(x -> userIds.get().contains(
                                    x.getInitiator().getId()
                            )
                    )
                    .collect(Collectors.toList());

            return events;
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(events);

        } else if (!categoryIds.get().isEmpty()) {

            result = eventRepository.findAll(
                    predicate,
                    PageRequest.of(from, size)
            );

            events = result.getContent().stream()
                    .distinct()
                    .filter(x -> categoryIds.get().contains(
                                    x.getCategory().getId()
                            )
                    )
                    .collect(Collectors.toList());

            return events;
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(events);

        } else if (!states.get().isEmpty()) {

            result = eventRepository.findAll(
                    predicate,
                    PageRequest.of(from, size)
            );

            events = result.getContent().stream()
                    .distinct()
                    .filter(x -> states.get().contains(
                                    String.valueOf(x.getState())
                            )
                    )
                    .collect(Collectors.toList());

            return events;
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(events);

        } else {

            result = eventRepository.findAll(
                    predicate,
                    PageRequest.of(from, size)
            );

            return result.getContent();
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(result.getContent());

        }
    }

    @Override
    public Event patchEventDataAdmin(UpdateEventAdminDto updateEventAdminDto, long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);

        if (event.isPresent() && event.get().getClass().equals(Event.class)) {

            if (updateEventAdminDto.getAnnotation() != null) {
                event.get().setAnnotation(updateEventAdminDto.getAnnotation());
            }

            if (updateEventAdminDto.getCategory() != null) {

                Optional<Category> category = categoryRepository.findById(updateEventAdminDto.getCategory());

                if (category.isPresent() && category.get().getClass().equals(Category.class)) {

                    event.get().setCategory(category.get());

                } else {

                    throw new CategoryDoesNotExistException("Category doesn't exist exception.");
//                    return ResponseEntity
//                            .status(HttpStatus.NOT_FOUND)
//                            .body(new ApiError(
//                                    "404",
//                                    "Not Found.",
//                                    "Category doesn't exist."
//                            ));

                }
            }

            if (updateEventAdminDto.getDescription() != null) {
                event.get().setDescription(updateEventAdminDto.getDescription());
            }

            if (updateEventAdminDto.getEventDate() != null) {

                if (EventValidation.isEventDateValidForUpdate(
                        event.get().getEventDate(),
                        updateEventAdminDto.getEventDate())) {

                    event.get().setEventDate(updateEventAdminDto.getEventDate());

                } else {

                    throw new InvalidEventDateException("Invalid event date.");
//                    return ResponseEntity
//                            .status(HttpStatus.BAD_REQUEST)
//                            .body(new ApiError(
//                                    "400",
//                                    "Bad Request.",
//                                    "Invalid event date."
//                            ));
                }

            }

            if (updateEventAdminDto.getLocation() != null) {
                event.get().setLocation(updateEventAdminDto.getLocation());
            }

            if (updateEventAdminDto.getPaid() != null) {
                event.get().setPaid(updateEventAdminDto.getPaid());
            }

            if (updateEventAdminDto.getParticipantLimit() != null) {
                event.get().setParticipantLimit(updateEventAdminDto.getParticipantLimit());
            }

            if (updateEventAdminDto.getRequestModeration() != null) {
                event.get().setRequestModeration(updateEventAdminDto.getRequestModeration());
            }

            if (updateEventAdminDto.getStateAction() != null) {

                if (event.get().getState().equals(State.PENDING)) {

                    if (updateEventAdminDto.getStateAction()
                            .equals(String.valueOf(AdminStateActions.PUBLISH_EVENT))) {

                        event.get().setState(State.PUBLISHED);

                    } else if (updateEventAdminDto.getStateAction()
                            .equals(String.valueOf(AdminStateActions.REJECT_EVENT))) {

                        event.get().setState(State.CANCELED);

                    } else {

                        throw new InvalidStateActionException("Invalid state action.");
//                        return ResponseEntity
//                                .status(HttpStatus.BAD_REQUEST)
//                                .body(new ApiError(
//                                        "400",
//                                        "Bad Request.",
//                                        "Invalid state action."
//                                ));
                    }

                } else {

                    throw new InvalidEventStateException("Event is not in the right state.");
//                    return ResponseEntity
//                            .status(HttpStatus.CONFLICT)
//                            .body(new ApiError(
//                               "409",
//                               "Conflict.",
//                               "Event is not in the right state."
//                            ));

                }

            }

            if (updateEventAdminDto.getTitle() != null) {
                event.get().setTitle(updateEventAdminDto.getTitle());
            }

            return eventRepository.save(event.get());
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(eventRepository.save(event.get()));

        } else {

            throw new EventDoesNotExistException("Event doesn't exist.");
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(new ApiError(
//                       "404",
//                       "Not Found.",
//                            "Event doesn't exist."
//                    ));

        }
    }

    enum SortTypes {
        EVENT_DATE,
        VIEWS
    }

    enum AdminStateActions {
        PUBLISH_EVENT,
        REJECT_EVENT
    }

    enum UserStateActions {
        SEND_TO_REVIEW,
        CANCEL_REVIEW
    }

}
