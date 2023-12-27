package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.RequestDataDto;
import ru.practicum.ewm.model.RequestData;
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

//    public static RequestDataDto toRequestDataDtoShortened(RequestData requestData) {
//        return new RequestDataDto(
//                requestData.getApp(),
//                requestData.getUri(),
//                requestData.get
//        )
//    }

    public static List<RequestDataDto> toRequestDataDto(List<RequestData> requestDataList, RequestDataRepository repo) {
        List<RequestDataDto> dtos = new ArrayList<>();
        for (RequestData rd : requestDataList) {
            dtos.add(toRequestDataDto(rd, repo));
        }
        return dtos;
    }


    /*public static List<ItemDto> toItemDto(List<Item> items) {
        List<ItemDto> dtos = new ArrayList<>();
        for (Item item : items) {
            dtos.add(toItemDto(item));
        }
        return dtos;
    }*/
}
