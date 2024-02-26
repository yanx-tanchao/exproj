package cn.exp.proj.common.core.filter;

import cn.exp.proj.common.core.context.ProjContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 设置当前登录信息上下文
 * 不会对未登录的接口调用进行特殊处理
 */
@Slf4j
public class ContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        try {
            ProjContext.reset();

            chain.doFilter(servletRequest, servletResponse);
        } finally {
            // 请求结束后清理上下文
            ProjContext.clear();
        }
    }
}
