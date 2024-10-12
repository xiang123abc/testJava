package safety.ratelimit.impl;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import safety.ratelimit.util.RequestUtil;
import safety.ratelimit.exception.RateLimitException;
import safety.ratelimit.model.RateLimitRequestInfo;

/**
 * 用户限流
 *
 * @author wangwei
 * @date 2023-04-07 15:21
 */
@Slf4j
public class UserRateLimit extends AbstractRateLimit {

    private static final String AUTHORIZATION_HEADER = "authorization";

    @Override
    public String getKey(RateLimitRequestInfo rateLimitRequestInfo) {
        String authorization = RequestUtil.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isBlank(authorization)) {
            throw new RateLimitException("未登录用户不能使用用户限流");
        }
        return authorization;
    }
}
