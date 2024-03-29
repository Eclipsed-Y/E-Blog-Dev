package cn.ecnu.eblog.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseDO implements Serializable {
    @TableId(type = IdType.AUTO)  // 主键自增
    private Long id;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;
}
