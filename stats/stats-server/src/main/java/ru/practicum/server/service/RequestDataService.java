package ru.practicum.server.service;

import ru.practicum.dto.RequestDataDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.RequestData;

import java.util.List;

public interface RequestDataService {

    RequestData saveRequestData(RequestDataDto requestDataDto);

    List<ViewStatsDto> getAllRequestDataByPeriod(String start, String end, String[] uris, Boolean unique);

}
