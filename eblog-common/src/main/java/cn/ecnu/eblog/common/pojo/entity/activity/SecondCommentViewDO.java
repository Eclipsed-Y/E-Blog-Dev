package cn.ecnu.eblog.common.pojo.entity.activity;

import cn.ecnu.eblog.common.pojo.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("second_comment_view")
public class SecondCommentViewDO extends BaseDO {
    private static final long serialVersionUID = 1L;

    private Long articleId;
    private Long userId;
    private Long rootCommentId;
    private Long parentCommentId;
    private String nickname;
    private String avatar;
    private String targetNickname;
    private String content;
}
