package cn.ecnu.eblog.article.service;

import cn.ecnu.eblog.common.pojo.entity.article.CategoryDO;
import cn.ecnu.eblog.common.pojo.vo.CategoryVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.yulichang.base.MPJBaseService;

import java.util.List;

public interface CategoryService extends MPJBaseService<CategoryDO> {
    List<CategoryVO> getAllCategories();
}
