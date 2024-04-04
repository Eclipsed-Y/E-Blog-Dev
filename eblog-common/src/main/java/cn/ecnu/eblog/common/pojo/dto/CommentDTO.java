package cn.ecnu.eblog.common.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentDTO implements Serializable {
    private Long id;
    private Long userId;
    private Long articleId;
    private Long rootCommentId;
    private Long parentCommentId;
    private String content;
}
