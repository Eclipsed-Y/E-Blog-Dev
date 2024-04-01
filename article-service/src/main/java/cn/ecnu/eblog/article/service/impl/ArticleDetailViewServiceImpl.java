package cn.ecnu.eblog.article.service.impl;

import cn.ecnu.eblog.article.mapper.ArticleDetailViewMapper;
import cn.ecnu.eblog.article.service.ArticleDetailViewService;
import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.exception.ArticleException;
import cn.ecnu.eblog.common.pojo.entity.article.ArticleDetailViewDO;
import cn.ecnu.eblog.common.pojo.vo.ArticleDetailVO;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.query.MPJQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ArticleDetailViewServiceImpl extends MPJBaseServiceImpl<ArticleDetailViewMapper, ArticleDetailViewDO> implements ArticleDetailViewService {
    @Override
    public ArticleDetailViewDO getArticleDetail(Long id) {
        MPJQueryWrapper<ArticleDetailViewDO> wrapper = new MPJQueryWrapper<>();
        wrapper.selectAll(ArticleDetailViewDO.class).eq("deleted", 0).eq("article_id", id);
        ArticleDetailViewDO articleDetailViewDO = this.getOne(wrapper);
        // 如果无对应id或者未发布且非本人，则报错
        if (articleDetailViewDO == null || articleDetailViewDO.getStatus() != 1 && !Objects.equals(articleDetailViewDO.getUserId(), BaseContext.getCurrentId())) {
            throw new ArticleException(MessageConstant.ARTICLE_NOT_FOUND);
        }
        return articleDetailViewDO;
    }
}
