package safety.ratelimit;


import safety.ratelimit.impl.CustomRateLimit;
import safety.ratelimit.impl.IpRateLimit;
import safety.ratelimit.impl.UserRateLimit;

/**
 * @author wangwei
 * @date 2023-04-07 15:47
 */
public class RateLimitFactory {

    private RateLimitFactory() {

    }

    public static RateLimitable buildRateLimit(RateLimitType type) {
        switch (type) {
            case IP:
                return new IpRateLimit();
            case USER:
                return new UserRateLimit();
            case CUSTOM:
                return new CustomRateLimit();
            default:
                return null;
        }
    }
}
