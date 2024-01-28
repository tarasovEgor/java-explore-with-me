package ru.practicum.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.practicum.dto.RequestDataDto;

import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.RequestData;
import ru.practicum.server.service.RequestDataService;

import java.util.List;

@RestController
public class RequestDataController {

    private final RequestDataService requestDataService;

    @Autowired
    public RequestDataController(RequestDataService requestDataService) {
        this.requestDataService = requestDataService;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestData saveRequestData(@RequestBody RequestDataDto requestDataDto) {
        return requestDataService.saveRequestData(requestDataDto);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(requestDataService.saveRequestData(requestDataDto));
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStatsDto> getAllRequestDataByPeriod(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) String[] uris,
            @RequestParam(required = false) Boolean unique) {
        return requestDataService.getAllRequestDataByPeriod(start, end, uris, unique);
        
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(requestDataService.getAllRequestDataByPeriod(start, end, uris, unique));
    }
}
