package cn.ecnu.eblog.user.controller;

import cn.ecnu.eblog.common.annotation.RunningTime;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.pojo.dto.PasswordDTO;
import cn.ecnu.eblog.common.pojo.dto.UserDTO;
import cn.ecnu.eblog.common.pojo.entity.user.UserDO;
import cn.ecnu.eblog.common.pojo.result.Result;
import cn.ecnu.eblog.common.utils.AutoFillUtil;
import cn.ecnu.eblog.user.properties.JwtProperties;
import cn.ecnu.eblog.user.service.UserService;
import cn.ecnu.eblog.user.utils.PasswordUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;


@RequestMapping("/user")
@RestController
@Api(tags = "登录模块")
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordUtil passwordUtil;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private JwtProperties jwtProperties;
    @ApiOperation("注册接口")
    @PostMapping("/signup")
    @RunningTime
    public Result signup(@RequestBody UserDTO user){
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(user, userDO);
        AutoFillUtil.init(userDO);
        userDO.setPassword(passwordUtil.encPwd(userDO.getPassword()));  // 加盐后md5加密
        userService.save(userDO);
        return Result.success();
    }

    @ApiOperation("登录接口")
    @PostMapping("/login")
    @RunningTime
    public Result login(@RequestBody UserDTO user) throws LoginException {
        return Result.success(userService.login(user));
    }

    @ApiOperation("退出登录接口")
    @PostMapping("/logout")
    @RunningTime
    public Result logout(){
        redisTemplate.delete("token:" + BaseContext.getCurrentId());
        return Result.success();
    }

    @ApiOperation("修改密码")
    @PutMapping("/password")
    @RunningTime
    public Result changePassword(@RequestBody PasswordDTO passwordDTO){
        userService.changePassword(passwordDTO.getOldPassword(), passwordDTO.getNewPassword());
        return logout();
    }
}
