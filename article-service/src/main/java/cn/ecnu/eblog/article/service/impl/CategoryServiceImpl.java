package cn.ecnu.eblog.article.service.impl;

import cn.ecnu.eblog.article.mapper.CategoryMapper;
import cn.ecnu.eblog.article.service.CategoryService;
import cn.ecnu.eblog.common.pojo.entity.article.CategoryDO;
import cn.ecnu.eblog.common.pojo.vo.CategoryVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl extends MPJBaseServiceImpl<CategoryMapper, CategoryDO> implements CategoryService {
    @Override
    public List<CategoryVO> getAllCategories() {
        QueryWrapper<CategoryDO> wrapper = new QueryWrapper<>();
        // 未删除且已发布
        wrapper.eq("deleted", 0).eq("status", 1);
        wrapper.orderByAsc("sort");
        List<CategoryDO> categoryDOS = this.list(wrapper);
        List<CategoryVO> categoryVOS = new ArrayList<>();
        for (CategoryDO categoryDO : categoryDOS) {
            CategoryVO categoryVO = CategoryVO.builder()
                    .id(categoryDO.getId())
                    .categoryName(categoryDO.getCategoryName())
                    .build();
            categoryVOS.add(categoryVO);
        }
        return categoryVOS;
    }
}
