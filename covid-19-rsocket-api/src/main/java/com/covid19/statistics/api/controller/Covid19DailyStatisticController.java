package com.covid19.statistics.api.controller;

import com.covid19.statistics.api.dto.Covid19GeneralStatistic;
import com.covid19.statistics.api.dto.Covid19DailyStatisticByDatesRangeRequest;
import com.covid19.statistics.api.service.Covid19DailyStatisticService;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class Covid19DailyStatisticController {

  private final Covid19DailyStatisticService covid19DailyStatisticService;

  public Covid19DailyStatisticController(
      final Covid19DailyStatisticService covid19DailyStatisticService
  ) {
    this.covid19DailyStatisticService = covid19DailyStatisticService;
  }

  @MessageMapping("covid19.statistics.by.dates.range")
  public Flux<Covid19GeneralStatistic> streamCovid19DailyStatisticsByDatesRange(
      final Covid19DailyStatisticByDatesRangeRequest request
  ) {
    return
        covid19DailyStatisticService.getCovid19StatisticsByDatesRange(
            request.getStartDate(),
            request.getEndDate()
        );
  }

  private Flux<Covid19GeneralStatistic> generateCovid19StatisticsByDatesRange() {
    int amt = 1000;
    List<Covid19GeneralStatistic> stat = new ArrayList<>();
    for (int i = 0; i < amt; i++) {
      stat.add(
          Covid19GeneralStatistic.builder()
              .id(UUID.randomUUID())
              .build()
      );
    }
    return Flux.fromIterable(stat).delayElements(Duration.ofMillis(50));
  }
}
