package cn.ecnu.eblog.common.pojo.dto;

import lombok.Data;

import java.util.List;

@Data
public class ArticleDTO {
    private Long id;
    private String title;
    private String summary;
    private Long categoryId;
    private Short source;
    private String content;
    private List<Long> tagIdList;
}
