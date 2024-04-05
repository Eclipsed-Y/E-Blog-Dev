package cn.ecnu.eblog.common.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecondCommentVO {
    private Long id;
    private Long userId;
    private String nickname;
    private String avatar;
    private String content;
    private Long parentCommentId;
    private String targetNickname;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
