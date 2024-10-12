package safety.ratelimit.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;

/**
 *
 * @author wangwei
 * @date 2023-04-07 16:20
 */
@Data
@Accessors(chain = true)
public class RateLimitRule {

    /**
     * 限流规则
     */
    private String rule;

    /**
     * 限流数量
     */
    private Integer limit;

    /**
     * 时间窗口（单位秒）
     */
    private Long timeWindow;

    /**
     * 请求时间集合
     */
    private LinkedList requestTimeList;
}
