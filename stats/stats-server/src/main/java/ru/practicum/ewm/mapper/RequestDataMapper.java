package ru.practicum.ewm.mapper;

import ru.practicum.dto.RequestDataDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.RequestData;
import ru.practicum.ewm.repository.RequestDataRepository;

import java.util.ArrayList;
import java.util.List;

public class RequestDataMapper {

    public static RequestData toRequestData(RequestDataDto requestDataDto) {
        return new RequestData(
                requestDataDto.getApp(),
                requestDataDto.getUri(),
                requestDataDto.getIp(),
                requestDataDto.getTimestamp()
        );
    }

    public static RequestDataDto toRequestDataDto(RequestData requestData, RequestDataRepository repo) {
        return new RequestDataDto(
                requestData.getId(),
                requestData.getApp(),
                requestData.getUri(),
                requestData.getIp(),
                requestData.getTimestamp(),
                repo.findRequestDataHitCount(requestData.getIp())
        );
    }

    public static ViewStatsDto toViewStatsDto(RequestData requestData, RequestDataRepository repo) {
        return new ViewStatsDto(
                requestData.getApp(),
                requestData.getUri(),
                repo.findRequestDataHitCount(requestData.getIp())
        );
    }

    public static List<ViewStatsDto> toViewStatsDto(List<RequestData> requestDataList, RequestDataRepository repo) {
        List<ViewStatsDto> dtos = new ArrayList<>();
        for (RequestData rd : requestDataList) {
            dtos.add(toViewStatsDto(rd, repo));
        }
        return dtos;
    }

}
