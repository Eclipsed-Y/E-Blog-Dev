package cn.ecnu.eblog.article.service;

import cn.ecnu.eblog.common.pojo.entity.article.TagDO;
import cn.ecnu.eblog.common.pojo.vo.TagVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.yulichang.base.MPJBaseService;

import java.util.List;

public interface TagService extends MPJBaseService<TagDO> {
    List<TagVO> getAllTags();
}
