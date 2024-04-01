package cn.ecnu.eblog.article.service.impl;

import cn.ecnu.eblog.article.mapper.TagMapper;
import cn.ecnu.eblog.article.service.TagService;
import cn.ecnu.eblog.common.pojo.entity.article.TagDO;
import cn.ecnu.eblog.common.pojo.vo.TagVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl extends MPJBaseServiceImpl<TagMapper, TagDO> implements TagService {
    @Override
    public List<TagVO> getAllTags() {
        QueryWrapper<TagDO> wrapper = new QueryWrapper<>();
        // 未删除且已发布
        wrapper.eq("deleted", 0).eq("status", 1);
        wrapper.orderByAsc("sort");
        List<TagDO> tagDOS = this.list(wrapper);
        List<TagVO> tagVOS = new ArrayList<>();
        for (TagDO tagDO : tagDOS) {
            TagVO tagVO = TagVO.builder()
                    .id(tagDO.getId())
                    .tagName(tagDO.getTagName())
                    .build();
            tagVOS.add(tagVO);
        }
        return tagVOS;
    }
}
