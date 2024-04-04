package cn.ecnu.eblog.common.pojo.entity.activity;

import cn.ecnu.eblog.common.pojo.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("comment")
public class CommentDO extends BaseDO {
    private static final long serialVersionUID = 1L;
    private Long articleId;
    private Long userId;
    private Long rootCommentId;
    private Long parentCommentId;
    private String content;
}
