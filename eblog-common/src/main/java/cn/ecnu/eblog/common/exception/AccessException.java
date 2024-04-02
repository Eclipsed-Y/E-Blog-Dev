package cn.ecnu.eblog.common.exception;

/**
 * 账号被锁定异常
 */
public class AccessException extends BaseException {

    public AccessException() {
    }

    public AccessException(String msg) {
        super(msg);
    }

}
