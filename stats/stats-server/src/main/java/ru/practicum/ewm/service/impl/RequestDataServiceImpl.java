package ru.practicum.ewm.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.practicum.ewm.dto.RequestDataDto;
import ru.practicum.ewm.mapper.RequestDataMapper;
import ru.practicum.ewm.model.RequestData;
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
    public List<RequestDataDto> getAllRequestDataByPeriod(String start, String end, String[] uris, Boolean unique) {
        if ((uris == null || uris.length == 0) && (unique == null || !unique)) {
            return RequestDataMapper.toRequestDataDto(repository.findAllRequestDataByPeriod(start, end), repository);
        } else if (!(unique == null || !unique) && (uris == null || uris.length == 0)) {
            return RequestDataMapper.toRequestDataDto(repository.findAllRequestDataByPeriodIpIsUnique(start, end), repository);
        } else if ((uris != null || uris.length != 0)) {
            if (!(unique == null || !unique)) {
                // return finAllRequestDataByPeriodAndUrisAndIpIsUnique
            } else {
                // return finAllRequestDataByPeriodAndUris
            }
        }
        return RequestDataMapper.toRequestDataDto(repository.findAllRequestDataByPeriod(start, end), repository);
    }


//    @Override
//    public RequestDataDto saveRequestData(RequestDataDto requestDataDto) {
//        RequestData requestData = RequestDataMapper.toRequestData(requestDataDto);
//        log.info("{}", requestData);
//        return RequestDataMapper.toRequestDataDto(repository.save(requestData));
//    }


}
