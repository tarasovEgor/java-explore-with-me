package ru.practicum.server.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ru.practicum.dto.RequestDataDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.exception.InvalidRequestDataDateException;
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
    public RequestData saveRequestData(RequestDataDto requestDataDto) {

        RequestData requestData = RequestDataMapper.toRequestData(requestDataDto);

        return requestDataRepository.save(requestData);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(requestDataRepository.save(requestData));

    }

    @Override
    public List<ViewStatsDto> getAllRequestDataByPeriod(String start, String end, String[] uris, Boolean unique) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime startLDT = LocalDateTime.parse(start, formatter);
        LocalDateTime endLDT = LocalDateTime.parse(end, formatter);

        if (startLDT.isAfter(endLDT)) {

            throw new InvalidRequestDataDateException("Start date can't come after the end date.");
//            return ResponseEntity
//                    .badRequest().build();

        }

        if (uris != null && unique != null) {

            if (unique) {

                return requestDataRepository
                                .findAllByPeriodAndUrisAndIpIsUnique(
                                        uris,
                                        start,
                                        end
                                );

//                return ResponseEntity
//                        .status(HttpStatus.OK)
//                        .body(requestDataRepository
//                                .findAllByPeriodAndUrisAndIpIsUnique(
//                                        uris,
//                                        start,
//                                        end
//                                )
//                        );

            } else {

                return requestDataRepository
                        .findAllByPeriodAndUris(
                                uris,
                                start,
                                end
                        );
//                return ResponseEntity
//                        .status(HttpStatus.OK)
//                        .body(requestDataRepository
//                                .findAllByPeriodAndUris(
//                                        uris,
//                                        start,
//                                        end
//                                )
//                        );

            }

        } else if (uris != null) {

            return requestDataRepository
                    .findAllByPeriodAndUrisAndIpIsUnique(
                            uris,
                            start,
                            end
                    );
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(requestDataRepository
//                            .findAllByPeriodAndUrisAndIpIsUnique(
//                                    uris,
//                                    start,
//                                    end
//                            )
//                    );

        } else if (unique != null) {

            if (unique) {

                return requestDataRepository
                        .findAllByPeriodIpIsUnique(
                                start, end
                        );
//                return ResponseEntity
//                        .status(HttpStatus.OK)
//                        .body(requestDataRepository
//                                .findAllByPeriodIpIsUnique(
//                                        start, end
//                                )
//                        );

            } else {

                return requestDataRepository
                        .findAllByPeriod(
                                start, end
                        );
//                return ResponseEntity
//                        .status(HttpStatus.OK)
//                        .body(requestDataRepository
//                                .findAllByPeriod(
//                                        start, end
//                                )
//                        );
            }

        } else {

            return requestDataRepository
                    .findAllByPeriod(
                            start, end
                    );
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(requestDataRepository
//                            .findAllByPeriod(
//                                    start, end
//                            )
//                    );

        }

    }

}
