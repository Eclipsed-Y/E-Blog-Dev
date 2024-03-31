package cn.ecnu.eblog.article.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class AutoFillAspect {

    @Pointcut("execution(* cn.ecnu.eblog.article.controller.*.*(..))")
    private void exec() {
    }
    @Around("exec()")
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
}