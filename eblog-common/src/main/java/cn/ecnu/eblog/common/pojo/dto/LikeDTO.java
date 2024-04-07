package cn.ecnu.eblog.common.pojo.dto;

import lombok.Data;

@Data
public class LikeDTO {
    private Long userId;
    private Long articleId;
}
