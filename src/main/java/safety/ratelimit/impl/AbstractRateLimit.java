package safety.ratelimit.impl;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import safety.ratelimit.RateLimitable;
import safety.ratelimit.util.RequestUtil;
import safety.ratelimit.util.SpringContextUtil;
import safety.ratelimit.Time;
import safety.ratelimit.exception.RateLimitException;
import safety.ratelimit.model.RateLimitInfo;
import safety.ratelimit.model.RateLimitRequestInfo;
import safety.ratelimit.model.RateLimitRule;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author wangwei
 * @date 2023-04-07 15:53
 */
@Slf4j
public abstract class AbstractRateLimit implements RateLimitable {

    private static final String RATE_LIMIT_LOCK_KEY = "RATE_LIMIT:LOCK:";

    private static final String RATE_LIMIT_RULE_KEY = "RATE_LIMIT:RULE:";

    @Override
    public boolean allowPass(RateLimitRequestInfo rateLimitRequestInfo) {
        HttpServletRequest request = RequestUtil.getRequest();
        String requestUri = request.getMethod() + request.getRequestURI();
        log.info("新的限流请求:{}, customExp:{}. rule:{}", requestUri, rateLimitRequestInfo.getCustomExp(), rateLimitRequestInfo.getRule());
        String key = getKey(rateLimitRequestInfo);
        String md5 = DigestUtils.md5DigestAsHex((requestUri + key).getBytes());
        RedissonClient redissonClient = SpringContextUtil.getBean(RedissonClient.class);
        RLock lock = redissonClient.getLock(RATE_LIMIT_LOCK_KEY + md5);
        try {
            lock.lock(5, TimeUnit.SECONDS);
            //解析限流规则
            List<RateLimitRule> rateLimitRuleList = parseRule(rateLimitRequestInfo.getRule());

            RedisTemplate<String, Object> redisTemplate = SpringContextUtil.getBean("redisTemplate", RedisTemplate.class);
            RateLimitInfo rateLimitInfo = (RateLimitInfo) redisTemplate.opsForValue().get(RATE_LIMIT_RULE_KEY + md5);
            if (rateLimitInfo == null) {
                rateLimitInfo = new RateLimitInfo().setRuleMap(new HashMap<>());
            }
            //获取当前时间
            long currentTimeMillis = System.currentTimeMillis();

            rateLimitInfo.setLastRequestTime(currentTimeMillis);
            Map<String, RateLimitRule> ruleMap = rateLimitInfo.getRuleMap();
            for (RateLimitRule rateLimitRule : rateLimitRuleList) {
                RateLimitRule limitRule = ruleMap.computeIfAbsent(rateLimitRule.getRule(), l -> rateLimitRule);
                LinkedList<Long> requestTimeList = limitRule.getRequestTimeList();
                Integer limit = limitRule.getLimit();
                // 如果队列还没满，则允许通过，并添加当前时间戳到队列开始位置
                if (requestTimeList.size() < limit) {
                    requestTimeList.addFirst(currentTimeMillis);
                    continue;
                }
                // 队列已满（达到限制次数），则获取队列中最早添加的时间戳
                Long lastRequestTime = requestTimeList.getLast();
                // 用当前时间戳 减去 最早添加的时间戳
                if (currentTimeMillis - lastRequestTime <= limitRule.getTimeWindow() * 1000) {
                    log.warn("触发限流规则: {}", limitRule.getRule());
                    //若结果小于等于timeWindow，则说明在timeWindow内，通过的次数大于limit 不允许通过
                    return false;
                } else {
                    // 若结果大于timeWindow，则说明在timeWindow内，通过的次数小于等于limit
                    // 允许通过，并删除最早添加的时间戳，将当前时间添加到队列开始位置
                    requestTimeList.removeLast();
                    requestTimeList.addFirst(currentTimeMillis);
                }
            }
            //获取最大时间窗口
            Long maxTimeWindowTime = rateLimitRuleList.stream().max(Comparator.comparingLong(RateLimitRule::getTimeWindow)).get().getTimeWindow();
            redisTemplate.opsForValue().set(RATE_LIMIT_RULE_KEY + md5, rateLimitInfo, maxTimeWindowTime, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("allowPass error:", e);
            throw new RateLimitException("服务忙，请稍后重试");
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }

    /**
     * 解析限流规则
     *
     * @param rule
     * @return
     */
    private List<RateLimitRule> parseRule(String[] rule) {
        if (rule == null || rule.length == 0) {
            throw new RateLimitException("组件缺少必要参数");
        }
        return Arrays.stream(rule).map(item -> {
            String[] args = item.split("/");
            if (args.length != 2) {
                throw new RateLimitException("错误规则参数:" + item);
            }
            RateLimitRule rateLimitRule = new RateLimitRule();
            try {
                rateLimitRule.setRule(item);
                int limit = Integer.parseInt(args[0]);
                if (limit <= 0) {
                    throw new RateLimitException("错误的限流次数:" + limit);
                }
                rateLimitRule.setLimit(limit);
                String timeArg = args[1];
                Long time = Long.parseLong(timeArg.substring(0, timeArg.length() - 1));
                if (time <= 0) {
                    throw new RateLimitException("错误的限流时间:" + time);
                }
                String timeUnitArg = timeArg.substring(timeArg.length() - 1);
                Long limitSecondTime;
                switch (timeUnitArg) {
                    case "s":
                        limitSecondTime = time;
                        break;
                    case "m":
                        limitSecondTime = time * Time.MINUTE;
                        break;
                    case "h":
                        limitSecondTime = time * Time.HOUR;
                        break;
                    case "d":
                        limitSecondTime = time * Time.DAY;
                        break;
                    default:
                        throw new RateLimitException("错误的时间参数:" + timeUnitArg);
                }
                rateLimitRule.setTimeWindow(limitSecondTime);
                rateLimitRule.setRequestTimeList(new LinkedList<>());
            } catch (Exception e) {
                log.error("错误规则参数: {}", item, e);
                throw new RateLimitException("错误规则参数:" + item);
            }
            return rateLimitRule;
        }).collect(Collectors.toList());
    }
}
