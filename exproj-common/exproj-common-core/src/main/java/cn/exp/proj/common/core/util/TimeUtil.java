package cn.exp.proj.common.core.util;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * All rights Reserved, Designed By www.hgplan.cn
 *
 * @author 41972
 * @version V1.0
 * Copyright 2023 www.hgplan.cn Inc. All rights reserved.
 * @date 2023/4/13 9:31
 **/
public class TimeUtil {
    private TimeUtil() {
    }

    public static final Long HOUR_MINUTES = 60L;

    public static final Long DAY_MINUTES = 24 * HOUR_MINUTES;

    public static final Long DAY_SECONDS = DAY_MINUTES * 60;

    public static final BigDecimal DAY_MINUTES_DECIMAL = BigDecimal.valueOf(DAY_MINUTES);

    public static final BigDecimal DAY_HOURS_DECIMAL = BigDecimal.valueOf(24);

    public static final Long SECOND_MILLS = 1000L;

    public static final String MONTH_FORMAT = "yyyy-MM";

    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern(MONTH_FORMAT);

    public static final String DATE_FORMAT = MONTH_FORMAT + "-" + "dd";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static final String TIME_FORMAT = "HH:mm:ss";

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static final String DATE_TIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

    public static final ZoneOffset DEAULT_ZONE_OFFSET = ZoneOffset.ofHours(8);

    /**
     * 最大至此的年份 - 避免在某些场景下进入死循环
     */
    public static final int MAX_YEAR = 2123;

    /**
     * 最大支持的日期 - 避免在某些场景下进入死循环
     */
    public static final LocalDate MAX_DATE = LocalDate.of(MAX_YEAR, 12, 31);

    /**
     * 最大支持的日期 - 避免在某些场景下进入死循环
     */
    public static final LocalDateTime MAX_DATE_TIME = LocalDateTime.of(MAX_DATE, LocalTime.MAX);

    /**
     * LocalDate To Date String
     *
     * @param localDateTime {@link LocalDateTime}
     * @return {@link String}
     */
    public static String toDateText(LocalDateTime localDateTime) {
        return Optional.ofNullable(localDateTime).map(LocalDateTime::toLocalDate).map(TimeUtil::toDateText).orElse(null);
    }

    /**
     * LocalDate To Date String
     *
     * @param localDate {@link LocalDate}
     * @return {@link String}
     */
    public static String toDateText(LocalDate localDate) {
        return Optional.ofNullable(localDate).map(LocalDate::toString).orElse(null);
    }

    /**
     * LocalDate To Date
     *
     * @param localDate {@link LocalDate}
     * @return {@link Date}
     */
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime To Date
     *
     * @param localDateTime {@link LocalDateTime}
     * @return {@link Date}
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date To LocalDate
     *
     * @param date {@link Date}
     * @return {@link LocalDate}
     */
    public static LocalDate toLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date To LocalDateTime
     *
     * @param date {@link Date}
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * LocalDateTime To LocalDate
     *
     * @param localDateTime {@link LocalDateTime}
     * @return {@link LocalDate}
     */
    public static LocalDate toLocalDate(LocalDateTime localDateTime) {
        return Optional.ofNullable(localDateTime).map(LocalDateTime::toLocalDate).orElse(null);
    }

    /**
     * mills To LocalDateTime
     *
     * @param mills {@link Long}
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime millsToDateTime(Long mills) {
        return toZoneDateTime(mills).toLocalDateTime();
    }

    /**
     * seconds To LocalDateTime
     *
     * @param seconds {@link Long}
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime toDateTime(Long seconds) {
        if (Objects.isNull(seconds)) {
            return null;
        }
        return millsToDateTime(seconds * SECOND_MILLS);
    }

    /**
     * mills To LocalDate
     *
     * @param mills {@link Long}
     * @return {@link LocalDate}
     */
    public static LocalDate toLocalDate(Long mills) {
        return toZoneDateTime(mills).toLocalDate();
    }

    /**
     * mills To LocalDateTime
     *
     * @param mills {@link Long}
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime toLocalDateTime(Long mills) {
        return toZoneDateTime(mills).toLocalDateTime();
    }

    /**
     * mills To ZonedDateTime
     *
     * @param mills {@link Long}
     * @return {@link ZonedDateTime}
     */
    public static ZonedDateTime toZoneDateTime(Long mills) {
        return Instant.ofEpochMilli(mills).atZone(ZoneId.systemDefault());
    }

    /**
     * To mills
     *
     * @param localDateTime {@link LocalDateTime}
     * @return {@link Long}
     */
    public static Long toEpochMilli(LocalDateTime localDateTime) {
        return toInstant(localDateTime).map(Instant::toEpochMilli).orElse(null);
    }

    /**
     * temporal To Instant
     *
     * @param localDateTime {@link TemporalAccessor}
     * @return {@link Optional<Instant>}
     */
    public static Optional<Instant> toInstant(LocalDateTime localDateTime) {
        return Optional.ofNullable(localDateTime).map(t -> t.toInstant(DEAULT_ZONE_OFFSET));
    }

    /**
     * 比较两个时间的Date
     *
     * @param time1 时间1
     * @param time2 时间2
     * @return time1.compareTo(time2)
     */
    public static int comareDate(LocalDateTime time1, LocalDateTime time2) {
        return Comparator.comparing(LocalDateTime::toLocalDate).compare(time1, time2);
    }

    public static String format(LocalDateTime startTime) {
        return Optional.ofNullable(startTime).map(t -> t.format(DATE_TIME_FORMATTER)).orElse(null);
    }

    public static Long toSecond(LocalDate localDate) {
        return toSecond(localDate.atTime(LocalTime.MIN));
    }

    public static Long toSecond(LocalDateTime localDateTime) {
        return Optional.ofNullable(localDateTime).map(time -> time.toEpochSecond(DEAULT_ZONE_OFFSET)).orElse(0L);
    }
}
