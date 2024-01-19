package ru.practicum.ewm.request.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ru.practicum.ewm.error.ApiError;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.request.repository.ParticipationRequestRepository;
import ru.practicum.ewm.request.service.ParticipationRequestService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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
    public ResponseEntity<Object> saveParticipationRequestPrivate(ParticipationRequestDto participationRequestDto, long userId) {

        //  ДОДЕЛАТЬ


        /*
        - нельзя добавить повторный запрос (Ожидается код ошибки 409)
        - инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)
        - нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)
        - если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)
        - если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного
        */

        Optional<User> requester = userRepository.findById(userId);
        if (requester.isPresent() && requester.get().getClass().equals(User.class)) {

            Optional<Event> event = eventRepository.findById(participationRequestDto.getEvent());
            if (event.isPresent() && event.get().getClass().equals(Event.class)) {

                DateTimeFormatter formatter
                        = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                ParticipationRequest participationRequest =
                        ParticipationRequestMapper.toParticipantRequest(
                                requester.get(),
                                event.get(),
                                Status.PENDING
                        );

                String createdOn = now().format(formatter);
                participationRequest.setCreated(createdOn);

                ParticipationRequest savedRequest =
                        requestRepository.save(participationRequest);

                participationRequestDto.setId(savedRequest.getId());
                participationRequestDto.setRequester(requester.get().getId());
                participationRequestDto.setEvent(event.get().getId());
                participationRequestDto.setCreated(LocalDateTime.parse(createdOn, formatter));
                participationRequestDto.setStatus(Status.PENDING);


                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(participationRequestDto);
            }
        }

        return null;
    }

    @Override
    public ResponseEntity<Object> getParticipationRequestByUserIdPrivate(long userId) {

        /*
        *   CHECKS !!!
        * */

        Optional<User> requester = userRepository.findById(userId);
        if (requester.isPresent() && requester.get().getClass().equals(User.class)) {
            Optional<ParticipationRequest> participationRequest =
                    requestRepository.findByRequester(requester.get());

            if (participationRequest.isPresent() && participationRequest.get().getClass()
                    .equals(ParticipationRequest.class)) {

                ParticipationRequestDto foundRequest =
                        ParticipationRequestMapper.toParticipationRequestDto(participationRequest.get());

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(foundRequest);
            }
        }



        return null;
    }

    @Override
    public ResponseEntity<Object> cancelParticipationRequestByUserIdPrivate(long userId, long requestId) {

        Optional<User> requester = userRepository.findById(userId);
        if (requester.isPresent() && requester.get().getClass().equals(User.class)) {

            Optional<ParticipationRequest> participationRequest = requestRepository.findById(requestId);
            if (participationRequest.isPresent() && participationRequest.get().getClass()
                    .equals(ParticipationRequest.class)) {

                if (participationRequest.get().getRequester().equals(requester.get())
                        && participationRequest.get().getStatus().equals(Status.PENDING)) {

                    participationRequest.get().setStatus(Status.CANCELLED);

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(ParticipationRequestMapper
                                    .toParticipationRequestDto(
                                            requestRepository.save(participationRequest.get())
                                    ));

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
                                "Participant request doesn't exist."
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
}