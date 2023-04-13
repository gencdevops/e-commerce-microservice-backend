package com.fmss.apigateway.service;

import com.turkcell.dcs.gncgateway.servicecaller.usermanagament.AccountAgreementServiceCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountAgreementService {
    private final AccountAgreementServiceCaller accountAgreementServiceCaller;
    public Mono<Void> accountagreement() {
        return accountAgreementServiceCaller.accountAgreement();
    }
}
