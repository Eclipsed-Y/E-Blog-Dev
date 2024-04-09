package cn.ecnu.eblog.activity.service.impl;

import cn.ecnu.eblog.activity.mapper.LikeMapper;
import cn.ecnu.eblog.activity.service.LikeService;
import cn.ecnu.eblog.common.constant.CacheConstant;
import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.exception.RequestExcetption;
import cn.ecnu.eblog.common.feign.ArticleClient;
import cn.ecnu.eblog.common.pojo.dto.LikeDTO;
import cn.ecnu.eblog.common.pojo.entity.activity.LikeDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl extends MPJBaseServiceImpl<LikeMapper, LikeDO> implements LikeService {

    @Autowired
    private ArticleClient articleClient;
    @Autowired
    private LikeService likeService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
//    private RedisTemplate<String, Long> redisTemplate;
    @Override
    public void like(LikeDTO likeDTO) {
        // 检查合法性
        if (likeDTO.getArticleId() == null || !articleClient.existsArticle(likeDTO.getArticleId(), likeDTO.getUserId()).getData()){
            throw new RequestExcetption(MessageConstant.ILLEGAL_REQUEST);
        }
        LikeDO likeDO = new LikeDO();
        BeanUtils.copyProperties(likeDTO, likeDO);
        if (likeService.exists(new QueryWrapper<LikeDO>().eq("user_id", likeDO.getUserId()).eq("article_id", likeDO.getArticleId()))){
            // 已存在
            likeService.update(new UpdateWrapper<LikeDO>().eq("user_id", likeDO.getUserId()).eq("article_id", likeDO.getArticleId()).setSql("status = -status"));
        } else {
            // 尝试插入
            try{
                likeService.save(likeDO);
            } catch (Exception e){
                // 插入失败，说明其他线程已经插入
                likeService.update(new UpdateWrapper<LikeDO>().eq("user_id", likeDO.getUserId()).eq("article_id", likeDO.getArticleId()).setSql("status = -status"));
            }
        }
    }

    @Override
    @Cacheable(value = CacheConstant.LIKE_COUNT, cacheManager = "redisCacheManagerForNums", key = "#articleId")
    public Integer getLikeCount(Long articleId, Long userId) {
        // 判断是否合法
        if (!articleClient.existsArticle(articleId, userId).getData()){
            throw new RequestExcetption(MessageConstant.ILLEGAL_REQUEST);
        }
        return Math.toIntExact(likeService.count(new QueryWrapper<LikeDO>().eq("article_id", articleId).eq("status", 1)));
    }

    @Override
    @Cacheable(value = CacheConstant.LIKED, cacheManager = "redisCacheManagerForNums", key = "#likeDTO.articleId + '_' + #likeDTO.userId")
    public Integer liked(LikeDTO likeDTO) {
        if (likeDTO.getArticleId() == null){
            throw new RequestExcetption(MessageConstant.ILLEGAL_REQUEST);
        }
        LikeDO likeDO = likeService.getOne(new QueryWrapper<LikeDO>().eq("user_id", likeDTO.getUserId()).eq("article_id", likeDTO.getArticleId()));
        if (likeDO == null){
            return -1;
        }
        return Integer.valueOf(likeDO.getStatus());
    }
}
