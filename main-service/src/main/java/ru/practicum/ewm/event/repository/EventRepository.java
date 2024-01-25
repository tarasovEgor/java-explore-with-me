package ru.practicum.ewm.event.repository;

import java.util.List;
import java.util.Optional;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    Page<Event> findByEventDateAfter(String date, Pageable pageable);

    List<Event> findAllByInitiator(User user, Pageable pageable);

    Optional<Event> findByIdAndInitiator(long eventId, User user);

    Optional<List<Event>> findAllByIdIn(List<Long> eventIds);

}
