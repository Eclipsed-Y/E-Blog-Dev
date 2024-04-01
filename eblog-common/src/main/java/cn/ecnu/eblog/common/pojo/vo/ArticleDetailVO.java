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
public class ArticleDetailVO {
    private Long id;
    private Long articleId;
    private Long userId;
    private String nickname;
    private String avatar;
    private Short role;
    private String title;
    private String summary;
    private Long categoryId;
    private String categoryName;
    private Short source;
    private Short officialStat;
    private Short topStat;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
