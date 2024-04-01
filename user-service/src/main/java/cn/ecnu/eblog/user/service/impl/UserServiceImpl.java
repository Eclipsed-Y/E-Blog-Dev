package cn.ecnu.eblog.user.service.impl;

import cn.ecnu.eblog.common.constant.JwtClaimsConstant;
import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.exception.LoginFailedException;
import cn.ecnu.eblog.common.exception.PasswordErrorException;
import cn.ecnu.eblog.common.pojo.dto.UserDTO;
import cn.ecnu.eblog.common.pojo.entity.user.UserDO;
import cn.ecnu.eblog.common.pojo.entity.user.UserInfoDO;
import cn.ecnu.eblog.common.pojo.vo.UserVO;
import cn.ecnu.eblog.common.utils.JwtUtil;
import cn.ecnu.eblog.user.mapper.UserMapper;
import cn.ecnu.eblog.user.properties.JwtProperties;
import cn.ecnu.eblog.user.service.UserInfoService;
import cn.ecnu.eblog.user.service.UserService;
import cn.ecnu.eblog.user.utils.PasswordUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordUtil passwordUtil;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private UserInfoService userInfoService;

    @Override
    public UserVO login(UserDTO user) {
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername());
        List<UserDO> userDOS = userMapper.selectList(wrapper);
        // 账号不存在
        if (userDOS.isEmpty() || userDOS.get(0).getDeleted() == 1) {
            throw new LoginFailedException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        UserDO userDO = userDOS.get(0);
        // 验证密码
        if (!passwordUtil.match(user.getPassword(), userDO.getPassword())) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, userDO.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);
        redisTemplate.opsForValue().set("token:" + userDO.getId(), token, jwtProperties.getUserTtl(), TimeUnit.MILLISECONDS);
        return UserVO.builder()
                .id(userDO.getId())
                .username(userDO.getUsername())
                .token(token)
                .build();
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        UserDO userDO = userMapper.selectById(BaseContext.getCurrentId());
        if (!passwordUtil.match(oldPassword, userDO.getPassword())){
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        userDO.setPassword(passwordUtil.encPwd(newPassword));
        userMapper.updateById(userDO);
    }

    @Override
    public void signup(UserDTO user) {
        UserDO userDO = new UserDO();
        UserInfoDO userInfoDO = new UserInfoDO();
        BeanUtils.copyProperties(user, userDO);
        BeanUtils.copyProperties(user, userInfoDO);
        userDO.setPassword(passwordUtil.encPwd(userDO.getPassword()));  // 加盐后md5加密

        transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                userMapper.insert(userDO);
                userInfoDO.setUserId(userDO.getId());
                userInfoService.save(userInfoDO);
                return null;
            }
        });
    }
}
