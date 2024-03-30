package cn.ecnu.eblog.common.pojo.entity.article;

import cn.ecnu.eblog.common.pojo.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("article_detail")
public class TagDO extends BaseDO {
    private static final long serialVersionUID = 1L;
    private String tagName;
    private Short status;
    private Short rank;
}
