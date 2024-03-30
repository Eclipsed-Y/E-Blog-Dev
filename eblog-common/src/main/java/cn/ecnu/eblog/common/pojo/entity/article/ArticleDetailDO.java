package cn.ecnu.eblog.common.pojo.entity.article;

import cn.ecnu.eblog.common.pojo.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("article_detail")
public class ArticleDetailDO extends BaseDO {
    private static final long serialVersionUID = 1L;
    private Long articleId;
    @TableField(update = "version + 1")
    private Long version;
    private String content;
}
