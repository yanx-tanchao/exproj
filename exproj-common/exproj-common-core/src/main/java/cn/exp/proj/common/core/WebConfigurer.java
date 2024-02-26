package cn.exp.proj.common.core;

import cn.exp.proj.common.core.filter.ContextFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<ContextFilter> apsContextFilter() {
        FilterRegistrationBean<ContextFilter> registrationBean = new FilterRegistrationBean<ContextFilter>();
        registrationBean.setFilter(new ContextFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Integer.MAX_VALUE);
        return registrationBean;
    }
}
