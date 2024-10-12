package safety.ratelimit;

/**
 * 接口限流类型
 *
 * @author wangwei
 * @date 2023-04-06 17:08
 */
public enum RateLimitType {
    // IP限流
    IP,

    USER,

    CUSTOM
}
