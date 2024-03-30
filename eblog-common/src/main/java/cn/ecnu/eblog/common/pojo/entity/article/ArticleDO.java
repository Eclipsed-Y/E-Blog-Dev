package cn.ecnu.eblog.common.pojo.entity.article;

import cn.ecnu.eblog.common.pojo.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("article")
public class ArticleDO extends BaseDO {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private String title;
    private String summary;
    private Long categoryId;
    private Short source;
    private Short officialStat;
    private Short topStat;
    private Short status;
}
