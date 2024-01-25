package ru.practicum.server.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ru.practicum.dto.RequestDataDto;
import ru.practicum.server.model.RequestData;
import ru.practicum.server.mapper.RequestDataMapper;

import ru.practicum.server.repository.RequestDataRepository;
import ru.practicum.server.service.RequestDataService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

        if (startLDT.isAfter(endLDT)) {

            return ResponseEntity
                    .badRequest().build();

        }

        if (uris != null && unique != null) {

            if (unique) {

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(requestDataRepository
                                .findAllByPeriodAndUrisAndIpIsUnique(
                                        uris,
                                        start,
                                        end
                                )
                        );

            } else {

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(requestDataRepository
                                .findAllByPeriodAndUris(
                                        uris,
                                        start,
                                        end
                                )
                        );

            }

        } else if (uris != null) {

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(requestDataRepository
                            .findAllByPeriodAndUrisAndIpIsUnique(
                                    uris,
                                    start,
                                    end
                            )
                    );

        } else if (unique != null) {

            if (unique) {

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(requestDataRepository
                                .findAllByPeriodIpIsUnique(
                                        start, end
                                )
                        );

            } else {

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(requestDataRepository
                                .findAllByPeriod(
                                        start, end
                                )
                        );
            }

        } else {

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(requestDataRepository
                            .findAllByPeriod(
                                    start, end
                            )
                    );

        }

    }

}
