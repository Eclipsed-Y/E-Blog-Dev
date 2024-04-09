package cn.ecnu.eblog.activity.service.impl;

import cn.ecnu.eblog.activity.mapper.AttentionMapper;
import cn.ecnu.eblog.activity.service.AttentionService;
import cn.ecnu.eblog.common.constant.CacheConstant;
import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.exception.RequestExcetption;
import cn.ecnu.eblog.common.feign.UserClient;
import cn.ecnu.eblog.common.pojo.dto.AttentionDTO;
import cn.ecnu.eblog.common.pojo.entity.activity.AttentionDO;
import cn.ecnu.eblog.common.pojo.entity.activity.LikeDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.aspectj.bridge.Message;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AttentionServiceImpl extends MPJBaseServiceImpl<AttentionMapper, AttentionDO> implements AttentionService {
    @Autowired
    private AttentionService attentionService;
    @Autowired
    private UserClient userClient;
    @Override
    public void attentionByUserId(AttentionDTO attentionDTO) {
        // 判断合法
        if (attentionDTO.getUserId() == null || attentionDTO.getUserId().equals(attentionDTO.getFollowerId())){
            throw new RequestExcetption(MessageConstant.ILLEGAL_REQUEST);
        }
        AttentionDO attentionDO = new AttentionDO();
        BeanUtils.copyProperties(attentionDTO, attentionDO);
        if (attentionService.exists(new QueryWrapper<AttentionDO>().eq("user_id", attentionDO.getUserId()).eq("follower_id", attentionDO.getFollowerId()))){
            // 已存在
            attentionService.update(new UpdateWrapper<AttentionDO>().eq("user_id", attentionDO.getUserId()).eq("follower_id", attentionDO.getFollowerId()).setSql("status = -status"));
        } else {
            // 尝试插入
            try{
                attentionService.save(attentionDO);
            } catch (Exception e){
                // 插入失败，说明其他线程已经插入
                attentionService.update(new UpdateWrapper<AttentionDO>().eq("user_id", attentionDO.getUserId()).eq("follower_id", attentionDO.getFollowerId()).setSql("status = -status"));
            }
        }
    }

    @Override
    @Cacheable(value = CacheConstant.ATTENTION_COUNT, cacheManager = "redisCacheManagerForNums", key = "#userId")
    public Integer getAttentionCount(Long userId) {
        // 判断是否合法
        if (!userClient.existsUser(userId, BaseContext.getCurrentId()).getData()){
            throw new RequestExcetption(MessageConstant.ILLEGAL_REQUEST);
        }
        return Math.toIntExact(attentionService.count(new QueryWrapper<AttentionDO>().eq("user_id", userId)));
    }

    @Override
    @Cacheable(value = CacheConstant.HAS_ATTENTION, cacheManager = "redisCacheManagerForNums", key = "#attentionDTO.userId + '_' + #attentionDTO.followerId")
    public Integer hasAttention(AttentionDTO attentionDTO) {
        // 判断是否合法
        if (attentionDTO.getUserId() == null){
            throw new RequestExcetption(MessageConstant.ILLEGAL_REQUEST);
        }
        AttentionDO attentionDO = attentionService.getOne(new QueryWrapper<AttentionDO>().eq("user_id", attentionDTO.getUserId()).eq("follower_id", attentionDTO.getUserId()));
        if (attentionDO == null){
            return -1;
        }
        return Integer.valueOf(attentionDO.getStatus());

    }
}
