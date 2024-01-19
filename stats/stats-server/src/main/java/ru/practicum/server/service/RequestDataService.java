package ru.practicum.server.service;

import ru.practicum.dto.RequestDataDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.RequestData;

import java.util.List;

public interface RequestDataService {

   // RequestDataDto saveRequestData(RequestData requestDataDto);

    List<ViewStatsDto> getAllRequestDataByPeriod(String start, String end, String[] uris, Boolean unique);

    // ------------------   MODEL CHANGED TO DTO IN METHOD PARAMS

    RequestData saveRequestData(RequestDataDto requestDataDto);

}
