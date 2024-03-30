package cn.ecnu.eblog.user.service;

import cn.ecnu.eblog.common.pojo.dto.UserDTO;
import cn.ecnu.eblog.common.pojo.entity.user.UserInfoDO;
import cn.ecnu.eblog.common.pojo.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.security.auth.login.LoginException;

public interface UserInfoService extends IService<UserInfoDO> {
}
