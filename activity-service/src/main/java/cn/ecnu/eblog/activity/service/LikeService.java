package cn.ecnu.eblog.activity.service;

import cn.ecnu.eblog.common.pojo.dto.LikeDTO;
import cn.ecnu.eblog.common.pojo.entity.activity.LikeDO;
import cn.ecnu.eblog.common.pojo.mq.Msg;
import com.github.yulichang.base.MPJBaseService;

public interface LikeService extends MPJBaseService<LikeDO> {
    void like(LikeDTO likeDTO);
}
