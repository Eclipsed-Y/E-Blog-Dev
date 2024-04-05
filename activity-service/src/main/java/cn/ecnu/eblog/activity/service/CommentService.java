package cn.ecnu.eblog.activity.service;

import cn.ecnu.eblog.common.pojo.dto.CommentDTO;
import cn.ecnu.eblog.common.pojo.dto.RootCommentPageQueryDTO;
import cn.ecnu.eblog.common.pojo.dto.SecondCommentPageQueryDTO;
import cn.ecnu.eblog.common.pojo.entity.activity.CommentDO;
import cn.ecnu.eblog.common.pojo.result.PageResult;
import com.github.yulichang.base.MPJBaseService;

public interface CommentService extends MPJBaseService<CommentDO> {
    void insertComment(CommentDTO commentDTO);

    void updateComment(CommentDTO commentDTO);

    void deleteComment(CommentDTO commentDTO);

    PageResult getByArticleId(RootCommentPageQueryDTO rootCommentPageQueryDTO);

    PageResult getSecondComment(SecondCommentPageQueryDTO secondCommentPageQueryDTO);
}
