package ru.practicum.ewm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.practicum.client.HttpClient;

@RestController
public class EwmController {

    @Autowired
    HttpClient httpClient;

    String[] args = {"arg1", "arg2", "arg3"};

    @GetMapping("/event")
    public void getEvent() {
        httpClient.getAllRequestDataByPeriod(
                "2020-05-05 00:00:00",
                "2023-05-05 00:00:00",
                args,
                true);
    }

}
