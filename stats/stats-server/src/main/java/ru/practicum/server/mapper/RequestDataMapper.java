package ru.practicum.server.mapper;

import ru.practicum.dto.RequestDataDto;
import ru.practicum.server.model.RequestData;

public class RequestDataMapper {

    public static RequestData toRequestData(RequestDataDto requestDataDto) {
        return new RequestData(
                requestDataDto.getApp(),
                requestDataDto.getUri(),
                requestDataDto.getIp(),
                requestDataDto.getTimestamp()
        );
    }

}
