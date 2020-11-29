package com.report.dao;

import com.report.bean.CategoryInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CategoryDao {
    List<CategoryInfo> getAllCategories();

    int deleteCategoryByIds(@Param("ids") String[] ids);

    int updateCategoryById(CategoryInfo category);

    int addCategory(CategoryInfo category);
}
