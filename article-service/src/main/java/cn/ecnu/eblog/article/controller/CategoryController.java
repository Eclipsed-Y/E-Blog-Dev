package cn.ecnu.eblog.article.controller;

import cn.ecnu.eblog.article.service.CategoryService;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.pojo.entity.article.CategoryDO;
import cn.ecnu.eblog.common.pojo.result.Result;
import cn.ecnu.eblog.common.pojo.vo.CategoryVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 获取所有分类
     * @return
     */
    @GetMapping("/list")
    public Result<List<CategoryVO>> getAllCategories(){
        log.info("id: {} 用户获取所有分类", BaseContext.getCurrentId());
        List<CategoryVO> categoryVOS = categoryService.getAllCategories();
        return Result.success(categoryVOS);
    }
}
