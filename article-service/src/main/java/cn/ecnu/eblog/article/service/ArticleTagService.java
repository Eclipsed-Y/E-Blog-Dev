package cn.ecnu.eblog.article.service;

import cn.ecnu.eblog.common.pojo.entity.article.ArticleTagDO;
import com.github.yulichang.base.MPJBaseService;

import java.util.List;

public interface ArticleTagService extends MPJBaseService<ArticleTagDO> {
    List<String> getTagsByArticleId(Long id);

}
