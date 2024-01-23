package ru.practicum.ewm.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.practicum.ewm.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    List<Compilation> getAllByPinnedTrue(Pageable pageable);

    List<Compilation> getAllByPinnedFalse(Pageable pageable);

//    @Query("select compilation_id from compilation_event" +
//            " where event_id = ?1")
//    List<Long> getAllEventIdsByCategory(long eventId);

    // List<Long> getAll

//    @Query("select e from Event e" +
//            " where e.compilation = ?1")
//    Optional<List<Event>> getAllEventEdsByCompilation(Compilation compilation);
}
