package cn.ecnu.eblog.common.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArticlePageQueryDTO implements Serializable {

    private int page;
    private int pageSize;
    // 分类id
    private Long categoryId;
    // 排序规则，0按发布时间，1按更新时间
    private Short sortMethod;

}