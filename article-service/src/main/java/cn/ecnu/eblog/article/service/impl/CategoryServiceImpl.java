package cn.ecnu.eblog.article.service.impl;

import cn.ecnu.eblog.article.mapper.CategoryMapper;
import cn.ecnu.eblog.article.service.CategoryService;
import cn.ecnu.eblog.common.pojo.entity.article.CategoryDO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends MPJBaseServiceImpl<CategoryMapper, CategoryDO> implements CategoryService {
}
