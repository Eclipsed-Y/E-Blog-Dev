package cn.ecnu.eblog.article.service.impl;

import cn.ecnu.eblog.article.mapper.ArticleTagMapper;
import cn.ecnu.eblog.article.service.ArticleTagService;
import cn.ecnu.eblog.article.service.TagService;
import cn.ecnu.eblog.common.constant.CacheConstant;
import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.exception.RequestExcetption;
import cn.ecnu.eblog.common.pojo.entity.article.ArticleTagDO;
import cn.ecnu.eblog.common.pojo.entity.article.TagDO;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.query.MPJQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleTagServiceImpl extends MPJBaseServiceImpl<ArticleTagMapper, ArticleTagDO> implements ArticleTagService {
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private TagService tagService;
    @Override
    @Cacheable(value = CacheConstant.ARTICLE_TAG, cacheManager = CacheConstant.CACHE_MANAGER, key = "#id")
    public List<String> getTagsByArticleId(Long id) {
        MPJQueryWrapper<ArticleTagDO> queryWrapper = new MPJQueryWrapper<>();
        queryWrapper.select("tag_name").eq("article_id", id).eq("deleted", 0);
        // 将查询到的tags映射到String列表中
        return this.listObjs(queryWrapper, Object::toString);
    }

    @CacheEvict(value = CacheConstant.ARTICLE_TAG, cacheManager = CacheConstant.CACHE_MANAGER, key = "#id")
    @Override
    public void saveTags(List<Long> tagIdList, Long id) {
        List<ArticleTagDO> list = new ArrayList<>();
        for (Long tagId : tagIdList) {
            TagDO tagDO = tagService.getById(tagId);
            if (tagDO == null || tagDO.getStatus() == 0){
                throw new RequestExcetption(MessageConstant.ILLEGAL_REQUEST);
            }

            ArticleTagDO articleTagDO = ArticleTagDO.builder()
                    .tagId(tagId)
                    .articleId(id)
                    .tagName(tagDO.getTagName())
                    .build();
            list.add(articleTagDO);
        }
        articleTagService.saveBatch(list);
    }
}
