package safety.ratelimit.impl;

import lombok.extern.slf4j.Slf4j;
import safety.ratelimit.util.RequestUtil;
import safety.ratelimit.model.RateLimitRequestInfo;

/**
 * IP限流
 *
 * @author wangwei
 * @date 2023-04-07 15:21
 */
@Slf4j
public class IpRateLimit extends AbstractRateLimit {


    @Override
    public String getKey(RateLimitRequestInfo rateLimitRequestInfo) {
        String remoteAddress = RequestUtil.getRemoteAddress();
        log.info("使用IP限流,IP: {}", remoteAddress);
        return remoteAddress;
    }
}
