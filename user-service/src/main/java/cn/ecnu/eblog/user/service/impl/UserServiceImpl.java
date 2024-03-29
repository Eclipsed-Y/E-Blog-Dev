package cn.ecnu.eblog.user.service.impl;

import cn.ecnu.eblog.common.constant.JwtClaimsConstant;
import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.pojo.dto.UserDTO;
import cn.ecnu.eblog.common.pojo.entity.user.UserDO;
import cn.ecnu.eblog.common.pojo.vo.UserVO;
import cn.ecnu.eblog.user.properties.JwtProperties;
import cn.ecnu.eblog.common.utils.JwtUtil;
import cn.ecnu.eblog.user.mapper.UserMapper;
import cn.ecnu.eblog.user.service.UserService;
import cn.ecnu.eblog.user.utils.PasswordUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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

    @Override
    public UserVO login(UserDTO user) throws LoginException {
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername());
        List<UserDO> userDOS = userMapper.selectList(wrapper);
        // 账号不存在
        if (userDOS.isEmpty()) {
            throw new LoginException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        UserDO userDO = userDOS.get(0);
        // 验证密码
        if (!passwordUtil.match(user.getPassword(), userDO.getPassword())) {
            throw new LoginException(MessageConstant.LOGIN_FAILED);
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
}
