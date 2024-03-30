package cn.ecnu.eblog.article.interceptor;

import cn.ecnu.eblog.common.context.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class ArticleInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        try {
            String id = request.getHeader("userId");
            log.info("当前用户id:{}", id);
            BaseContext.setCurrentId(Long.valueOf(id));
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView){
        // 清除 BaseContext 中的数据
        BaseContext.removeCurrentId();
    }
}
