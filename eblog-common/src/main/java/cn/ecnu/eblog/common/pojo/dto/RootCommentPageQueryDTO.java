package cn.ecnu.eblog.common.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RootCommentPageQueryDTO implements Serializable {
    private int page;
    private int pageSize;
    private Long articleId;
}