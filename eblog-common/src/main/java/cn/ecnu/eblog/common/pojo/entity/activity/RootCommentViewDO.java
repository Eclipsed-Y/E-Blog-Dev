package cn.ecnu.eblog.common.pojo.entity.activity;

import cn.ecnu.eblog.common.pojo.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("root_comment_view")
public class RootCommentViewDO extends BaseDO {
    private static final long serialVersionUID = 1L;
    private String nickname;
    private String avatar;
    private Long articleId;
    private Long userId;
    private Long rootCommentId;
    private Long parentCommentId;
    private String content;
}
