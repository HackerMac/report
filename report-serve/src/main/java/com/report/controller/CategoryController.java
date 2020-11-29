package com.report.controller;

import com.report.bean.CategoryInfo;
import com.report.bean.RespInfo;
import com.report.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 超级管理员专属Controller
 */
@RestController
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<CategoryInfo> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
    public RespInfo deleteById(@PathVariable String ids) {
        boolean result = categoryService.deleteCategoryByIds(ids);
        if (result) {
            return new RespInfo("success", "删除成功!");
        }
        return new RespInfo("error", "删除失败!");
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public RespInfo addNewCate(CategoryInfo category) {

        if ("".equals(category.getCateName()) || category.getCateName() == null) {
            return new RespInfo("error", "请输入栏目名称!");
        }

        int result = categoryService.addCategory(category);

        if (result == 1) {
            return new RespInfo("success", "添加成功!");
        }
        return new RespInfo("error", "添加失败!");
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public RespInfo updateCate(CategoryInfo category) {
        int i = categoryService.updateCategoryById(category);
        if (i == 1) {
            return new RespInfo("success", "修改成功!");
        }
        return new RespInfo("error", "修改失败!");
    }
}
