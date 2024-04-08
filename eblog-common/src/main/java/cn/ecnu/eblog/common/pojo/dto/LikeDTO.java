package cn.ecnu.eblog.common.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LikeDTO implements Serializable {
    private Long userId;
    private Long articleId;
}
