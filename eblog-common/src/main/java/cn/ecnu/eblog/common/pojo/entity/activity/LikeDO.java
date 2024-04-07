package cn.ecnu.eblog.common.pojo.entity.activity;

import cn.ecnu.eblog.common.pojo.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("like")
public class LikeDO extends BaseDO {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private Long articleId;
    private Short status;
}
