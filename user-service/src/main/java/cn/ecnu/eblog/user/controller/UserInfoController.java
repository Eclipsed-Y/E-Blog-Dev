package cn.ecnu.eblog.user.controller;

import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.pojo.dto.UserInfoDTO;
import cn.ecnu.eblog.common.pojo.entity.BaseDO;
import cn.ecnu.eblog.common.pojo.entity.user.UserInfoDO;
import cn.ecnu.eblog.common.pojo.result.Result;
import cn.ecnu.eblog.common.pojo.vo.UserInfoVO;
import cn.ecnu.eblog.user.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 根据id获取用户详细信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<UserInfoVO> getUserInfo(@PathVariable Long id){
        UserInfoDO userInfoDO = userInfoService.getById(id);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userInfoDO, userInfoVO);
        return Result.success(userInfoVO);
    }

    /**
     * 修改个人信息
     * @param userInfoDTO
     * @return
     */
    @PutMapping("/update")
    public Result<?> updateInfo(@RequestBody UserInfoDTO userInfoDTO){
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
        userInfoService.update(wrapper);
        return Result.success();
    }
}