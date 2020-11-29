package com.report.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.report.bean.CategoryInfo;
import com.report.dao.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by mingku on 2020/11/20.
 */
@Service
@Transactional
public class CategoryService {
    @Autowired
    CategoryDao categoryDao;

    public List<CategoryInfo> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    public boolean deleteCategoryByIds(String ids) {
        String[] split = ids.split(",");
        int result = categoryDao.deleteCategoryByIds(split);
        System.out.println(result);
        return result == split.length;
    }

    public int updateCategoryById(CategoryInfo category) {
        return categoryDao.updateCategoryById(category);
    }

    public int addCategory(CategoryInfo category) {
        category.setDate(new DateTime(System.currentTimeMillis()));
        return categoryDao.addCategory(category);
    }
}
