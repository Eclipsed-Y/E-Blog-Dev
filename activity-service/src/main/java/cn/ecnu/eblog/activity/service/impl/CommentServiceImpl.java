package cn.ecnu.eblog.activity.service.impl;

import cn.ecnu.eblog.activity.mapper.CommentMapper;
import cn.ecnu.eblog.activity.service.CommentService;
import cn.ecnu.eblog.activity.service.RootCommentViewService;
import cn.ecnu.eblog.activity.service.SecondCommentViewService;
import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.exception.AccessException;
import cn.ecnu.eblog.common.exception.RequestExcetption;
import cn.ecnu.eblog.common.feign.ArticleClient;
import cn.ecnu.eblog.common.pojo.dto.CommentDTO;
import cn.ecnu.eblog.common.pojo.dto.RootCommentPageQueryDTO;
import cn.ecnu.eblog.common.pojo.dto.SecondCommentPageQueryDTO;
import cn.ecnu.eblog.common.pojo.entity.activity.CommentDO;
import cn.ecnu.eblog.common.pojo.entity.activity.RootCommentViewDO;
import cn.ecnu.eblog.common.pojo.entity.activity.SecondCommentViewDO;
import cn.ecnu.eblog.common.pojo.result.PageResult;
import cn.ecnu.eblog.common.pojo.result.Result;
import cn.ecnu.eblog.common.pojo.vo.RootCommentVO;
import cn.ecnu.eblog.common.pojo.vo.SecondCommentVO;
import com.alibaba.nacos.shaded.org.checkerframework.checker.index.qual.SameLen;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class CommentServiceImpl extends MPJBaseServiceImpl<CommentMapper, CommentDO> implements CommentService {
    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleClient articleClient;
    @Autowired
    private RootCommentViewService rootCommentViewService;
    @Autowired
    private SecondCommentViewService secondCommentViewService;

    private boolean check(CommentDTO commentDTO) {
        CommentDO commentDO = commentService.getOne(new QueryWrapper<CommentDO>().eq("id", commentDTO.getId()).eq("deleted", 0));
        if (commentDO == null || !articleClient.existsArticle(commentDO.getArticleId(), commentDTO.getUserId()).getData() || !Objects.equals(commentDO.getUserId(), commentDTO.getUserId())){
            log.info("id: {} 用户修改评论失败", commentDTO.getUserId());
            return true;
        }
        return false;
    }

    @Override
    public void insertComment(CommentDTO commentDTO) {
        CommentDO commentDO = new CommentDO();
        BeanUtils.copyProperties(commentDTO, commentDO);
        // 判断是否合法
        if (commentDO.getId() != null || commentDO.getArticleId() == null || !articleClient.existsArticle(commentDO.getArticleId(), commentDO.getUserId()).getData() || commentDO.getRootCommentId() == null || commentDO.getParentCommentId() == null) {
            log.info("id: {} 用户评论失败", commentDO.getUserId());
            return;
        }
        if (commentDO.getRootCommentId() != 0){
            CommentDO root = commentService.getById(commentDO.getRootCommentId());
            if (root == null || !Objects.equals(root.getArticleId(), commentDO.getArticleId())){
                log.info("id: {} 用户评论失败", commentDO.getUserId());
                return;
            }
        }
        if (Objects.equals(commentDO.getParentCommentId(), commentDO.getRootCommentId())){
            commentDO.setParentCommentId(0L);
        }
        if (commentDO.getParentCommentId() != 0){
            CommentDO parent = commentService.getById(commentDO.getParentCommentId());
            if (parent == null || !Objects.equals(parent.getArticleId(), commentDO.getArticleId())){
                log.info("id: {} 用户评论失败", commentDO.getUserId());
                return;
            }
        }
        commentService.save(commentDO);
    }

    @Override
    public void updateComment(CommentDTO commentDTO) {
        // 判断是否合法
        if (commentDTO.getId() == null || commentDTO.getContent() == null){
            log.info("id: {} 用户修改评论失败", commentDTO.getUserId());
        }
        if (check(commentDTO)) return;
        commentService.update(new UpdateWrapper<CommentDO>().eq("id", commentDTO.getId()).set("content", commentDTO.getContent()));
    }

    @Override
    public void deleteComment(CommentDTO commentDTO) {
        // 判断是否合法
        if (commentDTO.getId() == null){
            log.info("id: {} 用户修改评论失败", commentDTO.getUserId());
            return;
        }
        if (check(commentDTO)) return;
        commentService.update(new UpdateWrapper<CommentDO>().eq("id", commentDTO.getId()).set("deleted", 1));
    }

    @Override
    public PageResult getByArticleId(RootCommentPageQueryDTO rootCommentPageQueryDTO) {
        Page<RootCommentViewDO> page = rootCommentViewService.pageSelect(rootCommentPageQueryDTO);
        long total = page.getTotal();
        List<RootCommentViewDO> records = page.getRecords();
        List<RootCommentVO> list = new ArrayList<>();
        // 计算子评论数量
        for (RootCommentViewDO record : records) {
            long count = commentService.count(new QueryWrapper<CommentDO>().eq("root_comment_id", record.getId()));
            RootCommentVO rootCommentVO = new RootCommentVO();
            BeanUtils.copyProperties(record, rootCommentVO);
            rootCommentVO.setCount(count);
            list.add(rootCommentVO);
        }
        return new PageResult(total, list);
    }

    @Override
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
}
