package com.fmss.userservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CacheNames {
    public static final String HZ_MULTIMAP_USERNAME_SESSION_ID = "ecom.username-session-id-multi-map";
    public static final String HZ_MAP_SESSION_ID_SESSION_INFORMATION = "ecom.session-id-session-information-map";
    public static final String HZ_MAP_SESSION_ADVISOR_ONLINE_STATUS = "ecom.session-id-session-advisor-online-status";
}
