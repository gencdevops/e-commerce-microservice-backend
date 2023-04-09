package com.fmss.userservice.configuration;

import org.springframework.security.core.session.SessionInformation;

import java.util.Date;
import java.util.function.Consumer;

/**
 * Do not put this class into hazelcast!
 *
 * @author Muhammed ALAGOZ
 */
public class RedisSessionInformation extends SessionInformation {
    private static final long serialVersionUID = 1L;

    private final SessionInformation delegate;
    private final Consumer<SessionInformation> modifiedCallback;

    public RedisSessionInformation(SessionInformation sessionInformation, Consumer<SessionInformation> modifiedCallback) {
        super(sessionInformation.getPrincipal(), sessionInformation.getSessionId(), sessionInformation.getLastRequest());
        this.delegate = sessionInformation;
        this.modifiedCallback = modifiedCallback;
    }

    @Override
    public void expireNow() {
        delegate.expireNow();
        modifiedCallback.accept(this.delegate);
    }

    @Override
    public Date getLastRequest() {
        return delegate.getLastRequest();
    }

    @Override
    public Object getPrincipal() {
        return delegate.getPrincipal();
    }

    @Override
    public String getSessionId() {
        return delegate.getSessionId();
    }

    @Override
    public boolean isExpired() {
        return delegate.isExpired();
    }

    @Override
    public void refreshLastRequest() {
        delegate.refreshLastRequest();
        modifiedCallback.accept(this.delegate);
    }
}
