package com.fmss.commondata.util;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@UtilityClass
public class DateUtil {

    public static final String REQUEST_DATE_FORMAT = "dd-MMMM HH:mm";
    public static final String REQUEST_HOUR_FORMAT = "HH:mm";
    public static final String NUMERIC_DATE_FORMAT = "yyyyMMddhhmmss";

    public static final String FULL_DATE_WITH_OUT_TIME = "yyyy-MM-dd";

    public static final String FULL_DATE_WITH_TIME = "yyyy-MM-dd HH:mm";

    public static ZonedDateTime toZonedDateTime(String stringDate) {
        final var zoneId = getZoneId();
        final var localDateTime = toLocalDateTime(stringDate);
        return localDateTime.atZone(zoneId);
    }

    public static String formatWithPattern(ZonedDateTime zonedDateTime, String pattern) {
        return zonedDateTime == null ? null : DateTimeFormatter.ofPattern(pattern).format(zonedDateTime);
    }

    public static ZoneId getZoneId() {
        return ZoneId.systemDefault();
    }

    public static LocalDateTime toLocalDateTime(String stringDate) {
        return LocalDateTime.parse(stringDate);
    }

    public static ZonedDateTime createDateTimeByInt(int year, int month, int day, int hour) {
        return ZonedDateTime.of(year, month, day, hour, 0, 0, 0, getZoneId());
    }

    public static String toStringDate(Date date, String format) {
        SimpleDateFormat originalFormat = new SimpleDateFormat(format);
        return originalFormat.format(date);
    }
}
