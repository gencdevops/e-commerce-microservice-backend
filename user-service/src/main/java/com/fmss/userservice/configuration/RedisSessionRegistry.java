package com.fmss.userservice.configuration;

import com.fmss.userservice.util.CacheNames;
import com.google.common.collect.Sets;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public class RedisSessionRegistry implements SessionRegistry, ApplicationListener<SessionDestroyedEvent> {

    private final Map<String, String> usernameSessionIdMultiMap;
    private final Map<String, SessionInformation> sessionIdSessionInformationMap;

    public RedisSessionRegistry(CacheProperties redis) {
        this.usernameSessionIdMultiMap = hx.getMap(CacheNames.HZ_MULTIMAP_USERNAME_SESSION_ID);
        this.sessionIdSessionInformationMap = hz.getMap(CacheNames.HZ_MAP_SESSION_ID_SESSION_INFORMATION);

    }

    @Override
    public List<Object> getAllPrincipals() {
        return sessionIdSessionInformationMap.values().stream()
                .map(SessionInformation::getPrincipal)
                .collect(Collectors.toList());
    }

    @Override
    public List<SessionInformation> getAllSessions(Object principal, boolean includeExpiredSessions) {
        final String username = ((EcommerceUserDetailService) principal).getDelegate().getUserName();
        final Set<String> sessionsUsedByPrincipal = Sets.newHashSet(usernameSessionIdMultiMap.get(username));

        if (CollectionUtils.isEmpty(sessionsUsedByPrincipal)) {
            return Collections.emptyList();
        }

        final List<SessionInformation> list = new ArrayList<>(sessionsUsedByPrincipal.size());

        for (String sessionId : sessionsUsedByPrincipal) {
            final RedisSessionInformation sessionInformation = getSessionInformation(sessionId);

            if (sessionInformation == null) {
                continue;
            }

            if (includeExpiredSessions || !sessionInformation.isExpired()) {
                list.add(sessionInformation);
            }
        }

        return list;
    }

    @Override
    public RedisSessionInformation getSessionInformation(String sessionId) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        final SessionInformation sessionInformation = sessionIdSessionInformationMap.get(sessionId);
        if (sessionInformation == null) {
            return null;
        }
        return new RedisSessionInformation(sessionInformation, this::onSessionInformationModified);
    }

    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        String sessionId = event.getId();
        removeSessionInformation(sessionId);
    }

    @Override
    public void refreshLastRequest(String sessionId) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");

        final RedisSessionInformation info = getSessionInformation(sessionId);

        if (info != null) {
            info.refreshLastRequest();
        }
    }

    @Override
    public void registerNewSession(String sessionId, Object principal) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        Assert.notNull(principal, "Principal required as per interface contract");

        if (getSessionInformation(sessionId) != null) {
            removeSessionInformation(sessionId);
        }

        final String username = ((EcommerceUserDetailService) principal).getDelegate().getUserName();

        sessionIdSessionInformationMap.put(sessionId, new SessionInformation(principal, sessionId, new Date()));
        usernameSessionIdMultiMap.put(username, sessionId);

    }

    @Override
    public void removeSessionInformation(String sessionId) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        final RedisSessionInformation info = getSessionInformation(sessionId);
        if (info == null) {
            return;
        }

        sessionIdSessionInformationMap.remove(sessionId);

        final String username = ((EcommerceUserDetailService) info.getPrincipal()).getDelegate().getUserName();

        usernameSessionIdMultiMap.remove(username, sessionId);
    }

    private void onSessionInformationModified(SessionInformation sessionInformation) {
        sessionIdSessionInformationMap.put(sessionInformation.getSessionId(), sessionInformation);
    }
}
