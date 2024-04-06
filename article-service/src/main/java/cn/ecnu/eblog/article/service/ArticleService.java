package cn.ecnu.eblog.article.service;

import cn.ecnu.eblog.common.pojo.dto.ArticleDTO;
import cn.ecnu.eblog.common.pojo.dto.ArticlePageQueryDTO;
import cn.ecnu.eblog.common.pojo.dto.UserInfoDTO;
import cn.ecnu.eblog.common.pojo.entity.article.ArticleDO;
import cn.ecnu.eblog.common.pojo.result.PageResult;
import cn.ecnu.eblog.common.pojo.vo.ArticleDetailVO;
import com.github.yulichang.base.MPJBaseService;

public interface ArticleService extends MPJBaseService<ArticleDO> {
    PageResult getByCategoryId(ArticlePageQueryDTO articlePageQueryDTO);

    ArticleDetailVO getArticleDetail(Long id);

    long insertArticle(ArticleDTO articleDTO);

    long updateArticle(ArticleDTO articleDTO);

    void deleteArticle(Long id);

    void updateUserInfo(UserInfoDTO userInfoDTO);
}
