package ru.practicum.ewm.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.practicum.dto.RequestDataDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.RequestData;
import ru.practicum.ewm.mapper.RequestDataMapper;

import ru.practicum.ewm.repository.RequestDataRepository;
import ru.practicum.ewm.service.RequestDataService;

import java.util.List;

@Slf4j
@Service
public class RequestDataServiceImpl implements RequestDataService {

    private final RequestDataRepository repository;

    @Autowired
    public RequestDataServiceImpl(RequestDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public RequestDataDto saveRequestData(RequestData requestData) {
        log.info("Saving the following data: {}", requestData);
        return RequestDataMapper.toRequestDataDto(repository.save(requestData), repository);
    }

    @Override
    public List<ViewStatsDto> getAllRequestDataByPeriod(String start, String end, String[] uris, Boolean unique) {
        if ((uris == null || uris.length == 0) && (unique == null || !unique)) {
            return RequestDataMapper.toViewStatsDto(repository.findAllByPeriod(start, end), repository);
        } else if (unique && (uris == null || uris.length == 0)) {
            return repository.findAllByPeriodIpIsUnique(start, end);
        } else {
            if (unique) {
                return repository.findAllByPeriodAndUrisAndIpIsUnique(uris, start, end);
                // return finAllRequestDataByPeriodAndUrisAndIpIsUnique
            } else {
                return repository.findAllByPeriodAndUris(uris, start, end);
                // return finAllRequestDataByPeriodAndUris
            }
        }
    }


//    @Override
//    public RequestDataDto saveRequestData(RequestDataDto requestDataDto) {
//        RequestData requestData = RequestDataMapper.toRequestData(requestDataDto);
//        log.info("{}", requestData);
//        return RequestDataMapper.toRequestDataDto(repository.save(requestData));
//    }


}


// return RequestDataMapper.toRequestDataDto(repository.findAllRequestDataByPeriod(start, end), repository);
//  return RequestDataMapper.toViewStatsDto(repository.findAllRequestDataByPeriod(start, end), repository);
//  return RequestDataMapper.toViewStatsDto(repository.findAllByPeriod(start, end), repository);