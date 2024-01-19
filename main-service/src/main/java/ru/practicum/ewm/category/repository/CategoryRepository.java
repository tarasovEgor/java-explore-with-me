package ru.practicum.ewm.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Modifying
    @Transactional
    @Query("update Category c" +
            " set c.name = ?1" +
            " where c.id = ?2")
    void update(String name, Long id);

    //  PAGINATION METHODS
   // Page<Category> findAll

//    Page<Booking> findAllByBookerAndStatusOrderByStartDesc(User booker, BookingStatus status, Pageable pageable);

}
