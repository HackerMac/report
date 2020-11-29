package com.report.controller.admin;

import com.report.bean.ArticleInfo;
import com.report.bean.RespInfo;
import com.report.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 超级管理员专属Controller
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    ArticleService articleService;

    /**
     * 获取回收站中的文章
     * @param page
     * @param count
     * @param keywords
     * @return
     */
    @RequestMapping(value = "/article/all", method = RequestMethod.GET)
    public Map<String, Object> getArticleByStateByAdmin(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                        @RequestParam(value = "count", defaultValue = "8") Integer count,
                                                        String keywords) {
        // 获取回收站中的文章
        List<ArticleInfo> articles = articleService.getArticleByState(-2, page, count, keywords);
        // 将回收站的文章放入map中，将未放入回收站的文章总数放入map中
        Map<String, Object> map = new HashMap<>();
        map.put("articles", articles);
        map.put("totalCount", articleService.getArticleCountByState(1, null, keywords));
        return map;
    }

    /**
     * 批量更新文章状态
     * @param aids
     * @param state
     * @return
     */
    @RequestMapping(value = "/article/dustbin", method = RequestMethod.PUT)
    public RespInfo updateArticleState(Integer[] aids, Integer state) {
        if (articleService.updateArticleState(aids, state) == aids.length) {
            return new RespInfo("success", "删除成功!");
        }
        return new RespInfo("error", "删除失败!");
    }
}
