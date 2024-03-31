package cn.ecnu.eblog.article.service.impl;

import cn.ecnu.eblog.article.mapper.ArticleMapper;
import cn.ecnu.eblog.article.service.ArticleService;
import cn.ecnu.eblog.common.pojo.entity.article.ArticleDO;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl extends MPJBaseServiceImpl<ArticleMapper, ArticleDO> implements ArticleService{

}