package cn.ecnu.eblog.article.handler;

import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.exception.BaseException;
import cn.ecnu.eblog.common.pojo.result.Result;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        BaseContext.removeCurrentId();
        return Result.error(ex.getMessage());
    }

    /**
     * 捕获Feign调用异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> exceptionHandler(FeignException ex){
        log.error("异常信息：{}", ex.getMessage());
        BaseContext.removeCurrentId();
        return Result.error(ex.getMessage());
    }


    /**
     *  捕获数据库异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        String message = ex.getMessage();
        BaseContext.removeCurrentId();
        if (message.contains("Duplicate entry")){
            String username = message.split("'")[1];
            String msg = username + MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        }
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

}
