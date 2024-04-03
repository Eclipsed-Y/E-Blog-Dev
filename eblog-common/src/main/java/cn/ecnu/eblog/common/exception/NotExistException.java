package cn.ecnu.eblog.common.exception;

/**
 * 账号被锁定异常
 */
public class NotExistException extends BaseException {

    public NotExistException() {
    }

    public NotExistException(String msg) {
        super(msg);
    }

}
