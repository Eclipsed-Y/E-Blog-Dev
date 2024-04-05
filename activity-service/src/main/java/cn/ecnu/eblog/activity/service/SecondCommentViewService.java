package cn.ecnu.eblog.activity.service;

import cn.ecnu.eblog.common.pojo.dto.SecondCommentPageQueryDTO;
import cn.ecnu.eblog.common.pojo.entity.activity.SecondCommentViewDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseService;

public interface SecondCommentViewService extends MPJBaseService<SecondCommentViewDO> {
    Page<SecondCommentViewDO> pageSelect(SecondCommentPageQueryDTO secondCommentPageQueryDTO);
}
