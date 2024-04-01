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
        List<TagVO> tagVOS = tagService.getAllTags();
        return Result.success(tagVOS);
    }
}
