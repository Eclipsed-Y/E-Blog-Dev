package cn.ecnu.eblog.common.pojo.entity.user;

import cn.ecnu.eblog.common.pojo.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.net.ssl.SNIHostName;
import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user")
public class UserDO extends BaseDO {
    @Serial
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private Short isWhite;
    private Short deleted;
}
