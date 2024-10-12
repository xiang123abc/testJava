package safety.ratelimit.impl;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import safety.ratelimit.exception.RateLimitException;
import safety.ratelimit.model.RateLimitRequestInfo;

import java.lang.reflect.Method;

/**
 * 自定义参数限流
 *
 * @author wangwei
 * @date 2023-04-07 15:21
 */
@Slf4j
public class CustomRateLimit extends AbstractRateLimit {

    private static final SpelExpressionParser SPEL_PARSER = new SpelExpressionParser();

    private static final LocalVariableTableParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    @Override
    public String getKey(RateLimitRequestInfo rateLimitRequestInfo) {
        ProceedingJoinPoint joinPoint = rateLimitRequestInfo.getJoinPoint();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String classAndMethodName = getClassAndMethodName(joinPoint);
        // 解析springEl
        String[] parameterNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
        if (parameterNames == null || parameterNames.length == 0) {
            // 方法没有入参
            log.error("{}方法没有入参", classAndMethodName);
            throw new RateLimitException("限流方法没有入参");
        }
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        // 获取方法参数值
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            // 替换表达式里的变量值为实际值，p0,p1或者原参数名
            evaluationContext.setVariable(parameterNames[i], args[i]);
            evaluationContext.setVariable("p" + i, args[i]);
        }
        try {
            String value = SPEL_PARSER.parseExpression(rateLimitRequestInfo.getCustomExp()).getValue(evaluationContext, String.class);
            log.info("限流表达式{}, 解析结果:{}", rateLimitRequestInfo.getCustomExp(), value);
            if (StringUtils.isBlank(value)) {
                log.warn(classAndMethodName + "上的注解@RateLimit的customExp获取空属性，customExp参数将失效");
            }
            return value;
        } catch (RuntimeException e) {
            throw new EvaluationException(classAndMethodName + "上的注解@RateLimit的customExp属性指定有误，无法解析spEl表达式：" + classAndMethodName, e);
        }
    }

    private String getClassAndMethodName(ProceedingJoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        return "@" + joinPoint.getTarget().getClass().getName() + "." + method.getName();
    }

}
