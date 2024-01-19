package ru.practicum.ewm.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
