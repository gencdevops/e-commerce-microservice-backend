package com.fmss.apigateway.service;

import com.turkcell.dcs.gncgateway.clientdtos.response.highlights.HighlightsResponse;
import com.turkcell.dcs.gncgateway.clientdtos.response.highlights.MarsServiceResponse;
import com.turkcell.dcs.gncgateway.servicecaller.gncservicecaller.HighlightServiceCaller;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class HighlightService {

	private final HighlightServiceCaller highlightsCaller;
	
	public Mono<ResponseEntity<HighlightsResponse>> getHighlightList(){
		return highlightsCaller.getHighlightList()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
	}

	public Mono<ResponseEntity<MarsServiceResponse>> highlightSelectNotify(String offerid){
		return highlightsCaller.highlightSelectNotify(offerid)
				.map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
}