package cn.ecnu.eblog.activity.config;

import cn.ecnu.eblog.activity.interceptor.ActivityInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private ActivityInterceptor activityInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(activityInterceptor)
                .addPathPatterns("/activity/**");
    }

}
