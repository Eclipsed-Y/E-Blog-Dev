package cn.ecnu.eblog.user.controller;

import cn.ecnu.eblog.common.annotation.RunningTime;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.pojo.dto.PasswordDTO;
import cn.ecnu.eblog.common.pojo.dto.UserDTO;
import cn.ecnu.eblog.common.pojo.entity.user.UserDO;
import cn.ecnu.eblog.common.pojo.entity.user.UserInfoDO;
import cn.ecnu.eblog.common.pojo.result.Result;
import cn.ecnu.eblog.user.service.UserInfoService;
import cn.ecnu.eblog.user.service.UserService;
import cn.ecnu.eblog.user.utils.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;

@Slf4j
@RequestMapping("/user")
@RestController
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/signup")
    public Result<?> signup(@RequestBody UserDTO user){
        log.info("用户注册: {}", user.getUsername());
        userService.signup(user);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody UserDTO user) {
        log.info("用户登录: {}", user.getUsername());
        return Result.success(userService.login(user));
    }

    @PostMapping("/logout")
    public Result<?> logout(){
        log.info("id: {} 用户退出", BaseContext.getCurrentId());
        redisTemplate.delete("token:" + BaseContext.getCurrentId());
        return Result.success();
    }

    @PutMapping("/password")
    public Result<?> changePassword(@RequestBody PasswordDTO passwordDTO){
        log.info("id: {} 用户修改密码", BaseContext.getCurrentId());
        userService.changePassword(passwordDTO.getOldPassword(), passwordDTO.getNewPassword());
        return logout();
    }
}
