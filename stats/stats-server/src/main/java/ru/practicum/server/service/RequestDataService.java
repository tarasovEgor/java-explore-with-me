package ru.practicum.server.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.dto.RequestDataDto;

public interface RequestDataService {

    //   ---------------  OLD METHODS ----------------- //
   // RequestDataDto saveRequestData(RequestData requestDataDto);

    // ---> List<ViewStatsDto> getAllRequestDataByPeriod(String start, String end, String[] uris, Boolean unique);


    // ---> RequestData saveRequestData(RequestDataDto requestDataDto);

    //   ---------------  OLD METHODS ----------------- //






    // -----------------    REFACTORED METHODS    ------------------ //

    ResponseEntity<?> saveRequestData(RequestDataDto requestDataDto);

    ResponseEntity<?> getAllRequestDataByPeriod(String start, String end, String[] uris, Boolean unique);

}
