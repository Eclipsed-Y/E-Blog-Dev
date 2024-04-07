package cn.ecnu.eblog.article.service.impl;

import cn.ecnu.eblog.article.mapper.ArticleMapper;
import cn.ecnu.eblog.article.service.*;
import cn.ecnu.eblog.common.constant.CacheConstant;
import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.exception.*;
import cn.ecnu.eblog.common.feign.CommentClient;
import cn.ecnu.eblog.common.feign.UserClient;
import cn.ecnu.eblog.common.pojo.dto.ArticleDTO;
import cn.ecnu.eblog.common.pojo.dto.ArticlePageQueryDTO;
import cn.ecnu.eblog.common.pojo.dto.UserInfoDTO;
import cn.ecnu.eblog.common.pojo.entity.article.*;
import cn.ecnu.eblog.common.pojo.result.PageResult;
import cn.ecnu.eblog.common.pojo.result.Result;
import cn.ecnu.eblog.common.pojo.vo.ArticleDetailVO;
import cn.ecnu.eblog.common.pojo.vo.ArticleVO;
import cn.ecnu.eblog.common.pojo.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.query.MPJQueryWrapper;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
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
    private TransactionTemplate transactionTemplate;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CommentClient commentClient;
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    @Override
    @Cacheable(value = CacheConstant.ARTICLE_PAGE, cacheManager = CacheConstant.CACHE_MANAGER, key = "#articlePageQueryDTO.categoryId + '_' + #articlePageQueryDTO.page + '_' + #articlePageQueryDTO.pageSize + '_' + #articlePageQueryDTO.sortMethod")
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
    @Cacheable(value = CacheConstant.ARTICLE_DETAIL, cacheManager = CacheConstant.CACHE_MANAGER, key = "#id")
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
    @CacheEvict(value = CacheConstant.ARTICLE_PAGE, cacheManager = CacheConstant.CACHE_MANAGER, allEntries = true)
    public long insertArticle(ArticleDTO articleDTO) {
        // 判断是否合法
        if (articleDTO.getId() != null || articleDTO.getStatus() != 0 && articleDTO.getStatus() != -1 || articleDTO.getSource() != 1 && articleDTO.getSource() != 0){
            throw new RequestExcetption(MessageConstant.ILLEGAL_REQUEST);
        }
        ArticleDO articleDO = new ArticleDO();
        BeanUtils.copyProperties(articleDTO, articleDO);
        // 设置userId
        articleDO.setUserId(BaseContext.getCurrentId());
        // 获取用户昵称和头像
        Result<UserInfoVO> userInfoRes = userClient.getUserInfoById(articleDO.getUserId(), articleDO.getUserId());
        if (userInfoRes.getCode() == 0){
            throw new FeignBaseException(MessageConstant.INNER_ERROR);
        }
        articleDO.setNickname(userInfoRes.getData().getNickname());
        articleDO.setAvatar(userInfoRes.getData().getAvatar());
        // 判断是否管理员
        Short role = userInfoRes.getData().getRole();
        if (role == 1){
            articleDO.setOfficialStat((short) 1);
            // 管理员直接发布
            if (articleDO.getStatus() == 0){
                articleDO.setStatus((short) 1);
            }
        }
        // 文章细节
        ArticleDetailDO articleDetailDO = new ArticleDetailDO();
        articleDetailDO.setContent(articleDTO.getContent());
        transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                // 插入文章
                articleService.save(articleDO);
                // 设置文章id，并插入文章细节
                articleDetailDO.setArticleId(articleDO.getId());
                articleDetailService.save(articleDetailDO);
                // 插入文章tags
                articleTagService.saveTags(articleDTO.getTagIdList(), articleDO.getId());
                return null;
            }
        });
        return articleDO.getId();
    }

    @Override
    @Caching(evict = {@CacheEvict(value = CacheConstant.ARTICLE_DETAIL, cacheManager = CacheConstant.CACHE_MANAGER, key = "#articleDTO.id"), @CacheEvict(value = CacheConstant.ARTICLE_PAGE, cacheManager = CacheConstant.CACHE_MANAGER, allEntries = true)} )
//    @CacheEvict(value = CacheConstant.ARTICLE_DETAIL, cacheManager = CacheConstant.CACHE_MANAGER, key = "#articleDTO.id")
    public long updateArticle(ArticleDTO articleDTO) {
        // 判断是否合法
        if (articleDTO.getId() == null || articleDTO.getStatus() != 0 && articleDTO.getStatus() != -1 || articleDTO.getSource() != 1 && articleDTO.getSource() != 0){
            throw new RequestExcetption(MessageConstant.ILLEGAL_REQUEST);
        }
        ArticleDO origin = articleService.getById(articleDTO.getId());
        // 确认用户是否有权限
        if (origin == null || origin.getDeleted() == 1 || !Objects.equals(origin.getUserId(), BaseContext.getCurrentId()) || origin.getStatus() != -1 && articleDTO.getStatus() == -1){
            throw new AccessException(MessageConstant.ACCESS_DENIED);
        }

        UpdateWrapper<ArticleDO> updateWrapper = new UpdateWrapper<ArticleDO>().eq("id", articleDTO.getId());
        // 判断是否管理员
        Result<UserInfoVO> useInfoRes = userClient.getUserInfoById(BaseContext.getCurrentId(), BaseContext.getCurrentId());
        if (useInfoRes.getCode() == 0){
            throw new FeignBaseException(MessageConstant.INNER_ERROR);
        }
        Short role = useInfoRes.getData().getRole();
        if (role == 1 && articleDTO.getStatus() == 0){
            updateWrapper.set("status", 1);
        } else if (role != 1 && origin.getStatus() == 1){
            // 如果非管理员则原始为已发布则修改状态为待审核
            updateWrapper.set("status", 0);
        } else {
            updateWrapper.set("status", articleDTO.getStatus());
        }

        if (articleDTO.getTitle() != null){
            updateWrapper.set("title", articleDTO.getTitle());
        }
        if (articleDTO.getSummary() != null){
            updateWrapper.set("summary", articleDTO.getSummary());
        }
        if (articleDTO.getCategoryId() != null){
            // 判断分类是否存在
            CategoryDO categoryDO = categoryService.getById(articleDTO.getCategoryId());
            if(categoryDO == null || categoryDO.getDeleted() == 1 || categoryDO.getStatus() == 0){
                throw new NotExistException(MessageConstant.CATEGORY_NOT_EXISTS);
            }
            updateWrapper.set("category_id", articleDTO.getCategoryId());
        }
        if (articleDTO.getSource() != null){
            updateWrapper.set("source", articleDTO.getSource());
        }
        transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                // 更新文章
                articleService.update(updateWrapper);
                // 更新detail
                if (articleDTO.getContent() != null){
                    UpdateWrapper<ArticleDetailDO> wrapper = new UpdateWrapper<ArticleDetailDO>().eq("article_id", articleDTO.getId());
                    wrapper.set("content", articleDTO.getContent());
                    wrapper.setSql("version = version + 1");
                    articleDetailService.update(wrapper);
                }
                // 更新标签
                if (articleDTO.getTagIdList() != null){
                    articleTagService.remove(new QueryWrapper<ArticleTagDO>().eq("article_id", articleDTO.getId()));
                    articleTagService.saveTags(articleDTO.getTagIdList(), articleDTO.getId());
                }
                return null;
            }
        });
        return articleDTO.getId();
    }

    @Override
    @Caching(evict = {@CacheEvict(value = CacheConstant.ARTICLE_DETAIL, cacheManager = CacheConstant.CACHE_MANAGER, key = "#id"), @CacheEvict(value = CacheConstant.ARTICLE_PAGE, cacheManager = CacheConstant.CACHE_MANAGER, allEntries = true)})
    public void deleteArticle(Long id) {
        // 确认用户是否有权限
        ArticleDO origin = articleService.getById(id);
        if (origin == null || origin.getDeleted() == 1 || !Objects.equals(origin.getUserId(), BaseContext.getCurrentId())){
            throw new AccessException(MessageConstant.ACCESS_DENIED);
        }
        UpdateWrapper<ArticleDO> wrapper = new UpdateWrapper<ArticleDO>().eq("id", id).set("deleted", 1);
        UpdateWrapper<ArticleDetailDO> detailWrapper = new UpdateWrapper<ArticleDetailDO>().eq("article_id", id).set("deleted", 1);
        UpdateWrapper<ArticleTagDO> tagWrapper = new UpdateWrapper<ArticleTagDO>().eq("article_id", id).set("deleted", 1);
//        transactionTemplate.execute(new TransactionCallback<Object>() {
//            @Override
//            public Object doInTransaction(TransactionStatus transactionStatus) {
//                articleService.update(wrapper);
//                articleDetailService.update(detailWrapper);
//                articleTagService.update(tagWrapper);
//                // todo 分布式事务
//                commentClient.deleteByArticleId(id, BaseContext.getCurrentId());
//                return null;
//            }
//        });

        TransactionStatus status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            articleService.update(wrapper);
            articleDetailService.update(detailWrapper);
            articleTagService.update(tagWrapper);
            // 分布式事务
            Result<?> result = commentClient.deleteByArticleId(id, BaseContext.getCurrentId());
            if (result.getCode() == 0){
                throw new FeignBaseException(MessageConstant.INNER_ERROR);
            }
            platformTransactionManager.commit(status);
        }catch (Exception e){
            log.info("分布式事务回滚");
            platformTransactionManager.rollback(status);
            throw e;
        }
    }

    /**
     * 内部方法，更新用户信息
     * @param userInfoDTO
     */
    @Override
    @Caching(evict = {@CacheEvict(value = CacheConstant.ARTICLE_DETAIL, cacheManager = CacheConstant.CACHE_MANAGER, allEntries = true), @CacheEvict(value = CacheConstant.ARTICLE_PAGE, cacheManager = CacheConstant.CACHE_MANAGER, allEntries = true)})
    public void updateUserInfo(UserInfoDTO userInfoDTO) {
        if (userInfoDTO.getNickname() == null && userInfoDTO.getAvatar() == null){
            return;
        }
        UpdateWrapper<ArticleDO> wrapper = new UpdateWrapper<ArticleDO>().eq("user_id", BaseContext.getCurrentId());
        if (userInfoDTO.getNickname() != null){
            wrapper.set("nickname", userInfoDTO.getNickname());
        }
        if (userInfoDTO.getAvatar() !=null){
            wrapper.set("avatar", userInfoDTO.getAvatar());
        }
        articleService.update(wrapper);
    }

}