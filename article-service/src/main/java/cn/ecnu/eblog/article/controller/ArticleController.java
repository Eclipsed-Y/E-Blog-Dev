package cn.ecnu.eblog.article.controller;

import cn.ecnu.eblog.article.service.ArticleService;
import cn.ecnu.eblog.common.pojo.dto.ArticlePageQueryDTO;
import cn.ecnu.eblog.common.pojo.entity.article.ArticleDO;
import cn.ecnu.eblog.common.pojo.entity.article.CategoryDO;
import cn.ecnu.eblog.common.pojo.entity.user.UserInfoDO;
import cn.ecnu.eblog.common.pojo.result.PageResult;
import cn.ecnu.eblog.common.pojo.result.Result;
import cn.ecnu.eblog.common.pojo.vo.ArticleVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 根据分类id查看所有文章
     * @param articlePageQueryDTO
     * @return
     */
    @GetMapping
    public Result<PageResult> getByCategoryId(ArticlePageQueryDTO articlePageQueryDTO){

        // 联表查询
        MPJLambdaWrapper<ArticleDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(ArticleDO.class)  // 选择article表所有字段
                .select(UserInfoDO::getNickname)
                .select(CategoryDO::getCategoryName)
                .leftJoin(UserInfoDO.class, UserInfoDO::getUserId, ArticleDO::getUserId)
                .leftJoin(CategoryDO.class, CategoryDO::getId, ArticleDO::getCategoryId);

        if (articlePageQueryDTO.getCategoryId() != 0){
            wrapper.eq(ArticleDO::getCategoryId, articlePageQueryDTO.getCategoryId());
        }

        wrapper.eq(ArticleDO::getStatus, 1).eq(ArticleDO::getDeleted, 0).orderByDesc(ArticleDO::getTopStat);

        if (articlePageQueryDTO.getSortMethod() == 1){
            wrapper.orderByDesc(ArticleDO::getUpdateTime);
        } else {
            wrapper.orderByDesc(ArticleDO::getCreateTime);
        }
        IPage<ArticleVO> articleVOPage = articleService.selectJoinListPage(new Page<>(articlePageQueryDTO.getPage(), articlePageQueryDTO.getPageSize()), ArticleVO.class, wrapper);
        //todo 还未拼接tag

        return Result.success(new PageResult(articleVOPage.getTotal(), articleVOPage.getRecords()));

//        QueryWrapper<ArticleDO> wrapper = new QueryWrapper<>();
//
//        if (articlePageQueryDTO.getCategoryId() != 0){
//            wrapper.eq("category_id", articlePageQueryDTO.getCategoryId());
//        }
//        wrapper.eq("status", 1).eq("deleted", 0).orderByDesc("top_stat");
//        if (articlePageQueryDTO.getSortMethod() == 1){
//            wrapper.orderByDesc("update_time");
//        } else {
//            wrapper.orderByDesc("create_time");
//        }
//        Page<ArticleDO> articleDOPage = articleService.page(page, wrapper);
//        return Result.success(new PageResult(articleDOPage.getTotal(), articleDOPage.getRecords()));
    }
}
