package cn.ecnu.eblog.common.exception;

/**
 * 账号被锁定异常
 */
public class FeignBaseException extends BaseException {

    public FeignBaseException() {
    }

    public FeignBaseException(String msg) {
        super(msg);
    }

}
