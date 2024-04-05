package cn.ecnu.eblog.activity.service.impl;

import cn.ecnu.eblog.activity.mapper.SecondCommentViewMapper;
import cn.ecnu.eblog.activity.service.SecondCommentViewService;
import cn.ecnu.eblog.common.pojo.dto.SecondCommentPageQueryDTO;
import cn.ecnu.eblog.common.pojo.entity.activity.SecondCommentViewDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.query.MPJQueryWrapper;
import org.springframework.stereotype.Service;

@Service
public class SecondCommentViewServiceImpl extends MPJBaseServiceImpl<SecondCommentViewMapper, SecondCommentViewDO> implements SecondCommentViewService {
    @Override
    public Page<SecondCommentViewDO> pageSelect(SecondCommentPageQueryDTO secondCommentPageQueryDTO) {
        MPJQueryWrapper<SecondCommentViewDO> wrapper = new MPJQueryWrapper<SecondCommentViewDO>()
                .selectAll(SecondCommentViewDO.class)
                .eq("deleted", 0)
                .eq("root_comment_id", secondCommentPageQueryDTO.getRootCommentId())
                .orderByDesc("create_time");
        return this.page(new Page<>(secondCommentPageQueryDTO.getPage(), secondCommentPageQueryDTO.getPageSize()), wrapper);
    }
}
