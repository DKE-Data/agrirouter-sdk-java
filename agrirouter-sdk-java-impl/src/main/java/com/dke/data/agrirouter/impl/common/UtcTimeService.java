package com.dke.data.agrirouter.impl.common;

import com.dke.data.agrirouter.api.exception.CouldNotFindTimeZoneException;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Optional;
import java.util.TimeZone;

@SuppressWarnings("unused")
public class UtcTimeService {

    public static final long ONE_DAY_AGO = 60 * 60 * 24;
    public static final long TWO_DAYS_AGO = 60 * 60 * 24 * 2;
    public static final long THREE_DAYS_AGO = 60 * 60 * 24 * 3;
    public static final long FOUR_DAYS_AGO = 60 * 60 * 24 * 4;
    public static final long FIVE_DAYS_AGO = 60 * 60 * 24 * 5;
    public static final long SIX_DAYS_AGO = 60 * 60 * 24 * 6;

    public static final long ONE_WEEKS_AGO = 60 * 60 * 24 * 7;
    public static final long TWO_WEEKS_AGO = 60 * 60 * 24 * 14;
    public static final long THREE_WEEKS_AGO = 60 * 60 * 24 * 21;
    public static final long FOUR_WEEKS_AGO = 60 * 60 * 24 * 28;

    public static OffsetDateTime inThePast(long seconds) {
        return Instant.now().atOffset(ZoneOffset.UTC).minusSeconds(seconds);
    }

    public static OffsetDateTime inTheFuture(long seconds) {
        return Instant.now().atOffset(ZoneOffset.UTC).plusSeconds(seconds);
    }

    public static OffsetDateTime now() {
        return Instant.now().atOffset(ZoneOffset.UTC);
    }

    public static OffsetDateTime min() {
        return Instant.MIN.atOffset(ZoneOffset.UTC);
    }

    public static OffsetDateTime max() {
        return Instant.MAX.atOffset(ZoneOffset.UTC);
    }

    public static String offset() {
        var currentTimeZone = timeZone();
        if (currentTimeZone.isPresent()) {
            var zdt = Instant.now().atZone(ZoneId.of(currentTimeZone.get()));
            return zdt.getOffset().getId().replaceAll("Z", "+00:00");
        } else {
            throw new CouldNotFindTimeZoneException();
        }
    }

    private static Optional<String> timeZone() {
        var calendar = Calendar.getInstance();
        long zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
        var ids = TimeZone.getAvailableIDs();
        return Arrays.stream(ids)
                .filter(
                        (id) -> {
                            var tz = TimeZone.getTimeZone(id);
                            return tz.getRawOffset() == zoneOffset;
                        })
                .findFirst();
    }
}
