package cn.ecnu.eblog.user.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class PasswordUtil {
    /**
     * 密码加盐
     */

    @Value("akiey")
    private String salt;
    @Value("3")
    private Integer saltIdx;

    public boolean match(String plainPwd, String encPwd) {
        return Objects.equals(encPwd(plainPwd), encPwd);
    }

    /**
     * 明文密码处理
     *
     * @param plainPwd
     * @return
     */
    public String encPwd(String plainPwd) {
        // 如果密码长度大于saltIdx，则在idx处加盐
        if (plainPwd.length() > saltIdx) {
            plainPwd = plainPwd.substring(0, saltIdx) + salt + plainPwd.substring(saltIdx);
        }
        // 否则后面直接加盐
        else {
            plainPwd = plainPwd + salt;
        }
        return DigestUtils.md5DigestAsHex(plainPwd.getBytes(StandardCharsets.UTF_8));
    }
}
