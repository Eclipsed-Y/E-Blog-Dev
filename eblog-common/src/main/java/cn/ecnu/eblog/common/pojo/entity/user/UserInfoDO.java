package cn.ecnu.eblog.common.pojo.entity.user;

import cn.ecnu.eblog.common.pojo.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_info")
public class UserInfoDO extends BaseDO {
    private Long userId;
    private String nickname;
    private String avatar;
    private String position;
    private String profile;
    private Short role;
}
