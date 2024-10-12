package safety.ratelimit.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author liao
 */
@UtilityClass
public class RequestUtil {

    public HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Objects.requireNonNull(requestAttributes).getRequest();
    }

    public String getRemoteAddress() {
        HttpServletRequest request = getRequest();
        // 和运维协商的header，nginx中会在其中存储真实来源ip
        String realIp = request.getHeader("X-Real-IP");
        // 本地调试不经过nginx，一般返回就是内网ip或本机ip
        return realIp == null ? request.getRemoteAddr() : realIp;
    }

    public String getRequestUri() {
        return getRequest().getRequestURI();
    }

    public String getHeader(String header) {
        return getRequest().getHeader(header);
    }

    /**
     * 请求信息，含uri和请求参数
     * 示例：GET:/api/getUser?id=1
     */
    public String getRequestInfo() {
        HttpServletRequest request = getRequest();
        String queryString = request.getQueryString();
        queryString = (StringUtils.isEmpty(queryString) || "null".equals(queryString)) ? "" : "?" + queryString;
        return request.getMethod() + ":" + request.getRequestURI() + queryString;
    }
}
