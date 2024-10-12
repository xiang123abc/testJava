package safety.ratelimit;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import safety.ratelimit.exception.RateLimitException;
import safety.ratelimit.model.RateLimitRequestInfo;

/**
 * 接口限流AOP
 *
 * @author wangwei
 * @date 2023-04-06 18:18
 */
@Slf4j
@Aspect
public class RateLimitAspect {

    @Around("@annotation(rateLimitAnnotation)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimitAnnotation) throws Throwable {
        Object[] requestArgs = joinPoint.getArgs();
        RateLimitType rateLimitType = rateLimitAnnotation.type();
        RateLimitable rateLimitable = RateLimitFactory.buildRateLimit(rateLimitType);
        if (rateLimitable == null) {
            throw new RateLimitException("未找到限流器:" + rateLimitType);
        }
        RateLimitRequestInfo rateLimitInfo = new RateLimitRequestInfo()
                .setCustomExp(rateLimitAnnotation.customExp())
                .setRule(rateLimitAnnotation.rule())
                .setJoinPoint(joinPoint);
        boolean allowPass = rateLimitable.allowPass(rateLimitInfo);
        if (!allowPass) {
            throw new RateLimitException(rateLimitAnnotation.errorMsg());
        }
        return joinPoint.proceed(requestArgs);
    }
}
