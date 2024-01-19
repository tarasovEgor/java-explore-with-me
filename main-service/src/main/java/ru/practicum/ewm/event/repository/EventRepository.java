package ru.practicum.ewm.event.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {


    /*
    @Query("select i from Item i" +
            " where upper(i.name) like upper(concat('%', ?1, '%'))" +
            " or upper(i.description) like upper(concat('%', ?1, '%'))" +
            " and i.available = true")
    List<Item> search(String text);

    "select b from Booking as b" +
            " where b.booker = ?1 and" +
            " b.start <= ?2 and b.end >= ?2" +
            " order by b.start DESC"
    */

    // ------------------  PUBLIC  ------------------


    Page<Event> findAllByCategoryIdIn(long[] ids, Predicate predicate, Pageable pageable);

    @Query("select e from Event e" +
            " where e.category.id in ?2" +
            " and upper(e.annotation) like upper(concat('%',?1, '%'))" +
            " or upper(e.description) like upper(concat('%',?1, '%'))" +
            " and e.paid = ?3" +
            " and e.eventDate >= ?4 and e.eventDate <= ?5" +
            " and e.confirmedRequests < e.participantLimit" +
            " order by e.eventDate DESC")
    List<Event> findAllByTextAndCategoriesAllParamsAreNotNullAndOnlyAvailableIsTrueAndSortIsEqualToEventDate(String text, long[] categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, int from, int size);

    @Query("select e from Event e" +
            " where e.category.id in ?2" +
            " and upper(e.annotation) like upper(concat('%',?1, '%'))" +
            " or upper(e.description) like upper(concat('%',?1, '%'))" +
            " and e.paid = ?3" +
            " and e.eventDate >= ?4 and e.eventDate <= ?5" +
            " order by e.views DESC")
    List<Event> findAllByTextAndCategoriesAllParamsAreNotNullAndOnlyAvailableIsFalseAndSortIsEqualToViews(String text, long[] categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, int from, int size);
    /*String text, long[] categories, Boolean paid,
                                                     String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                     String sort, int from, int size*/


    // ------------------  PRIVATE METHODS  ----------------
    List<Event> findAllByInitiator(User user, Pageable pageable);

    Optional<Event> findByIdAndInitiator(long eventId, User user);

    @Transactional
    Optional<Set<Event>> findAllByIdIn(List<Long> eventIds);


    // ------------------  UTILS METHODS  ------------------

    @Modifying
    @Transactional
    @Query("update Event e" +
            " set e.views = e.views + 1" +
            " where e.id = ?1")
    void incrementEventViewsByOne(long eventId);

    /*@Modifying
    @Query("update Booking b " +
            " set b.status = ?1" +
            " where b.id = ?2 and b.booker = ?3")
    void updateBookingStatus(BookingStatus  status, long bookingId, User booker);*/

}