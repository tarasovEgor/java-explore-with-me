package ru.practicum.ewm.service;

import ru.practicum.dto.RequestDataDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.RequestData;

import java.util.List;

public interface RequestDataService {

    RequestDataDto saveRequestData(RequestData requestDataDto);

    List<ViewStatsDto> getAllRequestDataByPeriod(String start, String end, String[] uris, Boolean unique);

}
