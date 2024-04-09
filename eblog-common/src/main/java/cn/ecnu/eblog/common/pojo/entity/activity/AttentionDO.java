package cn.ecnu.eblog.common.pojo.entity.activity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("attention")
public class AttentionDO implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)  // 主键自增
    private Long id;
    private Long userId;
    private Long followerId;
    private Short status;
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
