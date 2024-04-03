package cn.ecnu.eblog.common.exception;

/**
 * 账号被锁定异常
 */
public class RequestExcetption extends BaseException {

    public RequestExcetption() {
    }

    public RequestExcetption(String msg) {
        super(msg);
    }

}
