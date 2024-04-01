package cn.ecnu.eblog.article.service;

import cn.ecnu.eblog.common.pojo.dto.ArticlePageQueryDTO;
import cn.ecnu.eblog.common.pojo.entity.article.ArticleViewDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseService;
import com.github.yulichang.query.MPJQueryWrapper;

import java.util.List;

public interface ArticleViewService extends MPJBaseService<ArticleViewDO> {
    Page<ArticleViewDO> pageSelect(ArticlePageQueryDTO articlePageQueryDTO);
}
