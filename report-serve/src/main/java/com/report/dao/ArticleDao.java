package com.report.dao;

import com.report.bean.ArticleInfo;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ArticleDao {
    int addNewArticle(ArticleInfo article);

    int updateArticle(ArticleInfo article);

    List<ArticleInfo> getArticleByState(@Param("state") Integer state, @Param("start") Integer start, @Param("count") Integer count, @Param("uid") Integer uid, @Param("keywords") String keywords);

//    List<Article> getArticleByStateByAdmin(@Param("start") int start, @Param("count") Integer count, @Param("keywords") String keywords);

    int getArticleCountByState(@Param("state") Integer state, @Param("uid") Integer uid, @Param("keywords") String keywords);

    int updateArticleState(@Param("aids") Integer aids[], @Param("state") Integer state);

    int updateArticleStateById(@Param("articleId") Integer articleId, @Param("state") Integer state);

    int deleteArticleById(@Param("aids") Integer[] aids);

    ArticleInfo getArticleById(Integer aid);

    void pvIncrement(Long aid);

    //INSERT INTO pv(countDate,pv,uid) SELECT NOW(),SUM(pageView),uid FROM article GROUP BY uid
    void pvStatisticsPerDay();

    List<String> getCategories(Integer uid);

    List<Integer> getDataStatistics(Integer uid);
}
