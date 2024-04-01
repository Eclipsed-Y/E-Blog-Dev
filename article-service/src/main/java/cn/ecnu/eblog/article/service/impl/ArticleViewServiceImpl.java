package cn.ecnu.eblog.article.service.impl;

import cn.ecnu.eblog.article.mapper.ArticleViewMapper;
import cn.ecnu.eblog.article.service.ArticleViewService;
import cn.ecnu.eblog.common.pojo.dto.ArticlePageQueryDTO;
import cn.ecnu.eblog.common.pojo.entity.article.ArticleViewDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.query.MPJQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleViewServiceImpl extends MPJBaseServiceImpl<ArticleViewMapper, ArticleViewDO> implements ArticleViewService {
    @Override
    public Page<ArticleViewDO> pageSelect(ArticlePageQueryDTO articlePageQueryDTO) {
        MPJQueryWrapper<ArticleViewDO> wrapper = new MPJQueryWrapper<ArticleViewDO>()
                .selectAll(ArticleViewDO.class)
                .eq("status", 1)
                .eq("deleted", 0)
                .orderByDesc("top_stat");
        // 分类id为0代表查询all
        if (articlePageQueryDTO.getCategoryId() != 0) {
            wrapper.eq("category_id", articlePageQueryDTO.getCategoryId());
        }
        // 判断根据创建时间还是更新时间排序
        if (articlePageQueryDTO.getSortMethod() == 1) {
            wrapper.orderByDesc("update_time");
        } else {
            wrapper.orderByDesc("create_time");
        }
        // 分页查询
        return this.page(new Page<>(articlePageQueryDTO.getPage(), articlePageQueryDTO.getPageSize()), wrapper);
    }
}
