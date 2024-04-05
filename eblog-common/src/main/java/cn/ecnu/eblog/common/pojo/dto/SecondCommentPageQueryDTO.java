package cn.ecnu.eblog.common.pojo.dto;

import lombok.Data;

@Data
public class SecondCommentPageQueryDTO {
    private int page;
    private int pageSize;
    private Long rootCommentId;
}
