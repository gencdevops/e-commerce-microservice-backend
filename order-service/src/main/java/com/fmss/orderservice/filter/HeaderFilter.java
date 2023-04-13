package com.fmss.orderservice.filter;

import com.turkcell.dcs.corelib.constants.HeaderConstants;
import com.turkcell.dcs.corelib.enums.LanguageType;
import com.turkcell.dcs.corelib.model.UserContext;
import com.turkcell.dcs.platinumservice.constant.HeaderConstantPlatinum;
import com.turkcell.dcs.platinumservice.constant.PlatinumConstants;
import com.turkcell.dcs.platinumservice.utils.StringUtils;
import com.turkcell.log.util.logService.LogService;
import com.turkcell.log.util.model.LogEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;

import static com.turkcell.dcs.corelib.utils.StringUtil.textDecodeToUtf8;

@Component
public class HeaderFilter implements Filter{

    @Autowired
    private UserContext userContext;

    @Autowired
    LogService logService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String path = ((HttpServletRequest) request).getRequestURI();
        if (path.startsWith("/actuator") || path.contains("swagger-ui") || path.contains("/v3/api-docs") ||
                path.contains("favicon") || path.contains("/deep-link")) {
            chain.doFilter(request, response);
            return;
        }

        String withoutUserUrl="";
        if(httpRequest.getRequestURL()!=null){
            withoutUserUrl=httpRequest.getRequestURL().toString();
        }

        String msisdn = httpRequest.getHeader(HeaderConstants.USER_MSISDN_HEADER);
        String language = httpRequest.getHeader(HeaderConstants.USER_LANGUAGE_HEADER);
        String sessionId = httpRequest.getHeader(HeaderConstants.USER_SESSION_ID);
        String channelType = httpRequest.getHeader(HeaderConstants.USER_CHANNEL_TYPE);
        String clientVersion = httpRequest.getHeader(HeaderConstants.APP_VERSION);
        String name = httpRequest.getHeader(HeaderConstants.USER_NAME);
        String lastName = httpRequest.getHeader(HeaderConstants.USER_LAST_NAME);
        String email = httpRequest.getHeader(HeaderConstants.USER_EMAIL);
        String rankId = httpRequest.getHeader(HeaderConstants.USER_RANK_ID);
        Boolean isSol = Boolean.valueOf(httpRequest.getHeader(HeaderConstants.USER_SOL));
        Boolean isElite = Boolean.valueOf(httpRequest.getHeader(HeaderConstants.USER_ELITE));
        Boolean isTurkcell = Boolean.valueOf(httpRequest.getHeader(HeaderConstants.USER_TURKCELL));
        String clubId = httpRequest.getHeader(HeaderConstants.USER_CLUB_ID);
        String segment = httpRequest.getHeader(HeaderConstants.USER_SEGMENT);
        String paymentTpyeCode = httpRequest.getHeader(HeaderConstantPlatinum.USER_PAYMENT_TYPE_CODE);
        String clientIp = httpRequest.getHeader(HeaderConstants.USER_CLIENT_IP);
        String clientPort = httpRequest.getHeader(HeaderConstants.USER_CLIENT_PORT);
        try {
            if(!withoutUserUrl.contains(PlatinumConstants.APP_ON_BOARDING_API_PREFIX)&&(sessionId == null || language == null  || channelType == null)) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden because of headers");
                return;

            }

            userContext.setMsisdn(msisdn);
            userContext.setLanguageType(LanguageType.getTypeFromValue(language));
            userContext.setSessionId(sessionId);
            userContext.setChannelType(channelType);
            userContext.setClientVersion(clientVersion);
            userContext.setName(textDecodeToUtf8(name));
            userContext.setLastName(textDecodeToUtf8(lastName));
            userContext.setEmail(email);
            userContext.setSol(isSol);
            userContext.setTurkcell(isTurkcell);
            userContext.setElite(isElite);
            userContext.setClubId(StringUtils.isEmpty(clubId) ? null : BigInteger.valueOf(Long.valueOf(clubId)));
            userContext.setRankId(StringUtils.isEmpty(rankId) ? null : BigInteger.valueOf(Long.valueOf(rankId)));
            userContext.setSegmentType(segment);
            userContext.setPaymentType(StringUtils.isNotEmpty(paymentTpyeCode) ? Integer.parseInt(paymentTpyeCode) : null);
            userContext.setClientIp(clientIp);
            userContext.setClientPort(clientPort);

            chain.doFilter(request, response);
        }catch (Exception e){
            LogEvent logEventError = LogEvent.builder().logMessage("Platinum service header filter hata").build().setThrowable(e);
            logService.logError(logEventError);
        } finally {
            this.userContext.clear();
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
