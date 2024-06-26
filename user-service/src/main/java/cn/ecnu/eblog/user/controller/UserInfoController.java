package cn.ecnu.eblog.user.controller;

import cn.ecnu.eblog.common.annotation.Inner;
import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.exception.AccountNotFoundException;
import cn.ecnu.eblog.common.pojo.dto.UserInfoDTO;
import cn.ecnu.eblog.common.pojo.entity.BaseDO;
import cn.ecnu.eblog.common.pojo.entity.user.UserDO;
import cn.ecnu.eblog.common.pojo.entity.user.UserInfoDO;
import cn.ecnu.eblog.common.pojo.result.Result;
import cn.ecnu.eblog.common.pojo.vo.UserInfoVO;
import cn.ecnu.eblog.user.service.UserInfoService;
import cn.ecnu.eblog.user.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("user")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserService userService;

    /**
     * 根据id获取用户详细信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<UserInfoVO> getUserInfo(@PathVariable Long id){
        log.info("获取id: {} 用户信息", id);
        UserInfoVO userInfoVO = userInfoService.getUserInfo(id);
        return Result.success(userInfoVO);
    }

    /**
     * 修改个人信息
     * @param userInfoDTO
     * @return
     */
    @PutMapping("/update")
    public Result<?> updateInfo(@RequestBody UserInfoDTO userInfoDTO){
        log.info("更新id: {} 用户信息", BaseContext.getCurrentId());
        userInfoService.updateInfo(userInfoDTO);
        return Result.success();
    }
    @GetMapping("/exists/{id}")
    @Inner
    public Result<Boolean> existsUser(@PathVariable Long id){
        log.info("id: {} 用户内部调用查询用户是否存在", BaseContext.getCurrentId());
        boolean exists = userService.exists(new QueryWrapper<UserDO>().eq("id", id).eq("deleted", 0));
        return Result.success(exists);
    }
}
