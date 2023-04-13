package com.fmss.apigateway.service;

import com.turkcell.dcs.gncgateway.clientdtos.response.homepage.HomePageDto;
import com.turkcell.dcs.gncgateway.servicecaller.gncservicecaller.HomePageServiceCaller;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@AllArgsConstructor
public class HomePageService {

    private final HomePageServiceCaller homePageServiceCaller;

    public Mono<ResponseEntity<HomePageDto>> getHomePage(String segmentType) {

        return homePageServiceCaller.getHomePage(segmentType)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }
}
