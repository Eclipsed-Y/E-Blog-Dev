package cn.ecnu.eblog.common.pojo.entity.article;

import cn.ecnu.eblog.common.pojo.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("article_tag")
public class ArticleTagDO extends BaseDO {
    private static final long serialVersionUID = 1L;
    private Long articleId;
    private Long tagId;
    private String tagName;
}
