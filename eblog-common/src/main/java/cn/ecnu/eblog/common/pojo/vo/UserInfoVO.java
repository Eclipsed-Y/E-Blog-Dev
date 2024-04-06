package cn.ecnu.eblog.common.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO implements Serializable {
    private Long userId;
    private String nickname;
    private String avatar;
    private String position;
    private Short role;
    private String profile;
}
