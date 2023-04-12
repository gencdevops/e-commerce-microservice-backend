package com.fmss.orderservice.feign;

import com.turkcell.dcs.platinumservice.configuration.FeignClientConfiguration;
import com.turkcell.dcs.platinumservice.dtos.catalog.GetDocumentByDocumentIdWithoutCacheResponseDto;
import com.turkcell.dcs.platinumservice.dtos.onemap.OneMapRequestDto;
import com.turkcell.dcs.platinumservice.dtos.penna.request.PennaRequestDto;
import com.turkcell.dcs.platinumservice.dtos.penna.response.PennaServiceResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigInteger;


@FeignClient(name = "dc-ws-service", url = "${dc.ws.service.url}",configuration = FeignClientConfiguration.class)
public interface BasketServiceFeignClient {

    @GetMapping(value = "/catalog-operations/document/{id}")
    //GetDocumentByDocumentIdWithoutCacheResponseDto getDocumentById(@PathVariable("id") BigInteger documentId);

    @PostMapping(value = "/one-map/query-result")
	//Boolean oneMapService(@RequestBody OneMapRequestDto oneMapRequestDto);

    @PostMapping(value = "/penna/penna-service")
	//PennaServiceResponseDto pennaService(@RequestBody PennaRequestDto pennaRequestDto);

}
