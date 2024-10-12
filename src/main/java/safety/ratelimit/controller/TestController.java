package safety.ratelimit.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import safety.ratelimit.RateLimit;
import safety.ratelimit.RateLimitType;


/**
 * @author zhou miao
 * @date 2022/05/31
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("test")
    @RateLimit(type = RateLimitType.IP, rule = {"3/1s","1/1m"})
    public void test(Long end) {
        System.out.print("");
    }

    @GetMapping("test2")
    @RateLimit(type = RateLimitType.USER, rule = {"3/1s","1/1m"})
    public void test2(Long end) {
        System.out.print("");
    }

    @GetMapping("test3")
    @RateLimit(type = RateLimitType.CUSTOM, rule = {"3/1s","1/1m"},customExp = "#name")
    public void test3(String name) {
        System.out.print("");
    }
}
