package cn.ecnu.eblog.user.aop;


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

    @Pointcut("execution(* cn.ecnu.eblog.user.controller.*.*(..))")
    private void exec() {
    }

//    @Before("exec()")
//    public void autoFill(JoinPoint joinPoint) {
//        log.info("自动填充");
//
//        // 获取当前方法注解中的枚举类
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        RunningTime anno = signature.getMethod().getAnnotation(RunningTime.class);
//        OperationType type = anno.value();
//
//
//        // 获取当前方法的参数
//        Object entity = joinPoint.getArgs()[0];
//
//        // 填充公共属性
//        LocalDateTime now = LocalDateTime.now();
//
//        try {
//            entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(entity, now);
//
//            if (type == OperationType.INSERT) {
//                entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class).invoke(entity, now);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
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