package com.report.dao.impl;

import com.report.bean.ArticleTagsInfo;
import com.report.bean.TagsInfo;
import com.report.dao.TagsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagsDaoImpl implements TagsDao {
    @Autowired
    private MongoTemplate template;

    public TagsDaoImpl() {
        super();
    }

    @Override
    public int deleteTagsByAid(Integer aid) {
        template.remove(Query.query(Criteria.where("id").is(aid)), TagsInfo.class);
        return 0;
    }

    @Override
    public int saveTags(String[] tags) {
        TagsInfo tagsInfo = new TagsInfo();
        // 给标签打上id值 并记录保存标签数目
        int count = 0;
        for (String tag : tags) {
            tagsInfo.setTagName(tag);
            List<TagsInfo> all = template.findAll(TagsInfo.class);
            int tag_c = 1;
            for (TagsInfo a : all) {
                if (a.getId() != count) {
                    tagsInfo.setId(tag_c);
                    break;
                }
                tag_c += 1;
            }
            if (tagsInfo.getId() == null) tagsInfo.setId(tag_c);
            TagsInfo tagsInfo1 = template.save(tagsInfo, "t_tags");
            if (tagsInfo1 != null)
                count += 1;
        }
        return count;
    }

    @Override
    public List<Integer> getTagsIdByTagName(String[] tagNames) {
        List<Integer> TagName = new ArrayList<Integer>();
        for (String tagname : tagNames) {
            TagName.add(template.findOne(Query.query(Criteria.where("tagName").is(tagname)), TagsInfo.class).getId());
        }
        return TagName;
    }

    @Override
    public int saveTags2ArticleTags(List<Integer> tagIds, Integer aid) {
        ArticleTagsInfo articleTagsInfo = new ArticleTagsInfo();
        articleTagsInfo.setTid(aid);
        int count = 0;
        for (Integer tagid : tagIds) {
            articleTagsInfo.setTid(tagid);
            ArticleTagsInfo articleTagsInfo1 = template.save(articleTagsInfo, "t_article_tags");
            if (articleTagsInfo1 != null) {
                count += 1;
            }
        }
        return count;
    }
}
