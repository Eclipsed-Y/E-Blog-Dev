package cn.ecnu.eblog.article.mapper;

import cn.ecnu.eblog.common.pojo.entity.article.ArticleDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<ArticleDO> {
}
