package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.RequestData;

import java.util.List;

public interface RequestDataRepository extends JpaRepository<RequestData, Long> {

    @Query("select rd from RequestData as rd" +
            " where rd.timestamp >= ?1 and rd.timestamp <= ?2" +
            " order by rd.timestamp DESC")
    List<RequestData> findAllByPeriod(String start, String end);



//    @Query("select new ru.practicum.dto.ViewStatsDto (rd.app, rd.uri, COUNT(rd.ip) as hits) from RequestData rd" +
//            " where rd.timestamp >= ?1 and rd.timestamp <= ?2" +
//            " order by hits DESC")
//    List<ViewStatsDto> findAllByPeriod(String start, String end);

    @Query("select new ru.practicum.dto.ViewStatsDto (rd.app, rd.uri, COUNT(rd.ip) as hits)" +
            " from RequestData rd" +
            " where rd.timestamp >= ?1 and rd.timestamp <= ?2" +
            " group by rd.ip, rd.uri, rd.app" +
            " order by hits DESC")
    List<ViewStatsDto> findAllByPeriodIpIsUnique(String start, String end);

    @Query("select new ru.practicum.dto.ViewStatsDto (rd.app, rd.uri, COUNT(rd.ip) as hits)" +
            " from RequestData rd" +
            " where rd.uri in (?1)" +
            " and rd.timestamp >= ?2 and rd.timestamp <= ?3" +
            " group by rd.ip, rd.uri, rd.app" +
            " order by hits DESC")
    List<ViewStatsDto> findAllByPeriodAndUrisAndIpIsUnique(String[] uris, String start, String end);

    @Query("select new ru.practicum.dto.ViewStatsDto (rd.app, rd.uri, COUNT(rd.ip) as hits)" +
            " from RequestData rd" +
            " where rd.uri in (?1)" +
            " and rd.timestamp >= ?2 and rd.timestamp <= ?3" +
            " order by rd.timestamp DESC")
    List<ViewStatsDto> findAllByPeriodAndUris(String[] uris, String start, String end);


//    @Query("select rd from RequestData as rd" +
//            " where rd.timestamp >= ?1 and rd.timestamp <= ?2" +
//            " group by rd.ip, rd.id order by COUNT(rd.ip)")
//    List<RequestData> findAllRequestDataByPeriodIpIsUnique(String start, String end);





//    @Query("select rd from RequestData as rd" +
//            " where rd.timestamp >= ?1 and rd.timestamp <= ?2" +
//            " and rd.ip in (select rd.ip from RequestData as rd" +
//            " group by rd.ip having COUNT(rd.ip) = 1)")
//    List<RequestData> findAllRequestDataByPeriodIpIsUnique(String start, String end);




//  ---------------

//    @Query("select rd from RequestData as rd" +
//            " where rd.timestamp >= ?1 and rd.timestamp <= ?2" +
//            " group by rd.id, rd.ip")
//    List<RequestData> findAllRequestDataByPeriodIpIsUnique(String start, String end);

    //-------------
//    @Query("select rd from RequestData as rd" +
//            " where rd.timestamp >= ?1 and rd.timestamp <= ?2" +
//            " and rd.ip in (select DISTINCT(rd.ip) from RequestData as rd)" +
//            " group by rd.id, rd.ip HAVING COUNT(rd.ip) = 1")
//    List<RequestData> findAllRequestDataByPeriodIpIsUnique(String start, String end);

    @Query("select COUNT(rd.ip) from RequestData as rd" +
            " where rd.ip = ?1")
    Long findRequestDataHitCount(String ip);


    // -------------------




    /*@Query("select b from Booking as b" +
            " join b.item as i" +
            " where i.owner = ?1 and" +
            " b.start <= ?2 and b.end >= ?2" +
            " order by b.start DESC")
    List<Booking> findAllBookingsByItemOwnerAndStatusCurrent(User owner, LocalDateTime now);*/

}
