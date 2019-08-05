package org.baeldung.config;

import com.netflix.zuul.context.ContextLifecycleFilter;
import com.netflix.zuul.http.ZuulServlet;
import java.util.Arrays;
import org.baeldung.web.StartServer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhaogy
 */
@Configuration
public class ZuulConfig {
    @Bean
    public ServletRegistrationBean zuulServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new ZuulServlet());
        registration.addUrlMappings("/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean<ContextLifecycleFilter> contextLifecycleFilterFilter(){
        FilterRegistrationBean  <ContextLifecycleFilter> registration = new FilterRegistrationBean <ContextLifecycleFilter>(new ContextLifecycleFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public ServletListenerRegistrationBean<StartServer> zuulListenerRegistration(){
        ServletListenerRegistrationBean<StartServer> registration = new ServletListenerRegistrationBean<StartServer>(new StartServer());
        return registration;
    }
}
