package com.fmss.apigateway.servicecaller;


import java.util.Map;

@Service
public class DeepLinkServiceCaller extends BaseServiceCaller {
    private final WebClient serviceWebClient;

    public DeepLinkServiceCaller(@Qualifier("GncServiceWebClient") WebClient serviceWebClient) {
        this.serviceWebClient = serviceWebClient;
    }

    public Mono<DeepLinkResponse> getDeepLink(String webLink) {
        Map<String, String> headerGncService = setFieldHeaderGncService();

        return serviceWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(DEEP_LINK_CALLER)
                        .queryParam("webLink", webLink)
                        .build())
                .headers(httpHeaders -> httpHeaders.setAll(headerGncService))
                .retrieve()
                .bodyToMono(DeepLinkResponse.class)
                .publishOn(Schedulers.fromExecutor(wsProcessTaskExecutor));
    }
}
