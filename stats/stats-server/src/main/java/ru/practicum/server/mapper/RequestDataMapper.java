package ru.practicum.server.mapper;

import ru.practicum.dto.RequestDataDto;
import ru.practicum.server.model.RequestData;
import ru.practicum.server.repository.RequestDataRepository;

public class RequestDataMapper {

    public static RequestDataDto toRequestDataDto(RequestData requestData, RequestDataRepository repo) {
        return new RequestDataDto(
                requestData.getId(),
                requestData.getApp(),
                requestData.getUri(),
                requestData.getIp(),
                requestData.getTimestamp()
        );
    }

}
