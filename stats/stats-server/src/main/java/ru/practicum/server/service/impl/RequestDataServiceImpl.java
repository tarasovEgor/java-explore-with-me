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

//                return ResponseEntity
//                        .status(HttpStatus.OK)
//                        .body(requestDataRepository
//                                .findAllById(1));

            }

        } else if (uris != null) {

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(requestDataRepository
                            .findAllByPeriodAndUris(uris, start, end)
                    );

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

        }

    }

//    @Override
//    public RequestDataDto saveRequestData(RequestData requestData) {
//        log.info("Saving the following data: {}", requestData);
//        return RequestDataMapper.toRequestDataDto(repository.save(requestData), repository);
//    }

    //  ----------- OLD IMPLEMENTATION --------------------- //
    /*@Override
    public List<ViewStatsDto> getAllRequestDataByPeriod(String start, String end, String[] uris, Boolean unique) {
        if ((uris == null || uris.length == 0) && (unique == null || !unique)) {
            return repository.findAllByPeriod(start, end);
        } else if (unique && (uris == null || uris.length == 0)) {
            return repository.findAllByPeriodIpIsUnique(start, end);
        } else {
            if (unique) {
                return repository.findAllByPeriodAndUrisAndIpIsUnique(uris, start, end);
            } else {
                return repository.findAllByPeriodAndUris(uris, start, end);
            }
        }
    }

    @Override
    public RequestData saveRequestData(RequestDataDto requestDataDto) {
        log.info("Saving the following data: {}", requestDataDto);
        RequestData requestData = RequestDataMapper.toRequestData(requestDataDto);
        return repository.save(requestData);
    }*/
    //  ----------- OLD IMPLEMENTATION --------------------- //











}
