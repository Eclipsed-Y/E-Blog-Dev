package cn.ecnu.eblog.user.service.impl;

import cn.ecnu.eblog.common.constant.CacheConstant;
import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.exception.AccessException;
import cn.ecnu.eblog.common.exception.AccountNotFoundException;
import cn.ecnu.eblog.common.exception.FeignBaseException;
import cn.ecnu.eblog.common.exception.RequestExcetption;
import cn.ecnu.eblog.common.feign.ArticleClient;
import cn.ecnu.eblog.common.feign.CommentClient;
import cn.ecnu.eblog.common.pojo.dto.UserInfoDTO;
import cn.ecnu.eblog.common.pojo.entity.user.UserInfoDO;
import cn.ecnu.eblog.common.pojo.vo.UserInfoVO;
import cn.ecnu.eblog.user.mapper.UserInfoMapper;

import cn.ecnu.eblog.user.service.UserInfoService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfoDO> implements UserInfoService {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private ArticleClient articleClient;
    @Autowired
    private CommentClient commentClient;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Override
    @Cacheable(value = CacheConstant.USER_INFO, cacheManager = CacheConstant.CACHE_MANAGER, key = "#id")
    public UserInfoVO getUserInfo(Long id) {
        if (id == null){
            throw new RequestExcetption(MessageConstant.ILLEGAL_REQUEST);
        }
        UserInfoDO userInfoDO = userInfoService.getOne(new QueryWrapper<UserInfoDO>().eq("user_id", id));
        if (userInfoDO == null || userInfoDO.getDeleted() == 1){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userInfoDO, userInfoVO);
        return userInfoVO;
    }
    @Override
    @CacheEvict(value = CacheConstant.USER_INFO, cacheManager = CacheConstant.CACHE_MANAGER, key = "#userInfoDTO.userId")
    public void updateInfo(UserInfoDTO userInfoDTO) {
        if (userInfoDTO.getUserId() == null || !BaseContext.getCurrentId().equals(userInfoDTO.getUserId())){
            throw new AccessException(MessageConstant.ILLEGAL_REQUEST);
        }
        UserInfoDO userInfoDO = new UserInfoDO();
        BeanUtils.copyProperties(userInfoDTO, userInfoDO);
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
        // 分布式事务更新
        TransactionStatus status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        try{
            userInfoService.update(wrapper);
            if (articleClient.updateUserInfo(userInfoDTO, BaseContext.getCurrentId()).getCode() == 0 || commentClient.updateUserInfo(userInfoDTO, BaseContext.getCurrentId()).getCode() == 0){
                throw new FeignBaseException(MessageConstant.INNER_ERROR);
            }
            platformTransactionManager.commit(status);
        } catch (Exception ex){
            log.info("分布式事务回滚");
            platformTransactionManager.rollback(status);
            throw ex;
        }

    }
}
