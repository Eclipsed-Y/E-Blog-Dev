package cn.ecnu.eblog.common.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AttentionDTO implements Serializable {
    private Long userId;
    private Long followerId;
}
