package safety.ratelimit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author wangwei
 * @date 2023-04-10 11:24
 */
@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class RateLimitExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(RateLimitException.class)
    public Object handleException(RateLimitException rateLimitException) {
        return null;
//        log.error("限流接口异常，uri：{}", RequestUtil.getRequestInfo(), rateLimitException);
//        return ResultWrapper.builder().code(String.valueOf(HttpStatus.BAD_REQUEST.value())).message(rateLimitException.getMessage()).build();

    }
}
