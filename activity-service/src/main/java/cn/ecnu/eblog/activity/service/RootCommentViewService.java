package cn.ecnu.eblog.activity.service;

import cn.ecnu.eblog.common.pojo.dto.RootCommentPageQueryDTO;
import cn.ecnu.eblog.common.pojo.entity.activity.RootCommentViewDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseService;

public interface RootCommentViewService extends MPJBaseService<RootCommentViewDO> {
    Page<RootCommentViewDO> pageSelect(RootCommentPageQueryDTO rootCommentPageQueryDTO);
}
