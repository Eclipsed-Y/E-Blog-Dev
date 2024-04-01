package cn.ecnu.eblog.article.service.impl;

import cn.ecnu.eblog.article.mapper.ArticleTagMapper;
import cn.ecnu.eblog.article.service.ArticleTagService;
import cn.ecnu.eblog.common.pojo.entity.article.ArticleTagDO;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ArticleTagServiceImpl extends MPJBaseServiceImpl<ArticleTagMapper, ArticleTagDO> implements ArticleTagService {
}
