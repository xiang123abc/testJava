package safety.ratelimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口限流
 *
 * 支持IP、用户、自定义参数限流
 *
 *
 * @author wangwei
 * @date 2023-04-06
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 限流类型 (必填)
     *
     */
    RateLimitType type();

    /**
     * 限流目标参数  (type = RateLimitType 必填)
     *
     * el表达式，如手机号码：“#user.phone”，如有多个参数，自行拼装
     */
    String customExp() default "";

    /**
     * 限流规则 (必填)
     *
     * 格式为: {"1/10s", "3/1m", "20/1h"}
     * 限流规则由1个或多个规则组成， "1/10s" ,  斜杠前面的数字1表示指定时间内能请求的次数，10s表示10秒钟， "1/10s" 表示10秒周期内只能请求1次
     *
     * 配置多个规则时，只要其中一个规则到达限定值，就会返回错误
     *
     * 时间目前支持： s : 秒, m: 分钟, h: 小时, d: 天
     *
     */
    String[] rule();

    String errorMsg() default "请求过于频繁，请稍后再试";
}
