package ru.practicum.server.service;

import org.springframework.http.ResponseEntity;

import ru.practicum.dto.RequestDataDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.RequestData;

import java.util.List;

public interface RequestDataService {

    //ResponseEntity<?> saveRequestData(RequestDataDto requestDataDto);

    RequestData saveRequestData(RequestDataDto requestDataDto);

    //ResponseEntity<?> getAllRequestDataByPeriod(String start, String end, String[] uris, Boolean unique);

    List<ViewStatsDto> getAllRequestDataByPeriod(String start, String end, String[] uris, Boolean unique);

}
