package cn.exp.proj.common.core.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class RequestUtil {
    public static String getHeader(String header) {
        if (StringUtil.isBlank(header)) {
            return StringUtil.EMPTY;
        }
        return getRequest().map(req -> req.getHeader(header)).orElse(StringUtil.EMPTY);
    }

    public static Optional<HttpServletRequest> getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Optional.ofNullable(attributes).map(ServletRequestAttributes::getRequest);
    }
}
