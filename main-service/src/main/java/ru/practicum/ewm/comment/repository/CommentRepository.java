package ru.practicum.ewm.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByEvent(Event event, Pageable pageable);

}
