package safety.ratelimit.exception;

/**
 * @author wangwei
 * @date 2023-04-07 15:33
 */
public class RateLimitException extends RuntimeException {

    public RateLimitException(){
        super("服务访问受限");
    }

    public RateLimitException(String message) {
        super(message);
    }
}
