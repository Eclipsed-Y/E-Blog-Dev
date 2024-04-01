package cn.ecnu.eblog.user.service.impl;

import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.exception.AccountNotFoundException;
import cn.ecnu.eblog.common.pojo.dto.UserInfoDTO;
import cn.ecnu.eblog.common.pojo.entity.user.UserInfoDO;
import cn.ecnu.eblog.common.pojo.vo.UserInfoVO;
import cn.ecnu.eblog.user.mapper.UserInfoMapper;

import cn.ecnu.eblog.user.service.UserInfoService;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfoDO> implements UserInfoService {
    @Override
    public UserInfoVO getUserInfo(Long id) {
        UserInfoDO userInfoDO = this.getById(id);
        if (userInfoDO == null || userInfoDO.getDeleted() == 1){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userInfoDO, userInfoVO);
        return userInfoVO;
    }

    @Override
    public void updateInfo(UserInfoDTO userInfoDTO) {
        UserInfoDO userInfoDO = new UserInfoDO();
        BeanUtils.copyProperties(userInfoDTO, userInfoDO);
        userInfoDO.setUserId(BaseContext.getCurrentId());
        UpdateWrapper<UserInfoDO> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id", userInfoDO.getUserId());
        if (userInfoDO.getNickname() != null) {
            wrapper.set("nickname", userInfoDO.getNickname());
        }
        if (userInfoDO.getProfile() != null) {
            wrapper.set("profile", userInfoDO.getProfile());
        }
        if (userInfoDO.getAvatar() != null) {
            wrapper.set("avatar", userInfoDO.getAvatar());
        }
        if (userInfoDO.getPosition() != null) {
            wrapper.set("position", userInfoDO.getPosition());
        }
        if (userInfoDO.getProfile() != null) {
            wrapper.set("profile", userInfoDO.getProfile());
        }
        this.update(wrapper);
    }
}
