package safety.ratelimit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import safety.ratelimit.RateLimitAspect;
import safety.ratelimit.exception.RateLimitExceptionHandler;

/**
 * @author wangwei
 * @date 2023-04-07 15:47
 */
@Configuration
@Import({RateLimitAspect.class, RateLimitExceptionHandler.class})
public class RateLimitAutoConfig {

}
