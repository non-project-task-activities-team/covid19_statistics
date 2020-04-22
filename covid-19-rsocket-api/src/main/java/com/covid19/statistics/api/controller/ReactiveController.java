package com.covid19.statistics.api.controller;

import com.covid19.statistics.api.dto.Dto;
import com.covid19.statistics.api.repository.DtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping(path = "/dto")
public class ReactiveController {

    private final DtoRepository repository;

    @Autowired
    public ReactiveController(final DtoRepository repository) {
        this.repository = repository;
    }

    //    http POST :8080/dto id="dcaefe81-198c-4c37-845f-091449534333" value="Roman"
    @PostMapping()
    @ResponseBody
    public Mono<Dto> addDto(@RequestBody final Dto dto) {
        return repository.save(dto);
    }

    //    http GET http://localhost:8080/dto
    @GetMapping()
    @ResponseBody
    public Flux<Dto> getAllDtos() {
        return repository.findAll();
    }

}
