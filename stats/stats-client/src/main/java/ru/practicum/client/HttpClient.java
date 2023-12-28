package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.model.RequestData;


import java.util.Map;

@Service
public class HttpClient extends BaseClient {

    @Autowired
    public HttpClient(@Value("http://localhost:9090") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> saveRequestData(RequestData requestData) {
        return post("/hit", requestData);
    }

    public ResponseEntity<Object> getAllRequestDataByPeriod(String start, String end, String[] uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
    /*


    public ResponseEntity<Object> getAllBookingsByItemOwner(int from, int size, String status, long ownerId) {
        BookingValidation.isBookingStateValid(status);
        Map<String, Object> parameters = Map.of(
                "state", status,
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", ownerId, parameters);
    }

    public ResponseEntity<Object> getAllBookingsByBooker(int from, int size, String status, long bookerId) {
        BookingValidation.isBookingStateValid(status);
        Map<String, Object> parameters = Map.of(
                "state", status,
                "from", from,
                "size", size
        );
        return get("/?state={state}&from={from}&size={size}", bookerId, parameters);
    }

    public ResponseEntity<Object> saveBooking(long bookerId, BookingDto bookingDto) {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new InvalidBookingDateException("Invalid booking date.");
        }
        BookingValidation.isBookingDateValid(bookingDto.getStart(), bookingDto.getEnd());
        return post("", bookerId, bookingDto);
    }

*/
}
