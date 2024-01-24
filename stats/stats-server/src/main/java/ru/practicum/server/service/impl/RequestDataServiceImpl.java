package ru.practicum.server.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ru.practicum.dto.RequestDataDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.RequestData;
import ru.practicum.server.mapper.RequestDataMapper;

import ru.practicum.server.repository.RequestDataRepository;
import ru.practicum.server.service.RequestDataService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class RequestDataServiceImpl implements RequestDataService {

    private final RequestDataRepository requestDataRepository;

    @Autowired
    public RequestDataServiceImpl(RequestDataRepository requestDataRepository) {
        this.requestDataRepository = requestDataRepository;
    }

    @Override
    public ResponseEntity<?> saveRequestData(RequestDataDto requestDataDto) {

        RequestData requestData = RequestDataMapper.toRequestData(requestDataDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(requestDataRepository.save(requestData));

    }

    @Override
    public ResponseEntity<?> getAllRequestDataByPeriod(String start, String end, String[] uris, Boolean unique) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime startLDT = LocalDateTime.parse(start, formatter);
        LocalDateTime endLDT = LocalDateTime.parse(end, formatter);

        List<ViewStatsDto> result;
        List<String> uriList;

        if (startLDT.isAfter(endLDT)) {

            return ResponseEntity
                    .badRequest().build();

        }

        if (uris != null && unique != null) {

            if (unique) {

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(requestDataRepository
                                .findAllByPeriodAndUrisAndIpIsUnique(uris, start, end)
                        );

            } else {

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(requestDataRepository
                                .findAllByPeriodAndUris(uris, start, end)
                        );

            }

        } else if (uris != null) {

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(requestDataRepository
                            .findAllByPeriodAndUrisAndIpIsUnique(uris, start, end)
                    );

        } else if (unique != null) {

            if (unique) {

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(requestDataRepository
                                .findAllByPeriodIpIsUnique(start, end)
                        );

            } else {

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(requestDataRepository
                                .findAllByPeriod(start, end)
                        );
            }

        } else {

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(requestDataRepository
                            .findAllByPeriod(start, end)
                    );

        }


        // ------
        /*if (uris != null) {

            if (unique) {

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(requestDataRepository
                                .findAllByPeriodAndUrisAndIpIsUnique(uris, start, end)
                        );

            } else {

                if (uris[0].equals("/events")) {

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(requestDataRepository
                                    .findAllByPeriodAndUris(uris[0], start, end)
                            );

                } else {

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(requestDataRepository
                                    .findAllByPeriodAndUris(uris, start, end)
                            );

                }



            }


        }*/




    }







    /*@Override
    public ResponseEntity<?> getAllRequestDataByPeriod(String start, String end, String[] uris, Boolean unique) {

        //        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        Event event = new Event();
//        event.setEventDate("2023-10-11 23:10:05");
//        LocalDateTime time = LocalDateTime.parse(event.getEventDate(), formatter);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime startLDT = LocalDateTime.parse(start, formatter);
        LocalDateTime endLDT = LocalDateTime.parse(end, formatter);

        List<ViewStatsDto> result;
        List<String> uriList;

        if (startLDT.isAfter(endLDT)) {

            return ResponseEntity
                    .badRequest().build();

        }

        if (uris != null) {

            uriList = List.of(uris);
            //return ResponseEntity.status(HttpStatus.OK).body("hello");

            if (unique) {

//                return ResponseEntity
//                        .status(HttpStatus.OK)
//                        .body(requestDataRepository
//                                .findAllByPeriodAndUrisAndIpIsUnique(uris, start, end)
//                        );

                List<ViewStatsDto> foundRequests = requestDataRepository.findByStartAndEndDateUnique(start, end);

//                result = foundRequests.stream()
////                        .filter(x -> uriList.contains(
////                                x.getUri()
////                        ))
//                        .distinct()
//                        .sorted(Comparator.comparing(ViewStatsDto::getHits).reversed())
//                        .collect(Collectors.toList());

                            *//*eventsByCat = result.getContent().stream()
                            .distinct()
                            //.filter(x -> x.getConfirmedRequests() < x.getParticipantLimit())
                            .filter(x -> categoryIds.contains(
                                            x.getCategory().getId()
                                    )
                            )
                            .sorted(Comparator.comparing(Event::getViews).reversed())
                            .collect(Collectors.toList());*//*

                result = new ArrayList<>();

                for (int i = 0; i < uris.length; i++) {
                    for (ViewStatsDto stat : foundRequests) {
                        if (stat.getUri().contains(uriList.get(i))) {
                            result.add(stat);
                        }
                    }
                }

                result = foundRequests.stream()
//                        .filter(x -> uriList.contains(
//                                x.getUri()
//                        ))
                      //  .distinct()
                        .sorted(Comparator.comparing(ViewStatsDto::getHits).reversed())
                        .collect(Collectors.toList());

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(result);

            } else {

//                return ResponseEntity
//                        .status(HttpStatus.OK)
//                        .body(requestDataRepository
//                                .findAllByPeriodAndUris(uris, start, end)
//                        );
                List<ViewStatsDto> foundRequests =
                        requestDataRepository.findAllByPeriodAndUris(uris[0], start, end);

//                result = new ArrayList<>();
//
//                for (int i = 0; i < uris.length; i++) {
//                    for (ViewStatsDto stat : foundRequests) {
//                        if (stat.getUri().contains(uriList.get(i))) {
//                            result.add(stat);
//                        }
//                    }
//                }
//
//                result = foundRequests.stream()
////                        .filter(x -> uriList.contains(
////                                x.getUri()
////                        ))
//                        //.distinct()
//                        .sorted(Comparator.comparing(ViewStatsDto::getHits).reversed())
//                        .collect(Collectors.toList());

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(foundRequests);
            }

        } else {

            if (unique) {

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(requestDataRepository
                                .findAllByPeriodIpIsUnique(start, end)
                        );
            } else {

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(requestDataRepository
                                .findAllByPeriod(start, end)
                        );

            }


//            List<ViewStatsDto> foundRequests =
//                    requestDataRepository.findByStartAndEndDate(start, end);
//
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(foundRequests);
        }

//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(requestDataRepository.findAll());
//                //.body(requestDataRepository.findAllViewStats(1));

//        if (uris != null && unique != null) {
//
//            if (unique) {
//
//                return ResponseEntity
//                        .status(HttpStatus.OK)
//                        .body(requestDataRepository
//                                .findAllByPeriodAndUrisAndIpIsUnique(uris, start, end)
//                        );
//
//            } else {
//
//                return ResponseEntity
//                        .status(HttpStatus.OK)
//                        .body(requestDataRepository
//                                .findAllByPeriodAndUris(uris, start, end)
//                        );
//
////                return ResponseEntity
////                        .status(HttpStatus.OK)
////                        .body(requestDataRepository
////                                .findAllById(1));
//
//            }
//
//        } else if (uris != null) {
//
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(requestDataRepository
//                            .findAllByPeriodAndUris(uris, start, end)
//                    );
//
//        } else {
//
//            if (unique) {
//
//                return ResponseEntity
//                        .status(HttpStatus.OK)
//                        .body(requestDataRepository
//                                .findAllByPeriodIpIsUnique(start, end)
//                        );
//
//            } else {
//
//                return ResponseEntity
//                        .status(HttpStatus.OK)
//                        .body(requestDataRepository
//                                .findAllByPeriod(start, end)
//                        );
//
//            }
//
//        }
//
    }*/

//    @Override
//    public RequestDataDto saveRequestData(RequestData requestData) {
//        log.info("Saving the following data: {}", requestData);
//        return RequestDataMapper.toRequestDataDto(repository.save(requestData), repository);
//    }

    //  ----------- OLD IMPLEMENTATION --------------------- //
//    @Override
//    public ResponseEntity<?> getAllRequestDataByPeriod(String start, String end, String[] uris, Boolean unique) {
//        if ((uris == null || uris.length == 0) && (unique == null || !unique)) {
//            return ResponseEntity.status(HttpStatus.OK).body(requestDataRepository.findAllByPeriod(start, end));
//        } else if (unique && (uris == null || uris.length == 0)) {
//            return ResponseEntity.status(HttpStatus.OK).body(requestDataRepository.findAllByPeriodIpIsUnique(start, end));
//        } else {
//            if (unique) {
//                return ResponseEntity.status(HttpStatus.OK).body(requestDataRepository.findAllByPeriodAndUrisAndIpIsUnique(uris, start, end));
//            } else {
//                return ResponseEntity.status(HttpStatus.OK).body(requestDataRepository.findAllByPeriodAndUris(uris, start, end));
//            }
//        }
//    }

    /*@Override
    public RequestData saveRequestData(RequestDataDto requestDataDto) {
        log.info("Saving the following data: {}", requestDataDto);
        RequestData requestData = RequestDataMapper.toRequestData(requestDataDto);
        return repository.save(requestData);
    }*/
    //  ----------- OLD IMPLEMENTATION --------------------- //











}
