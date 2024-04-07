package cn.ecnu.eblog.activity.service.impl;

import cn.ecnu.eblog.activity.mapper.CommentMapper;
import cn.ecnu.eblog.activity.service.CommentService;
import cn.ecnu.eblog.activity.service.SecondCommentViewService;
import cn.ecnu.eblog.activity.utils.RedisUtil;
import cn.ecnu.eblog.common.constant.CacheConstant;
import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.exception.FeignBaseException;
import cn.ecnu.eblog.common.exception.RequestExcetption;
import cn.ecnu.eblog.common.feign.ArticleClient;
import cn.ecnu.eblog.common.feign.UserClient;
import cn.ecnu.eblog.common.pojo.dto.CommentDTO;
import cn.ecnu.eblog.common.pojo.dto.CommentPageQueryDTO;
import cn.ecnu.eblog.common.pojo.dto.SecondCommentPageQueryDTO;
import cn.ecnu.eblog.common.pojo.dto.UserInfoDTO;
import cn.ecnu.eblog.common.pojo.entity.activity.CommentDO;
import cn.ecnu.eblog.common.pojo.entity.activity.SecondCommentViewDO;
import cn.ecnu.eblog.common.pojo.result.PageResult;
import cn.ecnu.eblog.common.pojo.result.Result;
import cn.ecnu.eblog.common.pojo.vo.CommentVO;
import cn.ecnu.eblog.common.pojo.vo.SecondCommentVO;
import cn.ecnu.eblog.common.pojo.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.query.MPJQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Service
@Slf4j
public class CommentServiceImpl extends MPJBaseServiceImpl<CommentMapper, CommentDO> implements CommentService {
    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleClient articleClient;
    @Autowired
    private SecondCommentViewService secondCommentViewService;
    @Autowired
    private UserClient userClient;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 判断是否非法
     * 合法返回DO，非法返回null
     *
     * @param commentDTO
     * @return
     */
    private CommentDO checkIllegal(CommentDTO commentDTO) {
        CommentDO commentDO = commentService.getOne(new QueryWrapper<CommentDO>().eq("id", commentDTO.getId()).eq("deleted", 0));
        if (commentDO == null || !articleClient.existsArticle(commentDO.getArticleId(), commentDTO.getUserId()).getData() || !Objects.equals(commentDO.getUserId(), commentDTO.getUserId())) {
            log.info("id: {} 用户修改评论失败", commentDTO.getUserId());
            return null;
        }
        return commentDO;
    }

    /**
     * 手动删除缓存
     * @param commentDTO
     */
    @Override
    public void insertComment(CommentDTO commentDTO) {
        CommentDO commentDO = new CommentDO();
        BeanUtils.copyProperties(commentDTO, commentDO);
        // 判断是否合法
        if (commentDO.getId() != null || commentDO.getArticleId() == null || !articleClient.existsArticle(commentDO.getArticleId(), commentDO.getUserId()).getData() || commentDO.getRootCommentId() == null || commentDO.getParentCommentId() == null) {
            log.info("id: {} 用户评论失败", commentDO.getUserId());
            return;
        }
        if (commentDO.getRootCommentId() != 0) {
            CommentDO root = commentService.getById(commentDO.getRootCommentId());
            if (root == null || !Objects.equals(root.getArticleId(), commentDO.getArticleId())) {
                log.info("id: {} 用户评论失败", commentDO.getUserId());
                return;
            }
        }
        if (Objects.equals(commentDO.getParentCommentId(), commentDO.getRootCommentId())) {
            commentDO.setParentCommentId(0L);
        }
        if (commentDO.getParentCommentId() != 0) {
            CommentDO parent = commentService.getById(commentDO.getParentCommentId());
            if (parent == null || !Objects.equals(parent.getArticleId(), commentDO.getArticleId())) {
                log.info("id: {} 用户评论失败", commentDO.getUserId());
                return;
            }
        }
        // feign调用获取用户信息
        Result<UserInfoVO> userInfoRes = userClient.getUserInfoById(commentDO.getUserId(), commentDO.getUserId());
        if (userInfoRes.getCode() == 0) {
            throw new FeignBaseException(MessageConstant.INNER_ERROR);
        }
        commentDO.setNickname(userInfoRes.getData().getNickname());
        commentDO.setAvatar(userInfoRes.getData().getAvatar());
        commentService.save(commentDO);

        // 删除缓存
        redisUtil.remove(CacheConstant.COMMENT_PAGE, commentDTO.getArticleId() + "_");
        redisUtil.remove(CacheConstant.SECOND_COMMENT_PAGE, commentDO.getRootCommentId() + "_");
    }

    /**
     * 手动删除缓存
     * @param commentDTO
     */
    @Override
    public void updateComment(CommentDTO commentDTO) {
        CommentDO commentDO = null;
        // 判断是否合法
        if (commentDTO.getId() == null || commentDTO.getContent() == null || (commentDO = checkIllegal(commentDTO)) == null) {
            log.info("id: {} 用户修改评论请求非法", commentDTO.getUserId());
            return;
        }
        commentService.update(new UpdateWrapper<CommentDO>().eq("id", commentDTO.getId()).set("content", commentDTO.getContent()));
        // 删除缓存
        // 删除缓存
        redisUtil.remove(CacheConstant.COMMENT_PAGE, commentDO.getArticleId() + "_");
        redisUtil.remove(CacheConstant.SECOND_COMMENT_PAGE, commentDO.getRootCommentId() + "_");
    }

    /**
     * 手动删除缓存
     * @param commentDTO
     */
    @Override
    public void deleteComment(CommentDTO commentDTO) {
        CommentDO commentDO = null;
        // 判断是否合法
        if (commentDTO.getId() == null || (commentDO = checkIllegal(commentDTO)) == null) {
            log.info("id: {} 用户删除评论请求非法", commentDTO.getUserId());
            return;
        }
        commentService.update(new UpdateWrapper<CommentDO>()
                .eq("id", commentDTO.getId())
                .or(commentDOUpdateWrapper -> commentDOUpdateWrapper.eq("root_comment_id", commentDTO.getId())).set("deleted", 1));
        // 删除缓存
        redisUtil.remove(CacheConstant.COMMENT_PAGE, commentDO.getArticleId() + "_");
        redisUtil.remove(CacheConstant.SECOND_COMMENT_PAGE, commentDO.getRootCommentId() + "_");
    }

    @Override
    @Cacheable(value = CacheConstant.COMMENT_PAGE, cacheManager = CacheConstant.CACHE_MANAGER, key = "#commentPageQueryDTO.articleId + '_' + #commentPageQueryDTO.page + '_' + #commentPageQueryDTO.pageSize")
    public PageResult pageSelect(CommentPageQueryDTO commentPageQueryDTO) {
        // 判断是否合法
        if (commentPageQueryDTO.getArticleId() == null || !articleClient.existsArticle(commentPageQueryDTO.getArticleId(), BaseContext.getCurrentId()).getData()) {
            throw new RequestExcetption(MessageConstant.ILLEGAL_REQUEST);
        }
        MPJQueryWrapper<CommentDO> wrapper = new MPJQueryWrapper<CommentDO>()
                .selectAll(CommentDO.class)
                .eq("article_id", commentPageQueryDTO.getArticleId())
                .eq("deleted", 0)
                .eq("root_comment_id", 0)
                .orderByDesc("create_time");
        Page<CommentDO> page = commentService.page(new Page<>(commentPageQueryDTO.getPage(), commentPageQueryDTO.getPageSize()), wrapper);
        long total = page.getTotal();
        List<CommentDO> records = page.getRecords();
        List<CommentVO> list = new ArrayList<>();
        // 计算子评论数量
        for (CommentDO record : records) {
            long count = commentService.count(new QueryWrapper<CommentDO>().eq("root_comment_id", record.getId()));
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(record, commentVO);
            commentVO.setCount(count);
            list.add(commentVO);
        }
        return new PageResult(total, list);
    }

    @Override
    @Cacheable(value = CacheConstant.SECOND_COMMENT_PAGE, cacheManager = CacheConstant.CACHE_MANAGER, key = "#secondCommentPageQueryDTO.rootCommentId + '_' + #secondCommentPageQueryDTO.page + '_' + #secondCommentPageQueryDTO.pageSize")
    public PageResult getSecondComment(SecondCommentPageQueryDTO secondCommentPageQueryDTO) {
        Page<SecondCommentViewDO> page = secondCommentViewService.pageSelect(secondCommentPageQueryDTO);
        long total = page.getTotal();
        List<SecondCommentViewDO> records = page.getRecords();
        List<SecondCommentVO> list = new ArrayList<>();
        for (SecondCommentViewDO record : records) {
            SecondCommentVO secondCommentVO = new SecondCommentVO();
            BeanUtils.copyProperties(record, secondCommentVO);
            list.add(secondCommentVO);
        }
        return new PageResult(total, list);
    }

    /**
     * 内部方法，更新用户信息
     *
     * @param userInfoDTO
     */
    @Override
    @Caching(evict = {@CacheEvict(value = CacheConstant.COMMENT_PAGE, cacheManager = CacheConstant.CACHE_MANAGER, allEntries = true), @CacheEvict(value = CacheConstant.SECOND_COMMENT_PAGE, cacheManager = CacheConstant.CACHE_MANAGER, allEntries = true)})
    public void updateUserInfo(UserInfoDTO userInfoDTO) {
        if (userInfoDTO.getNickname() == null && userInfoDTO.getAvatar() == null) {
            return;
        }
        UpdateWrapper<CommentDO> wrapper = new UpdateWrapper<CommentDO>().eq("user_id", BaseContext.getCurrentId());
        if (userInfoDTO.getNickname() != null) {
            wrapper.set("nickname", userInfoDTO.getNickname());
        }
        if (userInfoDTO.getAvatar() != null) {
            wrapper.set("avatar", userInfoDTO.getAvatar());
        }
        commentService.update(wrapper);
    }
}
