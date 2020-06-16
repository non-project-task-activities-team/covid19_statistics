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

    @Autowired
    CovidService covidService;

    private static final int MINUTE = 60 * 1000;

    @Scheduled(fixedRate = 5 * 1000)
    public void executeCurrentData() {

        covidService.getCurrentData();

        System.out.println(Thread.currentThread().getName() + " current covid data requested " + new Date());
    }

    @Scheduled(fixedRate = 60 * 1000)
    public void executeAllData() {

        covidService.getAllData();

        System.out.println(Thread.currentThread().getName() + " all covid data requested " + new Date());
    }
}