package cn.ecnu.eblog.activity.mapper;

import cn.ecnu.eblog.common.pojo.entity.activity.CommentDO;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends MPJBaseMapper<CommentDO> {
}
