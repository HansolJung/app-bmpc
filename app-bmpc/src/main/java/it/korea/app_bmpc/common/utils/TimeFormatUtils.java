package it.korea.app_bmpc.common.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class TimeFormatUtils {

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String TIME_FORMAT = "HH:mm";

    /**
     * 현재 시간 가져오기
     * @return
     */
    public static String getNowTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATETIME_FORMAT));
    }

    /**
     * LocalDateTime -> String
     * @param localDateTime
     * @param pattern
     * @return
     */
    public static String getTime(LocalDateTime localDateTime, String pattern) {
        pattern = pattern == null ? DATETIME_FORMAT : pattern;
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * LocalDateTime -> String
     * @param localDateTime
     * @return
     */
    public static String getTime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(DATETIME_FORMAT));
    }

    /**
     * LocalTime -> String
     * @param localTime
     * @param pattern
     * @return
     */
    public static String getTime(LocalTime localTime, String pattern) {
        pattern = pattern == null ? TIME_FORMAT : pattern;
        return localTime.format(DateTimeFormatter.ofPattern(pattern));
    }
}
