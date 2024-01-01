package ru.practicum.ewm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.practicum.dto.RequestDataDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.RequestData;
import ru.practicum.ewm.service.RequestDataService;

import java.util.List;

@RestController
public class RequestDataController {

    private final RequestDataService requestDataService;

    @Autowired
    public RequestDataController(RequestDataService requestDataService) {
        this.requestDataService = requestDataService;
    }

    @PostMapping("/hit")
    public RequestDataDto saveRequestData(@RequestBody RequestData requestData) {

        return requestDataService.saveRequestData(requestData);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getAllRequestDataByPeriod(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) String[] uris,
            @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return requestDataService.getAllRequestDataByPeriod(start, end, uris, unique);
    }

}
