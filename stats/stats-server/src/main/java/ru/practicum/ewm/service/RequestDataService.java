package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.RequestDataDto;
import ru.practicum.ewm.model.RequestData;

import java.util.List;
import java.util.Set;

public interface RequestDataService {

    //RequestDataDto saveRequestData(RequestDataDto requestDataDto);

    RequestDataDto saveRequestData(RequestData requestDataDto);

    List<RequestDataDto> getAllRequestDataByPeriod(String start, String end, String[] uris, Boolean unique);

}
