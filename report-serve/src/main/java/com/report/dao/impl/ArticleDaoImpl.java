package com.report.dao.impl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.report.bean.*;
import com.report.dao.ArticleDao;
import com.report.dao.CategoryDao;
import com.report.dao.UserDao;
import com.report.service.CategoryService;
import com.report.utils.Util;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;

@Component
public class ArticleDaoImpl implements ArticleDao {
    @Autowired
    private MongoTemplate template;

    @Autowired
    CategoryService categoryService;


    public ArticleDaoImpl() {
        super();
    }

    // 通过
    @Override
    public int addNewArticle(ArticleInfo article) {
        // 避免id字段重复
        List<ArticleInfo> all = template.findAll(ArticleInfo.class);
        // 对文章id排序，防止id
        Collections.sort(all,new Comparator<ArticleInfo>() {
            @Override
            public int compare(ArticleInfo a, ArticleInfo b) {
                return a.getId() - b.getId();
            }
        });
        Integer count = 1;
        Boolean flg = false;
        // 防止中途id离散分布
        for (ArticleInfo a : all) {
            if (a.getId() != count) {
                article.setId(count);
                flg = true;
                break;
            }
            count += 1;
        }
        if (!flg) article.setId(count);
        article.setNickname(Util.getCurrentUser().getNickname());
        article.setUid(Util.getCurrentUser().getId());
        List<CategoryInfo> categoryInfos = categoryService.getAllCategories();
        for (CategoryInfo categoryInfo : categoryInfos) {
            if (categoryInfo.getId() == article.getCid()) {
                article.setCateName(categoryInfo.getCateName());
                break;
            }
        }
        ArticleInfo articleInfo = template.save(article, "t_article");
        if (articleInfo != null) {
            return 1;
        }
        return 0;
    }

    //
    @Override
    public int updateArticle(ArticleInfo article) {
        Query query = new Query(Criteria.where("_id").is(article.getId()));
        ArticleInfo articleInfo = template.findOne(query, ArticleInfo.class);
        Update update = new Update();
        if (articleInfo.getState() == 1)  {
            update.set("state", 1);
        }
        if (articleInfo.getEditTime() != null) {
            update.set("editTime", article.getEditTime());
        }
        update.set("title", article.getTitle());
        update.set("mdContent", article.getMdContent());
        update.set("htmlContent", article.getHtmlContent());
        update.set("summary", article.getSummary());
        update.set("cid", article.getCid());
        UpdateResult updateResult = template.updateFirst(query, update, ArticleInfo.class);
        //System.out.println(updateResult);
        if (updateResult.getMatchedCount() == 1)
            return 1;
        return 0;
    }

    @Override
    public List<ArticleInfo> getArticleByState(Integer state,
                                               Integer start, Integer count,
                                               Integer uid, String keywords) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        Criteria criteria1 = new Criteria();
        Criteria criteria2 = new Criteria();
        Criteria criteria3 = new Criteria();

        if (state != -1 && state != -2) {
            criteria1 = Criteria.where("state").is(state);
        }
        if (state == -2) {
            criteria1 = Criteria.where("state").is(1);
        }
        if (state != -2 && state != -1) {
            criteria3 = Criteria.where("uid").is(uid);
        }

        if (keywords != null) {
            criteria2 = Criteria.where("title").regex(keywords);
        }

        criteria = new Criteria().andOperator(criteria1, criteria2, criteria3);
        query.addCriteria(criteria).with(Sort.by("editTime").descending())
                .skip(start).limit(count);
        System.out.println(query);
        List<ArticleInfo> ts = template.find(query, ArticleInfo.class);
        return ts;
    }

    @Override
    public int getArticleCountByState(Integer state, Integer uid, String keywords) {
        Query query = new Query();
        Criteria criteria1 = new Criteria();
        Criteria criteria2 = new Criteria();
        Criteria criteria3 = new Criteria();
        //Update update = new Update();
        if (state != -1) {
            criteria1 = Criteria.where("state").is(state);
        }
        if (uid != null) {
            criteria2 = Criteria.where("uid").is(uid);
        }
        if (keywords != null) {
            criteria3 = Criteria.where("title").regex(keywords);
        }
        if (criteria2 == null) {
            query.addCriteria(new Criteria().orOperator(criteria1, criteria3));
        }
        else {
            query.addCriteria(new Criteria().orOperator(criteria1,criteria2,criteria3));
        }

        List<ArticleInfo> ts = template.find(query, ArticleInfo.class);
        return ts.size();
    }


    @Override
    public int updateArticleState(Integer[] aids, Integer state) {
        Update update = new Update();
        int count = 0;
        System.out.println(aids);
        for (Integer aid : aids) {
            Query query = new Query(Criteria.where("_id").is(aid));
            System.out.println(template.findOne(query, ArticleInfo.class));
            update.set("state", state);
            UpdateResult updateResult = template.updateFirst(query, update, ArticleInfo.class);
            if (updateResult.getMatchedCount() != 0) {
                count += 1;
            }
        }
        return count;
    }

    @Override
    public int updateArticleStateById(Integer articleId, Integer state) {
        Query query = new Query(Criteria.where("_id").is(articleId));
        Update update = new Update();
        update.set("state", state);
        UpdateResult updateResult = template.updateFirst(query, update, ArticleInfo.class);
        if (updateResult.getMatchedCount() == 1) {
            return 1;
        }
        return 0;
    }

    @Override
    public int deleteArticleById(Integer[] aids) {
        long count = 0;
        for (Integer aid : aids) {
            DeleteResult result = template.remove(Query.query(Criteria.where("_id").is(aid)),
                    ArticleInfo.class);
            if (result.getDeletedCount() != 0) {
                count += 1;
            }
        }
        //System.out.println(count);
        return count == aids.length ? 1 : 0;
    }

    @Override
    public ArticleInfo getArticleById(Integer aid) {
        return template.findOne(Query.query(Criteria.where("_id").is(aid)), ArticleInfo.class);
    }

    @Override
    public void pvIncrement(Long aid) {
        Query query = Query.query(Criteria.where("id").is(aid));
        PvInfo pvInfo = template.findOne(query, PvInfo.class);
        pvInfo.setPv(pvInfo.getPv() + 1);
        Update update = new Update();
        update.set("pv", pvInfo.getPv());
        template.updateFirst(query, update, PvInfo.class);
    }

    @Override
    public void pvStatisticsPerDay() {

    }

    @Override
    public List<String> getCategories(Integer uid) {
        Query query = new Query();
        Criteria criteria = Criteria.where("uid").is(uid);
        query.addCriteria(criteria);
        List<PvInfo> pvInfos = template.find(query, PvInfo.class);
        int size = pvInfos.size();
        query.skip((size - 1) * 7);
        query.limit(7);
        query.with(Sort.by(
                Sort.Order.asc("countDate")
        ));
        pvInfos = template.find(query, PvInfo.class);
        List<String> countDate = new ArrayList<String>();
        for (PvInfo pvInfo : pvInfos) {
            countDate.add(pvInfo.getCountDate().toString());
        }
        return countDate;
    }

    @Override
    public List<Integer> getDataStatistics(Integer uid) {
        Query query = new Query();
        Criteria criteria = Criteria.where("uid").is(uid);
        query.addCriteria(criteria);
        List<PvInfo> pvInfos = template.find(query, PvInfo.class);
        int size = pvInfos.size();
        query.skip((size - 1) * 7);
        query.limit(7);
        query.with(Sort.by(
                Sort.Order.asc("countDate")
        ));
        pvInfos = template.find(query, PvInfo.class);
        List<Integer> countDate = new ArrayList<Integer>();
        for (PvInfo pvInfo : pvInfos) {
            countDate.add(pvInfo.getPv());
        }
        return countDate;
    }
}
