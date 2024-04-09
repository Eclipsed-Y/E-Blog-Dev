package cn.ecnu.eblog.activity.service;

import cn.ecnu.eblog.common.pojo.dto.AttentionDTO;
import cn.ecnu.eblog.common.pojo.entity.activity.AttentionDO;
import com.github.yulichang.base.MPJBaseService;

public interface AttentionService extends MPJBaseService<AttentionDO> {
    void attentionByUserId(AttentionDTO attentionDTO);

    Integer getAttentionCount(Long userId);

    Integer hasAttention(AttentionDTO attentionDTO);
}
