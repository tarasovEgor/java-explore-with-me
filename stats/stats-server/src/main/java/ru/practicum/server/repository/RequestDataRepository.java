package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.RequestData;

import java.util.List;

public interface RequestDataRepository extends JpaRepository<RequestData, Long> {

    @Query("select new ru.practicum.dto.ViewStatsDto (rd.app, rd.uri, COUNT(rd.ip) as hits)" +
            " from RequestData rd" +
            " where rd.timestamp >= ?1 and rd.timestamp <= ?2" +
            " group by rd.id, rd.app, rd.uri, rd.ip, rd.timestamp" +
            " order by rd.timestamp DESC")
    List<ViewStatsDto> findAllByPeriod(String start, String end);

    @Query("select new ru.practicum.dto.ViewStatsDto (rd.app, rd.uri, COUNT(rd.ip) as hits)" +
            " from RequestData rd" +
            " where rd.timestamp >= ?1 and rd.timestamp <= ?2" +
            " group by rd.ip, rd.uri, rd.app" +
            " order by hits DESC")
    List<ViewStatsDto> findAllByPeriodIpIsUnique(String start, String end);

    @Query("select new ru.practicum.dto.ViewStatsDto (rd.app, rd.uri, COUNT(distinct rd.ip) as hits)" +
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
            " group by rd.ip, rd.uri, rd.app" +
            " order by hits DESC")
    List<ViewStatsDto> findAllByPeriodAndUris(String[] uris, String start, String end);


//    @Query("select new ru.practicum.dto.ViewStatsDto (rd.app, rd.uri, COUNT(rd.ip) as hits)" +
//            " from RequestData rd" +
//            " where rd.uri like %?1%" +
//            " and rd.timestamp >= ?2 and rd.timestamp <= ?3" +
//            " group by rd.ip, rd.uri, rd.app" +
//            " order by hits DESC")
//    List<ViewStatsDto> findAllByPeriodAndUris(String[] uris, String start, String end);

    // ------------ NEW METHODS -----------

    @Query("select new ru.practicum.dto.ViewStatsDto (rd.app, rd.uri, COUNT(rd.ip) as hits)" +
            " from RequestData rd" +
            " where rd.id = ?1")
    List<ViewStatsDto> findAllById(long id);




}
