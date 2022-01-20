package io.github.mjyoun.core.utils;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 시간 관련된 유틸
 * 
 * @author MJ Youn
 * @since 2021. 12. 23.
 */
public class TimeUtils {

    /** 기본 timer format */
    private static final String DEFAULT_TIMER_FORMAT = "hh:MM:ss.SSS uuuu nnnn";

    /**
     * 시간 포맷
     * 
     * @author MJ Youn
     * @since 2021. 12. 24.
     */
    @Getter
    @AllArgsConstructor
    public enum TIME_FORMAT {

        nano("nnn", TimeUnit.NANOSECONDS), //
        micro("uuu", TimeUnit.MICROSECONDS), //
        milli("SSS", TimeUnit.MILLISECONDS), //
        second("ss", TimeUnit.SECONDS), //
        minute("MM", TimeUnit.MINUTES), //
        hour("hh", TimeUnit.HOURS), //
        day("dd", TimeUnit.DAYS);

        /** 문자열에 포함되어 있는 포맷팅 정보 */
        private String format;
        /** 단위 */
        private TimeUnit unit;

    }

    /**
     * nano second를 formatting하여 출력하는 함수
     * 
     * @param nano
     *            nanosecond
     * @return formatted time string. format <code>hh:MM:ss.SSS uuu u nnn n</code>
     * 
     * @author MJ Youn
     * @since 2021. 12. 23.
     */
    public static String printPrettyNano(long nano) {
        return printPrettyNano(nano, TimeUnit.HOURS, TimeUtils.DEFAULT_TIMER_FORMAT);
    }

    /**
     * nano second를 formatting하여 출력하는 함수
     * 
     * @param nano
     *            nanosecond
     * @param maxUnit
     *            최고 표시 단위. deafult <code>TimeUnit.DAYS</code>
     * @param format
     *            formatting <br>
     *            <ul>
     *            <li><b>nnn</b>: nanosecond</li>
     *            <li><b>uuu</b>: microsecond</li>
     *            <li><b>SSS</b>: millisecond</li>
     *            <li><b>ss</b>: second</li>
     *            <li><b>MM</b>: minute</li>
     *            <li><b>hh</b>: hour</li>
     *            <li><b>dd</b>: day</li>
     *            </ul>
     * @return formatted time string
     * 
     * @author MJ Youn
     * @since 2021. 12. 23.
     */
    public static String printPrettyNano(long nano, TimeUnit maxUnit, String format) {
        if (nano < 0) {
            throw new IllegalArgumentException(MessageFormat.format("시간 값은 음수가 들어 올 수 없습니다. [args: {}]", nano));
        }

        // 기본 포맷 설정
        if (format == null || format.trim().equals("")) {
            format = TimeUtils.DEFAULT_TIMER_FORMAT;
        }

        // 기본 표시 unit 설정
        if (maxUnit == null) {
            maxUnit = TimeUnit.DAYS;
        }

        String formattedString = format;

        switch (maxUnit) {
        case DAYS:
            // day
            long day = convertTimeNano(nano, TimeUnit.DAYS);
            nano -= TimeUnit.DAYS.toNanos(day);
            formattedString = formattedString.replaceAll(TIME_FORMAT.day.format, String.format("%02d", day));
        case HOURS:
            // hour
            long hour = convertTimeNano(nano, TimeUnit.HOURS);
            nano -= TimeUnit.HOURS.toNanos(hour);
            formattedString = formattedString.replaceAll(TIME_FORMAT.hour.format, String.format("%02d", hour));
        case MINUTES:
            // minute
            long minute = convertTimeNano(nano, TimeUnit.MINUTES);
            nano -= TimeUnit.MINUTES.toNanos(minute);
            formattedString = formattedString.replaceAll(TIME_FORMAT.minute.format, String.format("%02d", minute));
        case SECONDS:
            // second
            long second = convertTimeNano(nano, TimeUnit.SECONDS);
            nano -= TimeUnit.SECONDS.toNanos(second);
            formattedString = formattedString.replaceAll(TIME_FORMAT.second.format, String.format("%02d", second));
        case MILLISECONDS:
            // milli
            long milli = convertTimeNano(nano, TimeUnit.MILLISECONDS);
            nano -= TimeUnit.MILLISECONDS.toNanos(milli);
            formattedString = formattedString.replaceAll(TIME_FORMAT.milli.format, String.format("%03d", milli));
        case MICROSECONDS:
            // micro
            long micro = convertTimeNano(nano, TimeUnit.MICROSECONDS);
            nano -= TimeUnit.MICROSECONDS.toNanos(micro);
            formattedString = formattedString.replaceAll(TIME_FORMAT.micro.format, String.format("%03d", micro));
        case NANOSECONDS:
            // nano
            formattedString = formattedString.replaceAll(TIME_FORMAT.nano.format, String.format("%03d", nano));
        }

        return formattedString;
    }

    /**
     * nano second 값을 다른 단위로 변경하는 함수
     * 
     * @param nano
     *            nano second 값. <code>nullable</code>
     * @param unit
     *            변경할 시간의 단위
     * @return 다른 단위로 변경된 시간 값
     * 
     * @author MJ Youn
     * @since 2021. 12. 24.
     */
    public static Long convertTimeNano(Long nano, TimeUnit unit) {
        return TimeUtils.convertTime(nano, TimeUnit.NANOSECONDS, unit);
    }

    /**
     * 시간 값을 다른 단위로 변경하는 함수
     * 
     * @param time
     *            시간. 최대 일부터 nanosecond 까지. <code>nullable</code>
     * @param from
     *            parameter로 받은 시간의 단위
     * @param to
     *            변경할 시간의 단위
     * @return 다른 단위로 변경된 시간 값
     */
    public static Long convertTime(Long time, @NotNull TimeUnit from, @NotNull TimeUnit to) {
        if (time == null) {
            return null;
        }

        switch (to) {
        case NANOSECONDS:
            // nano
            return from.toNanos(time);
        case MICROSECONDS:
            // nano / micro unit
            return from.toMicros(time);
        case MILLISECONDS:
            // nano / milli unit
            return from.toMillis(time);
        case SECONDS:
            // nano / second unit
            return from.toSeconds(time);
        case MINUTES:
            // nano / minute unit
            return from.toMinutes(time);
        case HOURS:
            // nano / hour unit
            return from.toHours(time);
        case DAYS:
            // nano / day unit
            return from.toDays(time);
        default:
            throw new UnsupportedOperationException(MessageFormat.format("현재 버전에서 지원하지 않는 타임 유닛입니다. [unit: {0}]", to));
        }
    }

}
