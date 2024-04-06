package cn.ecnu.eblog.activity.service.impl;

import cn.ecnu.eblog.activity.mapper.SecondCommentViewMapper;
import cn.ecnu.eblog.activity.service.CommentService;
import cn.ecnu.eblog.activity.service.SecondCommentViewService;
import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.exception.RequestExcetption;
import cn.ecnu.eblog.common.pojo.dto.SecondCommentPageQueryDTO;
import cn.ecnu.eblog.common.pojo.entity.activity.CommentDO;
import cn.ecnu.eblog.common.pojo.entity.activity.SecondCommentViewDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.query.MPJQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecondCommentViewServiceImpl extends MPJBaseServiceImpl<SecondCommentViewMapper, SecondCommentViewDO> implements SecondCommentViewService {
    @Autowired
    private CommentService commentService;
    @Override
    public Page<SecondCommentViewDO> pageSelect(SecondCommentPageQueryDTO secondCommentPageQueryDTO) {
        // 判断是否合法
        if (secondCommentPageQueryDTO.getRootCommentId() == null || !commentService.exists(new QueryWrapper<CommentDO>().eq("root_comment_id", secondCommentPageQueryDTO.getRootCommentId()))){
            throw new RequestExcetption(MessageConstant.ILLEGAL_REQUEST);
        }
        MPJQueryWrapper<SecondCommentViewDO> wrapper = new MPJQueryWrapper<SecondCommentViewDO>()
                .selectAll(SecondCommentViewDO.class)
                .eq("deleted", 0)
                .eq("root_comment_id", secondCommentPageQueryDTO.getRootCommentId())
                .orderByDesc("create_time");
        return this.page(new Page<>(secondCommentPageQueryDTO.getPage(), secondCommentPageQueryDTO.getPageSize()), wrapper);
    }
}
