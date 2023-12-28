package ru.practicum.ewm.service;

import ru.practicum.dto.dto.RequestDataDto;
import ru.practicum.dto.model.RequestData;

import java.util.List;

public interface RequestDataService {

    //RequestDataDto saveRequestData(RequestDataDto requestDataDto);

    RequestDataDto saveRequestData(RequestData requestDataDto);

    List<RequestDataDto> getAllRequestDataByPeriod(String start, String end, String[] uris, Boolean unique);

}
