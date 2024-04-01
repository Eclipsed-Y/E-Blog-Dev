package cn.ecnu.eblog.article.service.impl;

import cn.ecnu.eblog.article.mapper.ArticleMapper;
import cn.ecnu.eblog.article.service.ArticleDetailViewService;
import cn.ecnu.eblog.article.service.ArticleService;
import cn.ecnu.eblog.article.service.ArticleTagService;
import cn.ecnu.eblog.article.service.ArticleViewService;
import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.exception.PageException;
import cn.ecnu.eblog.common.pojo.dto.ArticleDTO;
import cn.ecnu.eblog.common.pojo.dto.ArticlePageQueryDTO;
import cn.ecnu.eblog.common.pojo.entity.article.ArticleDO;
import cn.ecnu.eblog.common.pojo.entity.article.ArticleDetailViewDO;
import cn.ecnu.eblog.common.pojo.entity.article.ArticleTagDO;
import cn.ecnu.eblog.common.pojo.entity.article.ArticleViewDO;
import cn.ecnu.eblog.common.pojo.result.PageResult;
import cn.ecnu.eblog.common.pojo.vo.ArticleDetailVO;
import cn.ecnu.eblog.common.pojo.vo.ArticleVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.query.MPJQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl extends MPJBaseServiceImpl<ArticleMapper, ArticleDO> implements ArticleService {

    @Autowired
    private ArticleViewService articleViewService;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private ArticleDetailViewService articleDetailViewService;
    @Override
    public PageResult getByCategoryId(ArticlePageQueryDTO articlePageQueryDTO) {

        Page<ArticleViewDO> page = articleViewService.pageSelect(articlePageQueryDTO);
        long total = page.getTotal();
        List<ArticleViewDO> records = page.getRecords();
        if (records == null || records.isEmpty()){
            throw new PageException(MessageConstant.PAGE_ERROR);
        }
        // 拼接tags
        List<ArticleVO> articleVOS = new ArrayList<>();
        for (ArticleViewDO record : records) {
            List<String> list = articleTagService.getTagsByArticleId(record.getId());
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(record, articleVO);
            articleVO.setTags(list);
            articleVOS.add(articleVO);
        }
        return new PageResult(total, articleVOS);
    }

    @Override
    public ArticleDetailVO getArticleDetail(Long id) {
        ArticleDetailViewDO articleDetailViewDO = articleDetailViewService.getArticleDetail(id);
        ArticleDetailVO articleDetailVO = new ArticleDetailVO();
        BeanUtils.copyProperties(articleDetailViewDO, articleDetailVO);
        // 拼接tags
        List<String> list = articleTagService.getTagsByArticleId(id);
        articleDetailVO.setTags(list);
        return articleDetailVO;
    }

    @Override
    public long storeArticle(ArticleDTO articleDTO) {
        ArticleDO articleDO = new ArticleDO();
        BeanUtils.copyProperties(articleDTO, articleDO);
        articleDO.setUserId(BaseContext.getCurrentId());
        articleDO.setStatus((short) -1);

        // 新暂存
        if (articleDO.getId() == null){
            // 判断是否管理员
            // todo 使用dubbo进行rpc调用
        }
        return 0;
    }

}