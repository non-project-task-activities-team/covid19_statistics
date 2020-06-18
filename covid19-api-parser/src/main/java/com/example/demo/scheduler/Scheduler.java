package com.example.demo.scheduler;

import com.example.demo.service.CovidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Configuration
@EnableScheduling
public class Scheduler {

    private CovidService covidService;

    @Autowired
    public Scheduler(CovidService covidService) {
        this.covidService = covidService;
    }

    @Scheduled(fixedRate = 5 * 1000)
    public void executeCurrentData() {

        covidService.getCurrentData();
    }

    @Scheduled(fixedRate = 60 * 1000)
    public void executeAllData() {

        covidService.getAllData();
    }
}