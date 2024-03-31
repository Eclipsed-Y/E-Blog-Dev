package cn.ecnu.eblog.article.config;

import cn.ecnu.eblog.article.interceptor.ArticleInterceptor;
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
    private ArticleInterceptor articleInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(articleInterceptor)
                .addPathPatterns("/article/**")
                .addPathPatterns("/category/**")
                .addPathPatterns("/tag/**");
    }

}
