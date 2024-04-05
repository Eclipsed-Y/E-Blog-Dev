package cn.ecnu.eblog.activity.service.impl;

import cn.ecnu.eblog.activity.mapper.RootCommentViewMapper;
import cn.ecnu.eblog.activity.service.RootCommentViewService;
import cn.ecnu.eblog.common.pojo.dto.RootCommentPageQueryDTO;
import cn.ecnu.eblog.common.pojo.entity.activity.RootCommentViewDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.query.MPJQueryWrapper;
import org.springframework.stereotype.Service;

@Service
public class RootCommentViewServiceImpl extends MPJBaseServiceImpl<RootCommentViewMapper, RootCommentViewDO> implements RootCommentViewService {
    @Override
    public Page<RootCommentViewDO> pageSelect(RootCommentPageQueryDTO rootCommentPageQueryDTO) {
        MPJQueryWrapper<RootCommentViewDO> wrapper = new MPJQueryWrapper<RootCommentViewDO>()
                .selectAll(RootCommentViewDO.class)
                .eq("article_id", rootCommentPageQueryDTO.getArticleId())
                .eq("deleted", 0)
                .eq("root_comment_id", 0)
                .orderByDesc("create_time");

        return this.page(new Page<>(rootCommentPageQueryDTO.getPage(), rootCommentPageQueryDTO.getPageSize()), wrapper);
    }
}
