package ru.practicum.server.service;

import org.springframework.http.ResponseEntity;

import ru.practicum.dto.RequestDataDto;

public interface RequestDataService {

    ResponseEntity<?> saveRequestData(RequestDataDto requestDataDto);

    ResponseEntity<?> getAllRequestDataByPeriod(String start, String end, String[] uris, Boolean unique);

}
