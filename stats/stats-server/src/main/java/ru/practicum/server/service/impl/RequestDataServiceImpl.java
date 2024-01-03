package ru.practicum.server.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.practicum.dto.RequestDataDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.RequestData;
import ru.practicum.server.mapper.RequestDataMapper;

import ru.practicum.server.repository.RequestDataRepository;
import ru.practicum.server.service.RequestDataService;

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
            //return RequestDataMapper.toViewStatsDto(repository.findAllByPeriod(start, end), repository);
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

}
