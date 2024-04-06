package cn.ecnu.eblog.activity.service;

import cn.ecnu.eblog.common.pojo.dto.CommentDTO;
import cn.ecnu.eblog.common.pojo.dto.CommentPageQueryDTO;
import cn.ecnu.eblog.common.pojo.dto.SecondCommentPageQueryDTO;
import cn.ecnu.eblog.common.pojo.dto.UserInfoDTO;
import cn.ecnu.eblog.common.pojo.entity.activity.CommentDO;
import cn.ecnu.eblog.common.pojo.result.PageResult;
import com.github.yulichang.base.MPJBaseService;

public interface CommentService extends MPJBaseService<CommentDO> {
    void insertComment(CommentDTO commentDTO);

    void updateComment(CommentDTO commentDTO);

    void deleteComment(CommentDTO commentDTO);

    PageResult pageSelect(CommentPageQueryDTO commentPageQueryDTO);

    PageResult getSecondComment(SecondCommentPageQueryDTO secondCommentPageQueryDTO);

    void updateUserInfo(UserInfoDTO userInfoDTO);


}
