package cn.ecnu.eblog.user.service;

import cn.ecnu.eblog.common.pojo.dto.UserDTO;
import cn.ecnu.eblog.common.pojo.entity.user.UserDO;
import cn.ecnu.eblog.common.pojo.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.security.auth.login.LoginException;

public interface UserService extends IService<UserDO> {
    UserVO login(UserDTO user) throws LoginException;
}
