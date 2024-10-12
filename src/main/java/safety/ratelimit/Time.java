package safety.ratelimit;

import lombok.experimental.UtilityClass;

/**
 * 常用时间段（对应秒数）
 *
 * @author luozhan
 * @date 2022-7-9
 */
@UtilityClass
public class Time {
    public final int SECOND = 1;

    public final int MINUTE = 60;
    public final int HOUR = 60 * MINUTE;
    public final int DAY = 24 * HOUR;
    public final int MONTH = 30 * DAY;
    public final int YEAR = 12 * MONTH;
    /**
     * 永不过期
     */
    public final int FOREVER = 0;


}