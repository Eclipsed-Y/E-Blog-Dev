package cn.ecnu.eblog.article.controller;

import cn.ecnu.eblog.article.service.TagService;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.pojo.entity.article.TagDO;
import cn.ecnu.eblog.common.pojo.result.Result;
import cn.ecnu.eblog.common.pojo.vo.TagVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping("/tag")
@RestController
public class TagController {
    @Autowired
    private TagService tagService;

    /**
     * 获取所有tag
     * @return
     */
    @GetMapping("/list")
    public Result<List<TagVO>> getAllTags(){
        log.info("id: {} 用户获取所有tag", BaseContext.getCurrentId());
        QueryWrapper<TagDO> wrapper = new QueryWrapper<>();
        // 未删除且已发布
        wrapper.eq("deleted", 0).eq("status", 1);
        wrapper.orderByAsc("sort");
        List<TagDO> tagDOS = tagService.list(wrapper);
        List<TagVO> tagVOS = new ArrayList<>();
        for (TagDO tagDO : tagDOS) {
            TagVO tagVO = TagVO.builder()
                    .id(tagDO.getId())
                    .tagName(tagDO.getTagName())
                    .build();
            tagVOS.add(tagVO);
        }
        return Result.success(tagVOS);
    }
}
