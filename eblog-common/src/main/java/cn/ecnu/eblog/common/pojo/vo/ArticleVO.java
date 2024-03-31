package cn.ecnu.eblog.common.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVO {
    private Long id;
    private Long userId;
    private String nickname;
    private String title;
    private String summary;
    private Long categoryId;
    private String categoryName;
    private Short source;
    private Short officialStat;
    private Short topStat;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<String> tags;
}
