package ru.practicum.ewm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.practicum.dto.dto.RequestDataDto;
import ru.practicum.dto.model.RequestData;
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
    public List<RequestDataDto> getAllRequestDataByPeriod(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) String[] uris,
            @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return requestDataService.getAllRequestDataByPeriod(start, end, uris, unique);
    }




    /*@GetMapping("/owner")
    public List<BookingDto> getAllBookingsByItemOwner(@RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "5") int size,
                                                      @RequestParam(required = false) String state,
                                                      @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return bookingService.getAllBookingsByItemOwner(state, ownerId, from, size);
    }*/

//    @PostMapping("/hit")
//    public RequestDataDto saveRequestData(@RequestBody RequestDataDto requestDataDto) {
//        return requestDataService.saveRequestData(requestDataDto);
//    }


     /*private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto saveBooking(@RequestBody BookingDto bookingDto,
                               @RequestHeader("X-Sharer-User-Id") long bookerId) {
        return bookingService.saveBooking(bookingDto, bookerId);
    }*/
}
