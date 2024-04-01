package cn.ecnu.eblog.article.service.impl;

import cn.ecnu.eblog.article.mapper.ArticleViewMapper;
import cn.ecnu.eblog.article.service.ArticleViewService;
import cn.ecnu.eblog.common.pojo.entity.article.ArticleViewDO;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ArticleViewServiceImpl extends MPJBaseServiceImpl<ArticleViewMapper, ArticleViewDO> implements ArticleViewService {
}
