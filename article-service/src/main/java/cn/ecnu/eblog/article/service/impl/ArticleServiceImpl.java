package cn.ecnu.eblog.article.service.impl;

import cn.ecnu.eblog.article.mapper.ArticleMapper;
import cn.ecnu.eblog.article.service.*;
import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.exception.PageException;
import cn.ecnu.eblog.common.feign.UserClient;
import cn.ecnu.eblog.common.pojo.dto.ArticleDTO;
import cn.ecnu.eblog.common.pojo.dto.ArticlePageQueryDTO;
import cn.ecnu.eblog.common.pojo.entity.article.*;
import cn.ecnu.eblog.common.pojo.result.PageResult;
import cn.ecnu.eblog.common.pojo.result.Result;
import cn.ecnu.eblog.common.pojo.vo.ArticleDetailVO;
import cn.ecnu.eblog.common.pojo.vo.ArticleVO;
import cn.ecnu.eblog.common.pojo.vo.UserInfoVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.query.MPJQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl extends MPJBaseServiceImpl<ArticleMapper, ArticleDO> implements ArticleService {
    @Autowired
    private ArticleDetailService articleDetailService;
    @Autowired
    private ArticleViewService articleViewService;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private ArticleDetailViewService articleDetailViewService;
    @Autowired
    private UserClient userClient;
    @Autowired
    private TagService tagService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private ArticleService articleService;
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
        // 设置userId
        articleDO.setUserId(BaseContext.getCurrentId());
        // 设置状态为暂存
        articleDO.setStatus((short) -1);

        // 文章细节
        ArticleDetailDO articleDetailDO = new ArticleDetailDO();
        articleDetailDO.setContent(articleDTO.getContent());

        // 新文章暂存
        if (articleDO.getId() == null){
            // 判断是否管理员
            Short role = userClient.getUserInfoById(articleDO.getUserId()).getData().getRole();
            if (role == 1){
                articleDO.setOfficialStat((short) 1);
            }

            transactionTemplate.execute(new TransactionCallback<Object>() {
                @Override
                public Object doInTransaction(TransactionStatus transactionStatus) {
                    // 插入文章
                    articleService.save(articleDO);
                    // 设置文章id，并插入文章细节
                    articleDetailDO.setArticleId(articleDO.getId());
                    articleDetailService.save(articleDetailDO);
                    // 插入文章tags
                    List<ArticleTagDO> list = new ArrayList<>();
                    for (Long tagId : articleDTO.getTagIdList()) {
                        String tagName = tagService.getById(tagId).getTagName();
                        ArticleTagDO articleTagDO = ArticleTagDO.builder()
                                .tagId(tagId)
                                .articleId(articleDO.getId())
                                .tagName(tagName)
                                .build();
                        list.add(articleTagDO);
                    }
                    articleTagService.saveBatch(list);
                    return null;
                }
            });
        }
        return articleDO.getId();
    }

}