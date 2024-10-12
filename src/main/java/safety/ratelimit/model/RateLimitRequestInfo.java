package safety.ratelimit.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 限流请求信息
 *
 * @author wangwei
 * @date 2023-04-07 15:31
 */
@Accessors(chain = true)
@Data
public class RateLimitRequestInfo {

    /**
     * el表达式，如手机号码：“#user.phone”，如有多个参数，自行拼装
     */
    private String customExp;

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
    private String[] rule;


    private ProceedingJoinPoint joinPoint;
}
