package cn.ecnu.eblog.common.pojo.entity.article;

import cn.ecnu.eblog.common.pojo.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("article_detail_view")
public class ArticleDetailViewDO extends BaseDO {
    private static final long serialVersionUID = 1L;
    private Long articleId;
    private Long userId;
    private String nickname;
    private String avatar;
    private Short role;
    private String title;
    private String summary;
    private Long categoryId;
    private String categoryName;
    private Short source;
    private Short officialStat;
    private Short topStat;
    private Short status;
    private String content;
    private Long version;
}
