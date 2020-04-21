package com.covid19.statistics.api.controller;

import com.covid19.statistics.api.dto.Dto;
import java.time.Duration;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class ReactiveController {

    @MessageMapping("locate.radars.within")
    public Flux<Dto> get() {
        return get(10);
    }

    public Flux<Dto> get(Integer elements) {
        Random r = new Random();
        int low = 0;
        int high = 50;

        return
            Flux.fromStream(
                Stream.generate(() -> r.nextInt(high - low) + low)
                    .limit(elements)
                    .map(e ->
                        Dto.builder()
                            .id(UUID.randomUUID())
                            .value("Test value: " + e)
                            .build()
                    )
            ).delayElements(Duration.ofSeconds(1));
    }
}
