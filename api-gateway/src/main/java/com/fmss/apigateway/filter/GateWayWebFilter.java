package com.fmss.apigateway.filter;

import brave.baggage.BaggageField;
import com.google.gson.Gson;
import com.turkcell.dcs.gncgateway.configuration.HeadersThreadLocal;
import com.turkcell.dcs.gncgateway.exception.UserException;
import com.turkcell.dcs.gncgateway.model.user.UserDto;
import com.turkcell.dcs.gncgateway.redis.usersession.RedisCacheService;
import com.turkcell.dcs.gncgateway.util.LogGwInformationUtil;
import com.turkcell.dcs.gncgateway.util.RequestIpPortUtil;
import com.turkcell.log.util.logService.LogGwService;
import com.turkcell.log.util.model.LogGwEvent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.turkcell.dcs.corelib.constants.HeaderConstants.*;
import static com.turkcell.dcs.gncgateway.constants.HeaderConstantGnc.EULA_ID;


@Component
@RequiredArgsConstructor
public class GateWayWebFilter implements WebFilter {

    private final RedisCacheService redisCacheService;
    private final LogGwService logGwService;
    private final Gson gson;

    @Value(value = "${system.app-route.name}")
    private String appRouteName;


    @Autowired
    private BaggageField appRouteBaggageField;

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        long startTime = Instant.now().toEpochMilli();


        ServerHttpRequest request = serverWebExchange.getRequest();
        HttpHeaders headers = request
                .getHeaders();

        RequestPath path = request.getPath();
        String uri = path.toString();

        if (isDocumentAPI(uri)) {
            return webFilterChain.filter(serverWebExchange);
        }

        List<String> headerUserList = headers.get(USER_TOKEN);
        String userToken = !CollectionUtils.isEmpty(headerUserList) ? headerUserList.get(0) : null;

        UserDto user = null;
        //api isimlendirmesinde guest varsa token alma
        if (tokenControlRequired(request, uri)) {
            //Client tarafinda kullandiklari bir kutuphane bu headerin kontrolunu yaptigindan bizden istekleri dogrultusunda ekledik.
            serverWebExchange.getResponse().getHeaders()
                    .add("WWW-Authenticate", "Bearer");

            user = getUser(userToken);
            if (user == null) {
                try {
                    throw new UserException("User token is not valid!");
                } catch (Exception ex) {
                    logGwEvent.setLogMessage("user is null, fastlogintoken={}");
                    logGwService.logInfo(logGwEvent, userToken);
                    serverWebExchange.getResponse()
                            .setStatusCode(HttpStatus.UNAUTHORIZED);

                    return Mono.error(ex);
                }

            }
            logGwEvent.setMsisdn(user.getMsisdn());
        }

        String ipAddress = RequestIpPortUtil.getIpAddress(request);
        String port = RequestIpPortUtil.getPort(request);
        setHeaders(headers, userToken, user, ipAddress, port);
        appRouteBaggageField.updateValue(appRouteName);

        return webFilterChain.filter(serverWebExchange)
                .doOnSuccess(aVoid ->
                        logGwService.logInfo(logGwEvent,
                                appRouteName,
                                (Instant.now().toEpochMilli() - startTime)))
                .doOnError(aVoid ->{
                    logGwEvent.setThrowable(aVoid);
                    logGwService.logError(logGwEvent,
                            appRouteName,
                            (Instant.now().toEpochMilli() - startTime));
                });
    }

    private static boolean isDocumentAPI(String uri) {
        return uri.contains("/actuator/") || uri.contains("/swagger-ui") || uri.contains("/v3/api-docs");
    }



    private String parseJwt(HttpServletRequest request) {
        final var headerAuth = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
            return headerAuth.substring(7);
        }
        return "";
    }


}