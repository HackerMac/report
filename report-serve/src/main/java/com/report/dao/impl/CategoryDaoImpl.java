package com.report.dao.impl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.report.bean.ArticleInfo;
import com.report.bean.CategoryInfo;
import com.report.dao.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public class CategoryDaoImpl implements CategoryDao {
    @Autowired
    private MongoTemplate template;

    public CategoryDaoImpl() {
        super();
    }

    @Override
    public List<CategoryInfo> getAllCategories() {
        return template.findAll(CategoryInfo.class);
    }

    @Override
    public int deleteCategoryByIds(String[] ids) {
        int count = 0;
        for (String id : ids) {
            CategoryInfo categoryInfo = template.findAndRemove(Query.query(Criteria.where("id")
                    .is(Integer.valueOf(id))), CategoryInfo.class);
            if (categoryInfo != null) {
                count += 1;
            }
        }
        return count;
    }

    @Override
    public int updateCategoryById(CategoryInfo category) {
        Query query = Query.query(Criteria.where("id").is(category.getId()));
        Update update = new Update();
        update.set("cateName", category.getCateName());
        UpdateResult updateResult = template.updateFirst(query, update, CategoryInfo.class);
        if (updateResult.getMatchedCount() == 1) {
            return 1;
        }
        return 0;
    }

    @Override
    public int addCategory(CategoryInfo category) {
        CategoryInfo categoryInfo = new CategoryInfo();
        List<CategoryInfo> all = template.findAll(CategoryInfo.class);
        Integer count = 1;
        for (CategoryInfo a : all) {
            if (a.getId() != count) {
                category.setId(count);
                break;
            }
            count += 1;
        }
        if (category.getId() == null) category.setId(count);
        CategoryInfo result = template.save(category, "t_category");
        if (result != null) {
            return 1;
        }
        return 0;
    }
}
