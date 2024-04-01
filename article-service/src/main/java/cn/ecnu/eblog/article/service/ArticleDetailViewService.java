package cn.ecnu.eblog.article.service;

import cn.ecnu.eblog.common.pojo.entity.article.ArticleDetailViewDO;
import com.github.yulichang.base.MPJBaseService;

public interface ArticleDetailViewService extends MPJBaseService<ArticleDetailViewDO> {
    ArticleDetailViewDO getArticleDetail(Long id);
}
