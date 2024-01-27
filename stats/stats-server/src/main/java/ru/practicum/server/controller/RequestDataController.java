package ru.practicum.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.practicum.dto.RequestDataDto;

import ru.practicum.server.service.RequestDataService;

@RestController
public class RequestDataController {

    private final RequestDataService requestDataService;

    @Autowired
    public RequestDataController(RequestDataService requestDataService) {
        this.requestDataService = requestDataService;
    }

    @PostMapping("/hit")
    public ResponseEntity<?> saveRequestData(@RequestBody RequestDataDto requestDataDto) {
       // return requestDataService.saveRequestData(requestDataDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(requestDataService.saveRequestData(requestDataDto));
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getAllRequestDataByPeriod(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) String[] uris,
            @RequestParam(required = false) Boolean unique) {
        //return requestDataService.getAllRequestDataByPeriod(start, end, uris, unique);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(requestDataService.getAllRequestDataByPeriod(start, end, uris, unique));
    }
}
