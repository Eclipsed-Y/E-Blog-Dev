package cn.ecnu.eblog.article.service.impl;

import cn.ecnu.eblog.article.mapper.TagMapper;
import cn.ecnu.eblog.article.service.TagService;
import cn.ecnu.eblog.common.pojo.entity.article.TagDO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl extends MPJBaseServiceImpl<TagMapper, TagDO> implements TagService {
}
