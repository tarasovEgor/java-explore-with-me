package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.user.model.User;


public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    Optional<ParticipationRequest> findByRequester(User requester);

    Optional<ParticipationRequest> findByRequesterAndEvent(User requester, Event event);

    Optional<List<ParticipationRequest>> findAllByIdIn(Long[] ids);


}
