package com.fmss.logservice;

import com.fmss.logservice.model.LogModel;
import com.fmss.logservice.repository.LogModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/logs")
@RestController
@RequiredArgsConstructor
public class ControllerXX {

    private final LogModelRepository logModelRepository;

    @GetMapping
    public String hello() {

        return logModelRepository.save(new LogModel(null, "deneme") ).getMessage();
    }
}
