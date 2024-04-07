package cn.ecnu.eblog.activity.service.impl;

import cn.ecnu.eblog.activity.mapper.LikeMapper;
import cn.ecnu.eblog.activity.service.LikeService;
import cn.ecnu.eblog.common.pojo.dto.LikeDTO;
import cn.ecnu.eblog.common.pojo.entity.activity.LikeDO;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl extends MPJBaseServiceImpl<LikeMapper, LikeDO> implements LikeService {

    @Override
    public void like(LikeDTO likeDTO) {
    }
}
