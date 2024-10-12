package safety.ratelimit.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author wangwei
 * @date 2023-04-07 16:35
 */
@Data
@Accessors(chain = true)
public class RateLimitInfo {

    /**
     * 最后请求时间 （单位:毫秒）
     */
    private Long lastRequestTime;


    private Map<String, RateLimitRule> ruleMap;
}
