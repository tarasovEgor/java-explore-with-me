package ru.practicum.ewm.request.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ru.practicum.ewm.error.ApiError;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.BadRequestMethodException;
import ru.practicum.ewm.exception.event.EventDoesNotExistException;
import ru.practicum.ewm.exception.event.InvalidEventStateException;
import ru.practicum.ewm.exception.request.InvalidInitiatorException;
import ru.practicum.ewm.exception.request.ParticipationRequestDoesNotExistException;
import ru.practicum.ewm.exception.request.ParticipationRequestLimitIsFullException;
import ru.practicum.ewm.exception.request.RepeatedRequestException;
import ru.practicum.ewm.exception.user.UserDoesNotExistException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.request.repository.ParticipationRequestRepository;
import ru.practicum.ewm.request.service.ParticipationRequestService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.List;

import static java.time.LocalDateTime.now;

@Slf4j
@Service
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public ParticipationRequestServiceImpl(ParticipationRequestRepository requestRepository,
                                           EventRepository eventRepository,
                                           UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ParticipationRequestDto saveParticipationRequestPrivate(long userId, long eventId) {
        Optional<User> requester = userRepository.findById(userId);
        if (requester.isPresent() && requester.get().getClass().equals(User.class)) {

            Optional<Event> event = eventRepository.findById(eventId);
            if (event.isPresent() && event.get().getClass().equals(Event.class)) {

                if (!event.get().getState().equals(State.PENDING)) {

                    Optional<ParticipationRequest> participationRequest =
                            requestRepository.findByRequester(requester.get());

                    if (participationRequest.isPresent() && participationRequest.get().getClass()
                            .equals(ParticipationRequest.class)) {

                        throw new RepeatedRequestException("Unable to send a repeated request.");
//                        return ResponseEntity
//                                .status(HttpStatus.CONFLICT)
//                                .body(new ApiError(
//                                        "409",
//                                        "Conflict.",
//                                        "Unable to send a repeated request."
//                                ));

                    }

                    if (event.get().getInitiator().getId().equals(requester.get().getId())) {

                        throw new InvalidInitiatorException("Initiator can't send a request to his own event.");
//                        return ResponseEntity
//                                .status(HttpStatus.CONFLICT)
//                                .body(new ApiError(
//                                        "409",
//                                        "Conflict.",
//                                        "Initiator can't send a request to his own event."
//                                ));

                    }

                    if (event.get().getParticipantLimit() < event.get().getConfirmedRequests() + 1
                            && event.get().getParticipantLimit() != 0) {

                        throw new ParticipationRequestLimitIsFullException("Participation request limit is full.");
//                        return ResponseEntity
//                                .status(HttpStatus.CONFLICT)
//                                .body(new ApiError(
//                                        "409",
//                                        "Conflict.",
//                                        "Participant limit is full."
//                                ));

                    }

                    DateTimeFormatter formatter =
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    String createdOn = now().format(formatter);

                    ParticipationRequest newParticipationRequest;

                    if (!event.get().getRequestModeration() || event.get().getParticipantLimit() == 0) {

                        event.get().setConfirmedRequests(event.get().getConfirmedRequests() + 1);

                        eventRepository.save(event.get());

                        newParticipationRequest =
                                ParticipationRequestMapper.toParticipantRequest(
                                        event.get(),
                                        requester.get(),
                                        Status.CONFIRMED,
                                        createdOn
                                );

                    } else {

                        newParticipationRequest =
                                ParticipationRequestMapper.toParticipantRequest(
                                        event.get(),
                                        requester.get(),
                                        Status.PENDING,
                                        createdOn
                                );
                    }

//                    ParticipationRequestDto participationRequestDto =
//                            ParticipationRequestMapper.toParticipationRequestDto(
//                                    requestRepository.save(newParticipationRequest)
//                            );

                    return ParticipationRequestMapper
                            .toParticipationRequestDto(requestRepository.save(newParticipationRequest));
//                    return ResponseEntity
//                            .status(HttpStatus.CREATED)
//                            .body(participationRequestDto);

                } else {

//                    return ResponseEntity
//                            .status(HttpStatus.CONFLICT)
//                            .body(new ApiError(
//                                    "409",
//                                    "Conflict.",
//                                    "Event hasn't been published yet."
//                            ));
                    throw new InvalidEventStateException("Event hasn't been published yet.");

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

            throw new UserDoesNotExistException("Requester doesn't exist.");
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(new ApiError(
//                            "404",
//                            "Not Found.",
//                            "Requester doesn't exist."
//                    ));

        }
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequestByUserIdPrivate(long userId) {
        Optional<User> requester = userRepository.findById(userId);
        if (requester.isPresent() && requester.get().getClass().equals(User.class)) {

            Optional<List<ParticipationRequest>> participationRequests =
                    requestRepository.findAllByRequesterId(requester.get().getId());

            if (!participationRequests.get().isEmpty()) {

                List<ParticipationRequestDto> foundRequest =
                        ParticipationRequestMapper.toParticipationRequestDto(participationRequests.get());

                return foundRequest;
//                return ResponseEntity
//                        .status(HttpStatus.OK)
//                        .body(foundRequest);

            } else {

                throw new ParticipationRequestDoesNotExistException("Participation requests don't exist.");
//                return ResponseEntity
//                        .status(HttpStatus.NOT_FOUND)
//                        .body(new ApiError(
//                                "404",
//                                "Not Found.",
//                                "Participation requests do not exist."
//                        ));

            }

        } else {

            throw new UserDoesNotExistException("User doesn't exist.");
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(new ApiError(
//                            "404",
//                            "Not Found.",
//                            "User does not exist."
//                    ));

        }
    }

    @Override
    public ParticipationRequestDto cancelParticipationRequestByUserIdPrivate(long userId, long requestId) {
        Optional<User> requester = userRepository.findById(userId);
        if (requester.isPresent() && requester.get().getClass().equals(User.class)) {

            Optional<ParticipationRequest> participationRequest = requestRepository.findById(requestId);
            if (participationRequest.isPresent() && participationRequest.get().getClass()
                    .equals(ParticipationRequest.class)) {

                if (participationRequest.get().getRequester().equals(requester.get())
                        && (participationRequest.get().getStatus().equals(Status.PENDING)
                        || participationRequest.get().getStatus().equals(Status.CONFIRMED))) {

                    participationRequest.get().setStatus(Status.CANCELED);

                    return ParticipationRequestMapper
                            .toParticipationRequestDto(
                                    requestRepository.save(participationRequest.get())
                            );
//                    return ResponseEntity
//                            .status(HttpStatus.OK)
//                            .body(ParticipationRequestMapper
//                                    .toParticipationRequestDto(
//                                            requestRepository.save(participationRequest.get())
//                                    ));

                } else {

                    throw new BadRequestMethodException("Method not allowed.");
//                    return ResponseEntity
//                            .status(HttpStatus.BAD_REQUEST)
//                            .body(new ApiError(
//                                    "400",
//                                    "Bad Request.",
//                                    "Method not allowed."
//                            ));
                }

            } else {

                throw new ParticipationRequestDoesNotExistException("Participation request doesn't exist.");
//                return ResponseEntity
//                        .status(HttpStatus.NOT_FOUND)
//                        .body(new ApiError(
//                                "404",
//                                "Not Found.",
//                                "Participant request doesn't exist."
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
}
