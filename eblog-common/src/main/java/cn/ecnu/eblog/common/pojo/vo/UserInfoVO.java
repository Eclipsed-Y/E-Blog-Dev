package cn.ecnu.eblog.common.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO {
    private Long userId;
    private String nickname;
    private String avatar;
    private String position;
    private Short role;
    private String profile;
}
