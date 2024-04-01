package cn.ecnu.eblog.article.controller;

import cn.ecnu.eblog.article.service.ArticleService;
import cn.ecnu.eblog.article.service.ArticleTagService;
import cn.ecnu.eblog.article.service.ArticleViewService;
import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.exception.PageException;
import cn.ecnu.eblog.common.pojo.dto.ArticlePageQueryDTO;
import cn.ecnu.eblog.common.pojo.entity.article.ArticleTagDO;
import cn.ecnu.eblog.common.pojo.entity.article.ArticleViewDO;
import cn.ecnu.eblog.common.pojo.result.PageResult;
import cn.ecnu.eblog.common.pojo.result.Result;
import cn.ecnu.eblog.common.pojo.vo.ArticleVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.query.MPJQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private ArticleViewService articleViewService;

    /**
     * 根据分类id查看所有文章
     *
     * @param articlePageQueryDTO
     * @return
     */
    @GetMapping
    public Result<PageResult> getByCategoryId(ArticlePageQueryDTO articlePageQueryDTO) {
        log.info("id: {} 用户分页查询", BaseContext.getCurrentId());

//        // 联表查询
//        MPJLambdaWrapper<ArticleDO> wrapper = new MPJLambdaWrapper<>();
//        wrapper.selectAll(ArticleDO.class)  // 选择article表所有字段
//                .select(UserInfoDO::getNickname)
//                .select(CategoryDO::getCategoryName)
//                .leftJoin(UserInfoDO.class, UserInfoDO::getUserId, ArticleDO::getUserId)
//                .leftJoin(CategoryDO.class, CategoryDO::getId, ArticleDO::getCategoryId);
//
//        // 如果分类id为0代表all
//        if (articlePageQueryDTO.getCategoryId() != 0){
//            wrapper.eq(ArticleDO::getCategoryId, articlePageQueryDTO.getCategoryId());
//        }
//
//        wrapper.eq(ArticleDO::getStatus, 1).eq(ArticleDO::getDeleted, 0).orderByDesc(ArticleDO::getTopStat);
//
//        if (articlePageQueryDTO.getSortMethod() == 1){
//            wrapper.orderByDesc(ArticleDO::getUpdateTime);
//        } else {
//            wrapper.orderByDesc(ArticleDO::getCreateTime);
//        }
//        Page<ArticleVO> articleVOPage = articleService.selectJoinListPage(new Page<>(articlePageQueryDTO.getPage(), articlePageQueryDTO.getPageSize()), ArticleVO.class, wrapper);
//        List<ArticleVO> records = articleVOPage.getRecords();
//        for (ArticleVO record : records) {
//            MPJQueryWrapper<ArticleTagDO> queryWrapper = new MPJQueryWrapper<>();
//            queryWrapper.select("tag_name");
//            queryWrapper.eq("article_id", record.getId()).eq("deleted", 0);
//            List<String> list = articleTagService.listObjs(queryWrapper, Object::toString);
//            record.setTags(list);
//        }
//        return Result.success(new PageResult(articleVOPage.getTotal(), records));


        MPJQueryWrapper<ArticleViewDO> wrapper = new MPJQueryWrapper<ArticleViewDO>()
                .selectAll(ArticleViewDO.class)
                .eq("status", 1)
                .eq("deleted", 0)
                .orderByDesc("top_stat");
        // 分类id为0代表查询all
        if (articlePageQueryDTO.getCategoryId() != 0) {
            wrapper.eq("category_id", articlePageQueryDTO.getCategoryId());
        }
        // 判断根据创建时间还是更新时间排序
        if (articlePageQueryDTO.getSortMethod() == 1) {
            wrapper.orderByDesc("update_time");
        } else {
            wrapper.orderByDesc("create_time");
        }
        // 分页查询
        Page<ArticleViewDO> articleViewDOPage = articleViewService.page(new Page<>(articlePageQueryDTO.getPage(), articlePageQueryDTO.getPageSize()), wrapper);
        List<ArticleViewDO> records = articleViewDOPage.getRecords();
        if (records == null || records.isEmpty()) {
            throw new PageException(MessageConstant.PAGE_ERROR);
        }

        // 拼接tags
        List<ArticleVO> articleVOS = new ArrayList<>();
        for (ArticleViewDO record : records) {
            MPJQueryWrapper<ArticleTagDO> queryWrapper = new MPJQueryWrapper<>();
            queryWrapper.select("tag_name").eq("article_id", record.getId()).eq("deleted", 0);
            List<String> list = articleTagService.listObjs(queryWrapper, Object::toString);

            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(record, articleVO);
            articleVO.setTags(list);
            articleVOS.add(articleVO);
        }
        return Result.success(new PageResult(articleViewDOPage.getTotal(), articleVOS));
    }

//    @GetMapping("/{id}")
//    public Result<ArticleDetailVO> getArticleDetail(@PathVariable("id") Long id){
//        return Result.success();
//    }
}
