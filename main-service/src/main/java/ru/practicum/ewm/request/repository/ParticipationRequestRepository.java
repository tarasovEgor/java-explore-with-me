package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.user.model.User;


public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    Optional<ParticipationRequest> findByRequester(User requester);

    Optional<List<ParticipationRequest>> findAllByRequesterId(long requesterId);

    Optional<List<ParticipationRequest>> findAllByEventId(long eventId);

    Optional<List<ParticipationRequest>> findAllByRequesterIdAndEventId(long requesterId, long eventId);

    Optional<ParticipationRequest> findByRequesterIdAndEventId(long requesterId, long eventId);

    Optional<List<ParticipationRequest>> findAllByIdIn(Long[] ids);


}
