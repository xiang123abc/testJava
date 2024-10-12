package safety.ratelimit;


import safety.ratelimit.model.RateLimitRequestInfo;

/**
 *
 * @author wangwei
 * @date 2023-04-06 18:33
 */
public interface RateLimitable {

    /**
     * 获取key
     * @return
     */
    String getKey(RateLimitRequestInfo rateLimitRequestInfo);

    /**
     * 是否允许通过
     *
     * @param rateLimitRequestInfo
     * @return
     */
    boolean allowPass(RateLimitRequestInfo rateLimitRequestInfo);
}
