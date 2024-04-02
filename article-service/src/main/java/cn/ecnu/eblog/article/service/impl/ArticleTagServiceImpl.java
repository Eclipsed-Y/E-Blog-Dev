package cn.ecnu.eblog.article.service.impl;

import cn.ecnu.eblog.article.mapper.ArticleTagMapper;
import cn.ecnu.eblog.article.service.ArticleTagService;
import cn.ecnu.eblog.article.service.TagService;
import cn.ecnu.eblog.common.pojo.entity.article.ArticleTagDO;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.query.MPJQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleTagServiceImpl extends MPJBaseServiceImpl<ArticleTagMapper, ArticleTagDO> implements ArticleTagService {
    @Override
    public List<String> getTagsByArticleId(Long id) {
        MPJQueryWrapper<ArticleTagDO> queryWrapper = new MPJQueryWrapper<>();
        queryWrapper.select("tag_name").eq("article_id", id).eq("deleted", 0);
        // 将查询到的tags映射到String列表中
        return this.listObjs(queryWrapper, Object::toString);
    }


}
