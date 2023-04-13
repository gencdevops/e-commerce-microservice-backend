package com.fmss.apigateway.service;

import com.turkcell.dcs.gncgateway.clientdtos.response.campaign.CampaignDto;
import com.turkcell.dcs.gncgateway.clientdtos.response.privilege.privilegelist.PrevilegeListResponse;
import com.turkcell.dcs.gncgateway.clientdtos.response.privilege.priviligedetail.PrivilegeResponseDetailDto;
import com.turkcell.dcs.gncgateway.servicecaller.gncservicecaller.PrivilegeServiceCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivilegeService {

    private final PrivilegeServiceCaller privilegeServiceCaller;
    private final UserService userService;

    public Mono<ResponseEntity<PrevilegeListResponse>> getPrivilegeList(String segmentType) {
        Flux<CampaignDto> privilegeListDtoFlux = privilegeServiceCaller.getPrivilegeListWithSearch(segmentType);
        Mono<PrevilegeListResponse> collect = privilegeListDtoFlux.collect(Collectors.toList())
                .flatMap(list -> Mono.just(new PrevilegeListResponse(list)));
        return collect
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    public Mono<ResponseEntity<PrivilegeResponseDetailDto>> getPrivilegeDetail(String platform, String appVersion, String urlPostfix, String urlVersion) {
        Mono<PrivilegeResponseDetailDto> privilegeListDtoFlux = privilegeServiceCaller.getPrivilegeListDetail(platform, appVersion, urlPostfix, urlVersion);

        return  privilegeListDtoFlux.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }
}