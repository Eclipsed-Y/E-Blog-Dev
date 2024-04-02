package cn.ecnu.eblog.user.aop;


import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.exception.AccessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Slf4j
@Component
public class CommonAspect {
    @Autowired
    private HttpServletRequest request;

    @Pointcut("execution(* cn.ecnu.eblog.user.controller.*.*(..))")
    private void execTime() {
    }
    @Pointcut("execution(* cn.ecnu.eblog.user.controller.*.*(..)) && @annotation(cn.ecnu.eblog.common.annotation.Inner)")
    private void execInner(){
    }

    @Around("execTime()")
    public Object runningTime(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("计算接口时间");
        long start = System.currentTimeMillis();
        Object res = null;
        try{
            res = joinPoint.proceed();
        } finally {
            log.info("接口执行用时: " + (System.currentTimeMillis() - start) + "ms");
        }
        return res;
    }
    @Before("execInner()")
    public void checkInner(JoinPoint joinPoint){
        log.info("试图内部访问");
        // 获取header中的from属性
        String from = request.getHeader("from");
        if (!"E".equals(from)){
            throw new AccessException(MessageConstant.ACCESS_DENIED);
        }
        log.info("内部请求通过");
    }

}