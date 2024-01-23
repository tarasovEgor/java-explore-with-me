package ru.practicum.ewm.event.service.impl;

import com.querydsl.core.types.Predicate;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ru.practicum.client.HttpClient;
import ru.practicum.dto.RequestDataDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.error.ApiError;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.mapper.EventRequestMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.QEvent;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.LocationRepository;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.request.repository.ParticipationRequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.utils.QPredicates;
import ru.practicum.ewm.validation.EventValidation;
//import ru.practicum.server.model.RequestData;
//import ru.practicum.server.model.RequestData;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.*;

@Slf4j
@Service
public class EventServiceImpl implements EventService {
//
//    @Autowired
//    private EntityManager em;

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
    public ResponseEntity<Object> saveEventPrivate(EventDto eventDto, long userId) {

        Event event = EventMapper.toEvent(eventDto);

        Optional<User> initiator = userRepository.findById(userId);
        if (initiator.isPresent() && initiator.get().getClass().equals(User.class)) {

            Optional<Category> category = categoryRepository.findById(eventDto.getCategory());
            if (category.isPresent() && category.get().getClass().equals(Category.class)) {

//        LocalDateTime time = LocalDateTime.parse(event.getEventDate(), formatter);

                //LocalDateTime eventDate = LocalDateTime.parse(event.getEventDate(), formatter);
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

                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(eventRepository.save(event));

                } else {

                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(new ApiError(
                                    "400",
                                    "Bad Request.",
                                    "Invalid event date."
                            ));
                }

            } else {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiError(
                                "404",
                                "Not Found.",
                                "Category doesn't exist."
                        ));

            }

        } else {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(
                            "404",
                            "Not Found.",
                            "User doesn't exist."
                    ));

        }


        /*
         *   CHECKS!!!
         * *//*

        //if (eventDto.getAnnotation() == null)

        Event event = EventMapper.toEvent(eventDto);

        // переделать
        Optional<User> initiator = userRepository.findById(userId);
        Optional<Category> category = categoryRepository.findById(eventDto.getCategory());

        if (initiator.isEmpty() || category.isEmpty())
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiError());

        //   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createdOn = now().format(formatter);

        event.setInitiator(initiator.get());
        event.setCategory(category.get());
        event.setCreatedOn(createdOn);
        event.setState(State.PENDING);

        locationRepository.save(eventDto.getLocation());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventRepository.save(event));*/
    }

    @Override
    public ResponseEntity<Object> getAllEventsByUserPrivate(long userId, int from, int size) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent() && user.get().getClass().equals(User.class)) {

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(EventMapper.toEventShortDto(
                                    eventRepository.findAllByInitiator(
                                            user.get(),
                                            PageRequest.of(from, size)
                                    )
                            )
                    );
        } else {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(
                            "400",
                            "Not Found.",
                            "User doesn't exist."
                    ));
        }

    }

    @Override
    public ResponseEntity<Object> getEventByEventIdAndUserIdPrivate(long userId, long eventId) {

        Optional<User> user = userRepository.findById(userId);
        Optional<Event> event;

        if (user.isPresent() && user.get().getClass().equals(User.class)) {

            event = eventRepository.findByIdAndInitiator(eventId, user.get());
            if (event.isPresent() && event.get().getClass().equals(Event.class)) {

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(event.get());

            } else {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiError(
                                "404",
                                "Not Found.",
                                "Event doesn't exist."
                        ));
            }

        } else {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(
                            "404",
                            "Not Found.",
                            "User doesn't exist."
                    ));
        }
    }

    @Override
    public ResponseEntity<Object> patchEventByEventIdAndUserIdPrivate(UpdateEventUserDto updateEventUserDto,
                                                                      long userId, long eventId) {

        /*
        - изменить можно только отмененные события или события в состоянии ожидания модерации (Ожидается код ошибки 409)
        - дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента (Ожидается код ошибки 409)
        */

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

                            return ResponseEntity
                                    .status(HttpStatus.NOT_FOUND)
                                    .body(new ApiError(
                                            "404",
                                            "Not Found.",
                                            "Category doesn't exist."
                                    ));

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

                            return ResponseEntity
                                    .status(HttpStatus.BAD_REQUEST)
                                    .body(new ApiError(
                                            "400",
                                            "Bad Request.",
                                            "Invalid event date."
                                    ));
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

//                        if (event.get().getState().equals(State.PENDING)) {
//
//                            if (updateEventUserDto.getStateAction()
//                                    .equals(String.valueOf(UserStateActions.SEND_TO_REVIEW))) {
//
//                                event.get().setState(State.PENDING);
//
//                            } else if (updateEventUserDto.getStateAction()
//                                    .equals(String.valueOf(UserStateActions.CANCEL_REVIEW))) {
//
//                                event.get().setState(State.CANCELED);
//
//                            } else {
//
//                                return ResponseEntity
//                                        .status(HttpStatus.BAD_REQUEST)
//                                        .body(new ApiError(
//                                                "400",
//                                                "Bad Request.",
//                                                "Invalid state action."
//                                        ));
//                            }
//                        }

                        if (updateEventUserDto.getStateAction()
                                .equals(String.valueOf(UserStateActions.CANCEL_REVIEW))) {

                            event.get().setState(State.CANCELED);

                        } else if (!updateEventUserDto.getStateAction()
                                .equals(String.valueOf(UserStateActions.SEND_TO_REVIEW))) {

                            return ResponseEntity
                                        .status(HttpStatus.BAD_REQUEST)
                                        .body(new ApiError(
                                                "400",
                                                "Bad Request.",
                                                "Invalid state action."
                                        ));

                        }
                    }

                    if (updateEventUserDto.getTitle() != null) {
                        event.get().setTitle(updateEventUserDto.getTitle());
                    }

//                    if (!event.get().getState().equals(State.CANCELED)) {
//                        event.get().setState(State.PENDING);
//                    }

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(eventRepository.save(event.get()));

                } else {

                    return ResponseEntity
                            .status(HttpStatus.CONFLICT)
                            .body(new ApiError(
                                    "409",
                                    "Conflict.",
                                    "Event is not in the right state."
                            ));
                }

            } else {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiError(
                                "404",
                                "Not Found.",
                                "Event doesn't exist."
                        ));

            }

        } else {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(
                            "404",
                            "Not Found.",
                            "User doesn't exist."
                    ));
        }

//        Optional<User> user = userRepository.findById(userId);
//        Optional<Event> event;
//
//        if (user.isPresent() && user.get().getClass().equals(User.class)) {
//
//            event = eventRepository.findByIdAndInitiator(eventId, user.get());
//            if (event.isPresent() && event.get().getClass().equals(Event.class)) {
//
//                if (event.get().getState().equals(State.PENDING)
//                        || event.get().getState().equals(State.CANCELLED)) {
//
//                    if (updateEventUserDto.getStateAction() != null
//                            && updateEventUserDto.getStateAction()
//                            .equals(String.valueOf(UserStateActions.SEND_TO_REVIEW))) {
//
//                        if (EventValidation.isEventDateValidForUpdate(updateEventUserDto, event.get())
//                                && event.get().getEventDate() != null && updateEventUserDto.getEventDate() != null) {
//
//                            Category category = categoryRepository
//                                    .findById(updateEventUserDto.getCategory()).get();
//
//                            event.get().setAnnotation(updateEventUserDto.getAnnotation());
//                            event.get().setCategory(category);
//                            event.get().setDescription(updateEventUserDto.getDescription());
//                            event.get().setEventDate(updateEventUserDto.getEventDate());
//                            event.get().setLocation(updateEventUserDto.getLocation());
//                            event.get().setPaid(updateEventUserDto.getPaid());
//                            event.get().setParticipantLimit(updateEventUserDto.getParticipantLimit());
//                            event.get().setRequestModeration(updateEventUserDto.getRequestModeration());
//                            event.get().setTitle(updateEventUserDto.getTitle());
//                            event.get().setState(State.PENDING);
//
//                            return ResponseEntity
//                                    .status(HttpStatus.OK)
//                                    .body(eventRepository.save(event.get()));
//
//                        } else {
//
//                            return ResponseEntity
//                                    .status(HttpStatus.BAD_REQUEST)
//                                    .body(new ApiError(
//                                            "400",
//                                            "Bad Request.",
//                                            "Invalid event date, method not allowed."
//                                    ));
//                        }
//
//                    } else if (updateEventUserDto.getStateAction() != null
//                            && updateEventUserDto.getStateAction()
//                            .equals(String.valueOf(UserStateActions.CANCEL_REVIEW))) {
//
//                        if (EventValidation.isEventDateValidForUpdate(updateEventUserDto, event.get())
//                                && event.get().getEventDate() != null && updateEventUserDto.getEventDate() != null) {
//
//                            Category category = categoryRepository
//                                    .findById(updateEventUserDto.getCategory()).get();
//
//                            event.get().setAnnotation(updateEventUserDto.getAnnotation());
//                            event.get().setCategory(category);
//                            event.get().setDescription(updateEventUserDto.getDescription());
//                            event.get().setEventDate(updateEventUserDto.getEventDate());
//                            event.get().setLocation(updateEventUserDto.getLocation());
//                            event.get().setPaid(updateEventUserDto.getPaid());
//                            event.get().setParticipantLimit(updateEventUserDto.getParticipantLimit());
//                            event.get().setRequestModeration(updateEventUserDto.getRequestModeration());
//                            event.get().setTitle(updateEventUserDto.getTitle());
//                            event.get().setState(State.CANCELLED);
//
//                            return ResponseEntity
//                                    .status(HttpStatus.OK)
//                                    .body(eventRepository.save(event.get()));
//
//                        } else {
//
//                            return ResponseEntity
//                                    .status(HttpStatus.BAD_REQUEST)
//                                    .body(new ApiError(
//                                            "400",
//                                            "Bad Request.",
//                                            "Invalid event date, method not allowed."
//                                    ));
//                        }
//
//                    } else {
//
//                        return ResponseEntity
//                                .status(HttpStatus.BAD_REQUEST)
//                                .body(new ApiError(
//                                        "400",
//                                        "Bad Request.",
//                                        "Invalid state action, method not allowed."
//                                ));
//                    }
//
//                } else {
//
//                    return ResponseEntity
//                            .status(HttpStatus.FORBIDDEN)
//                            .body(new ApiError(
//                                    "403",
//                                    "Forbidden.",
//                                    "Only pending or canceled events can be changed."
//                            ));
//                }
//
//            } else {
//
//                return ResponseEntity
//                        .status(HttpStatus.NOT_FOUND)
//                        .body(new ApiError(
//                                "404",
//                                "Not found.",
//                                "Event doesn't exist."
//                        ));
//            }
//
//        } else {
//
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(new ApiError(
//                            "404",
//                            "Not found.",
//                            "User doesn't exist."
//                    ));
//        }
    }

    @Override
    public ResponseEntity<Object> getUserEventRequestByUserIdAndEventIdPrivate(long userId, long eventId) {

        Optional<User> requester = userRepository.findById(userId);
        if (requester.isPresent() && requester.get().getClass().equals(User.class)) {

            Optional<Event> event = eventRepository.findById(eventId);
            if (event.isPresent() && event.get().getClass().equals(Event.class)) {

                Optional<List<ParticipationRequest>> participationRequests =
//                        requestRepository.findAllByRequesterIdAndEventId(
//                                requester.get().getId(),
//                                event.get().getId()
//                        );
                requestRepository.findAllByEventId(event.get().getId());

                if (!participationRequests.get().isEmpty()) {

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(ParticipationRequestMapper
                                    .toParticipationRequestDto(participationRequests.get())
                            );

                } else {

                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(new ApiError(
                                    "404",
                                    "Not Found.",
                                    "Participant requests don't exist."
                            ));

                }

            } else {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiError(
                                "404",
                                "Not Found.",
                                "Event doesn't exist."
                        ));
            }

        } else {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(
                            "404",
                            "Not Found.",
                            "User doesn't exist."
                    ));
        }

//        Optional<User> requester = userRepository.findById(userId);
//        if (requester.isPresent() && requester.get().getClass().equals(User.class)) {
//            Optional<Event> event = eventRepository.findById(eventId);
//
//            if (event.isPresent() && event.get().getClass().equals(Event.class)) {
//
//                Optional<ParticipationRequest> participationRequest =
//                        requestRepository.findByRequesterIdAndEventId(
//                                requester.get().getId(),
//                                event.get().getId()
//                        );
//
//                if (participationRequest.isPresent() && participationRequest.get().getClass()
//                        .equals(ParticipationRequest.class)) {
//
//                    return ResponseEntity
//                            .status(HttpStatus.OK)
//                            .body(ParticipationRequestMapper
//                                    .toParticipationRequestDto(participationRequest.get())
//                            );
//                } else {
//
//                    return ResponseEntity
//                            .status(HttpStatus.NOT_FOUND)
//                            .body(new ApiError(
//                                    "404",
//                                    "Not Found.",
//                                    "Participation request doesn't exist."
//                            ));
//                }
//
//            } else {
//
//                return ResponseEntity
//                        .status(HttpStatus.NOT_FOUND)
//                        .body(new ApiError(
//                                "404",
//                                "Not Found.",
//                                "Event doesn't exist."
//                        ));
//            }
//
//        } else {
//
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(new ApiError(
//                            "404",
//                            "Not Found.",
//                            "Requester doesn't exist."
//                    ));
//        }
    }

    @Override
    public ResponseEntity<Object> patchUserEventRequestStatusByUserIdAndEventIdPrivate(
            EventRequestStatusUpdateRequest updateRequest, long userId, long eventId) {

        /*
        - если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
        - нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)
        - статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)
        - если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
        */


        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent() && user.get().getClass().equals(User.class)) {
            Optional<Event> event = eventRepository.findById(eventId);

            if (event.isPresent() && event.get().getClass().equals(Event.class)) {

                if (event.get().getParticipantLimit().equals(event.get().getConfirmedRequests())
                        && event.get().getParticipantLimit() != 0) {

                    return ResponseEntity
                            .status(HttpStatus.CONFLICT)
                            .body(new ApiError(
                                    "409",
                                    "Conflict.",
                                    "Participant limit is full."
                            ));

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

                        return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(EventRequestMapper
                                        .toEventRequestStatusUpdateResult(requests.get()));

                    } else if (updateRequest.getStatus().equals(Status.REJECTED)) {

                        for (int i = 0; i < requests.get().size(); i++) {
                            if (requests.get().get(i).getStatus().equals(Status.PENDING)
                                    && event.get().getParticipantLimit() > event.get().getConfirmedRequests()) {
                                requests.get().get(i).setStatus(Status.REJECTED);
                                requestRepository.save(requests.get().get(i));
                            }
                        }

                        return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(EventRequestMapper
                                        .toEventRequestStatusUpdateResult(requests.get()));

                    } else {

                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(new ApiError(
                                        "400",
                                        "Bad Request.",
                                        "Method not allowed."
                                ));

                    }

                } else {

                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(new ApiError(
                                    "404",
                                    "Not Found.",
                                    "Requests do not exist."
                            ));

                }

            } else {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiError(
                                "404",
                                "Not Found.",
                                "Event doesn't exist."
                        ));

            }

        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(
                            "404",
                            "Not Found.",
                            "User doesn't exist."
                    ));
        }
    }

    // ---------------------   PUBLIC   --------------------- //

    @Override
    public ResponseEntity<Object> getAllEventsPublic(HttpServletRequest request, String text, long[] categories,
                                                     Boolean paid, String rangeStart, String rangeEnd,
                                                     Boolean onlyAvailable, String sort, int from, int size) {
        /*
        - только опубликованные события                         -   DONE (ALMOST)
        - текстовый поиск должен быть без учета регистра букв   -   DONE
        - если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события,
         которые произойдут позже текущей даты и времени
        - информация о каждом событии должна включать в себя количество просмотров и
         количество уже одобренных заявок на участие
        - информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
         нужно сохранить в сервисе статистикиэто публичный эндпоинт,
          соответственно в выдаче должны быть только опубликованные события
        - в случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
        */


        //  ---------------------------------------------------------------------------------------


        Predicate predicate = QPredicates.builder()
                .add(text, QEvent.event.annotation::containsIgnoreCase)
                .add(text, QEvent.event.description::containsIgnoreCase)
                //.add()
                .add(paid, QEvent.event.paid::eq)
                .add(rangeStart, QEvent.event.eventDate::gt)
                .add(rangeEnd, QEvent.event.eventDate::lt)
                //.add(onlyAvailable, QEvent.event.participantLimit.lt(QEvent.event.))
                .buildAnd();

        RequestDataDto requestDataDto = new RequestDataDto(
                "main-service",
                request.getRemoteAddr(),
                request.getRequestURI(),
                String.valueOf(LocalDateTime.now())
        );

        httpClient.saveRequestData(requestDataDto);

        Page<Event> result;

        List<Long> categoryIds;
        List<Event> eventsByCat;
        List<EventWithLDT> eventsByCatLTD;

        if (predicate == null) {

            Page<Event> events = eventRepository.findAll(PageRequest.of(from, size));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(events.getContent());
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
                            //.filter(x -> x.getState().equals(State.PENDING))
                            .filter(x -> categoryIds.contains(
                                            x.getCategory().getId()
                                    )
                            )
                            .sorted(Comparator.comparing(EventWithLDT::getEventDate).reversed())
                            .collect(Collectors.toList());

                    //  httpClient.saveRequestData(requestDataDto);

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(eventsByCatLTD);

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

                    //  httpClient.saveRequestData(requestDataDto);

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(eventsByCat);

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

                    //  httpClient.saveRequestData(requestDataDto);

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(eventsByCat);
                }

            } else {

                if (sort != null && sort.equals(String.valueOf(SortTypes.EVENT_DATE))) {

                    result = eventRepository.findAll(
                            predicate,
                            PageRequest.of(from, size)
                    );

                    List<EventWithLDT> foundEvents = EventMapper.toEventWithLDT(result.getContent());

                    eventsByCatLTD = foundEvents.stream()
                            .distinct()
                            // .filter(x -> x.getConfirmedRequests() < x.getParticipantLimit())
                            //.filter(x -> x.getState().equals(State.PENDING))
                            .filter(x -> categoryIds.contains(
                                            x.getCategory().getId()
                                    )
                            )
                            .sorted(Comparator.comparing(EventWithLDT::getEventDate).reversed())
                            .collect(Collectors.toList());

                    //  httpClient.saveRequestData(requestDataDto);

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(eventsByCatLTD);

                } else if (sort != null && sort.equals(String.valueOf(SortTypes.VIEWS))) {

                    result = eventRepository.findAll(
                            predicate,
                            PageRequest.of(from, size)
                    );

                    eventsByCat = result.getContent().stream()
                            .distinct()
                            //.filter(x -> x.getConfirmedRequests() < x.getParticipantLimit())
                            .filter(x -> categoryIds.contains(
                                            x.getCategory().getId()
                                    )
                            )
                            .sorted(Comparator.comparing(Event::getViews).reversed())
                            .collect(Collectors.toList());

                    //  httpClient.saveRequestData(requestDataDto);

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(eventsByCat);
                } else {

                    result = eventRepository.findAll(
                            predicate,
                            PageRequest.of(from, size)
                    );

                    eventsByCat = result.getContent().stream()
                            .distinct()
                            //  .filter(x -> x.getConfirmedRequests() < x.getParticipantLimit())
                            .filter(x -> categoryIds.contains(
                                            x.getCategory().getId()
                                    )
                            )
                            .collect(Collectors.toList());

                    //  httpClient.saveRequestData(requestDataDto);

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(eventsByCat);
                }
            }
//                categoryIds  = Arrays
//                        .stream(categories)
//                        .boxed()
//                        .collect(Collectors.toList());


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
                            //.filter(x -> x.getState().equals(State.PENDING))
                            .filter(x -> x.getEventDate().isAfter(LocalDateTime.now()))
                            .sorted(Comparator.comparing(EventWithLDT::getEventDate).reversed())
                            .collect(Collectors.toList());

                    //  httpClient.saveRequestData(requestDataDto);

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(eventsByCatLTD);

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

                    //  httpClient.saveRequestData(requestDataDto);

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(eventsByCatLTD);


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

                    //  httpClient.saveRequestData(requestDataDto);

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(eventsByCatLTD);
                }

            } else if (onlyAvailable != null) {

                if (sort != null && sort.equals(String.valueOf(SortTypes.EVENT_DATE))) {

                    result = eventRepository.findAll(
                            predicate,
                            PageRequest.of(from, size)
                    );

                    List<EventWithLDT> foundEvents = EventMapper.toEventWithLDT(result.getContent());

                    eventsByCatLTD = foundEvents.stream()
                            .distinct()
                            //.filter(x -> x.getConfirmedRequests() < x.getParticipantLimit())
                            //.filter(x -> x.getState().equals(State.PENDING))
                            //.filter(x -> x.getEventDate().isAfter(LocalDateTime.now()))
                            .sorted(Comparator.comparing(EventWithLDT::getEventDate).reversed())
                            .collect(Collectors.toList());

                    //  httpClient.saveRequestData(requestDataDto);

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(eventsByCatLTD);

                } else if (sort != null && sort.equals(String.valueOf(SortTypes.VIEWS))) {

                    result = eventRepository.findAll(
                            predicate,
                            PageRequest.of(from, size)
                    );

                    List<EventWithLDT> foundEvents = EventMapper.toEventWithLDT(result.getContent());

                    eventsByCatLTD = foundEvents.stream()
                            .distinct()
                           // .filter(x -> x.getConfirmedRequests() < x.getParticipantLimit())
                           // .filter(x -> x.getEventDate().isAfter(LocalDateTime.now()))
                            .sorted(Comparator.comparing(EventWithLDT::getViews).reversed())
                            .collect(Collectors.toList());

                    //  httpClient.saveRequestData(requestDataDto);

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(eventsByCatLTD);

                } else {

                    result = eventRepository.findAll(
                            predicate,
                            PageRequest.of(from, size)
                    );

                    List<EventWithLDT> foundEvents = EventMapper.toEventWithLDT(result.getContent());

                    eventsByCatLTD = foundEvents.stream()
                            .distinct()
                            //.filter(x -> x.getConfirmedRequests() < x.getParticipantLimit())
                            //.filter(x -> x.getEventDate().isAfter(LocalDateTime.now()))
                            .collect(Collectors.toList());

                    //  httpClient.saveRequestData(requestDataDto);

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(eventsByCatLTD);

                }

            } else {

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(eventRepository.findAll(predicate, PageRequest.of(from, size)));

            }
        }





    //  Iterable<Event> result2 = eventRepository.findAll(predicate, PageRequest.of(from, size));

//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(eventRepository.findAll(predicate, PageRequest.of(from, size)));

        //  ---------------------------------------------------------------------------------------









//        BooleanExpression byAnnotation = QEvent.event.annotation.containsIgnoreCase(text);
//        BooleanExpression byDescription = QEvent.event.description.containsIgnoreCase(text);
//        BooleanExpression byPaid = QEvent.event.paid.eq(paid);
//        BooleanExpression byRangeStart = QEvent.event.eventDate.gt(rangeStart);
//        BooleanExpression byRangeEnd = QEvent.event.eventDate.lt(rangeEnd);

//        OrderSpecifier<String> bySort = QEvent.event.eventDate.desc();


//        Iterable<Event> foundEvents = eventRepository.findAll(
//                byAnnotation
//                        .and(byDescription)
//                        .and(byPaid)
//                        .and(byRangeStart)
//                        .and(byRangeEnd)
//        );

       // JPAQueryFactory queryFactory = new JPAQueryFactory(em);

//        QEvent event = QEvent.event;
//        queryFactory.selectFrom(event)
//                .orderBy(event.eventDate.desc());
        /*QCustomer customer = QCustomer.customer;
queryFactory.selectFrom(customer)
    .orderBy(customer.lastName.asc(), customer.firstName.desc())
    .fetch();*/

     //   return ResponseEntity.status(HttpStatus.OK).body(foundEvents);

//
//        List<BooleanExpression> categoryIds = new ArrayList<>();


        //BooleanExpression byCategories = QEvent.event.category.id.in(Arrays.stream(categories).c);


//        BooleanExpression byPaid = QEvent.event.paid.eq(paid);



    }

    @Override
    public ResponseEntity<Object> getEventByIdPublic(long eventId, HttpServletRequest request) {

        /*
        - событие должно быть опубликовано
        - информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов
        - информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
         нужно сохранить в сервисе статистики
        - в случае, если события с заданным id не найдено, возвращает статус код 404
         */

        Optional<Event> event = eventRepository.findById(eventId);

        RequestDataDto requestDataDto = new RequestDataDto(
                "main-service",
                request.getRemoteAddr(),
                request.getRequestURI(),
                String.valueOf(LocalDateTime.now())
        );

        if (event.isPresent() && event.get().getClass().equals(Event.class)) {

            if (event.get().getState().equals(State.PUBLISHED)) {

                //eventRepository.incrementEventViewsByOne(eventId);

                event.get().setViews(event.get().getViews() + 1);

                httpClient.saveRequestData(requestDataDto);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(event.get());

            } else {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiError(
                                "404",
                                "Not Found.",
                                "Event hasn't been published."
                        ));

            }

        } else {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(
                            "404",
                            "Not Found.",
                            "Event doesn't exist."
                    ));
        }
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(event);

//        if (event.isEmpty()) {
//
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(new ApiError());
//        } else {
//
//            if (event.get().getState() == State.PUBLISHED) {
//
//                RequestDataDto requestDataDto = new RequestDataDto(
//                        "main-service",
//                        request.getRemoteAddr(),
//                        request.getRequestURI(),
//                        String.valueOf(LocalDateTime.now())
//                );
//
////                eventRepository.incrementEventViewsByOne(eventId);
////                httpClient.saveRequestData(requestDataDto);
//
//            } else {
//
//                return ResponseEntity
//                        .status(HttpStatus.NOT_FOUND)
//                        .body(new ApiError(
//                                "404",
//                                "Event has not been published yet.",
//                                "Event unavailable.")
//                        );
//            }
//
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body(new ApiError(
//                            "400",
//                            "Bad Request.",
//                            "Method unavailable.")
//                    );
//        }
    }


    // ---------------------   ADMIN   --------------------- //

    @Override
    public ResponseEntity<Object> getAllEventsAdmin(HttpServletRequest request, long[] users,
                                                    String[] statesStr, long[] categories,
                                                    String rangeStart, String rangeEnd,
                                                    int from, int size) {

        /*
        - Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия
        - В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
        */

        Page<Event> result;

        List<Event> events;

        Predicate predicate = QPredicates.builder()
                .add(rangeStart, QEvent.event.eventDate::gt)
                .add(rangeEnd, QEvent.event.eventDate::lt)
                .buildAnd();

        // requestDataDto....

//        RequestDataDto requestDataDto = new RequestDataDto(
//                "main-service",
//                request.getRemoteAddr(),
//                request.getRequestURI(),
//                String.valueOf(LocalDateTime.now())
//        );

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

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(foundEvents.getContent());
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

            //  httpClient.saveRequestData(requestDataDto);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(events);

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

            //  httpClient.saveRequestData(requestDataDto);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(events);

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

            //  httpClient.saveRequestData(requestDataDto);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(events);

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

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(events);

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

            //  httpClient.saveRequestData(requestDataDto);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(events);

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

            //  httpClient.saveRequestData(requestDataDto);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(events);

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

            //  httpClient.saveRequestData(requestDataDto);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(events);

        } else {

            result = eventRepository.findAll(
                    predicate,
                    PageRequest.of(from, size)
            );

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(result.getContent());

        }

    }

    @Override
    public ResponseEntity<Object> patchEventDataAdmin(UpdateEventAdminDto updateEventAdminDto, long eventId) {

        /*
        - Дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (Ожидается код ошибки 409)
        - Событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409)
        - Событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)
        */

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

                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(new ApiError(
                                    "404",
                                    "Not Found.",
                                    "Category doesn't exist."
                            ));

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

                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(new ApiError(
                                    "400",
                                    "Bad Request.",
                                    "Invalid event date."
                            ));
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

                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(new ApiError(
                                        "400",
                                        "Bad Request.",
                                        "Invalid state action."
                                ));
                    }

                } else {

                    return ResponseEntity
                            .status(HttpStatus.CONFLICT)
                            .body(new ApiError(
                               "409",
                               "Conflict.",
                               "Event is not in the right state."
                            ));

                }

            }

            if (updateEventAdminDto.getTitle() != null) {
                event.get().setTitle(updateEventAdminDto.getTitle());
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(eventRepository.save(event.get()));

        } else {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(
                       "404",
                       "Not Found.",
                            "Event doesn't exist."
                    ));

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
