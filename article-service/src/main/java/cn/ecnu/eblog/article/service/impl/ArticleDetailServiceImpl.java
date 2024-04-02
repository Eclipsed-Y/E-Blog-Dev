package cn.ecnu.eblog.article.service.impl;

import cn.ecnu.eblog.article.mapper.ArticleDetailMapper;
import cn.ecnu.eblog.article.service.ArticleDetailService;
import cn.ecnu.eblog.common.pojo.entity.article.ArticleDetailDO;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ArticleDetailServiceImpl extends MPJBaseServiceImpl<ArticleDetailMapper, ArticleDetailDO> implements ArticleDetailService {
}
