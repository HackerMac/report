package com.report.dao;

import com.report.bean.TagsInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TagsDao {
    int deleteTagsByAid(Integer aid);

    int saveTags(@Param("tags") String[] tags);

    List<Integer> getTagsIdByTagName(@Param("tagNames") String[] tagNames);

    int saveTags2ArticleTags(@Param("tagIds") List<Integer> tagIds, @Param("aid") Integer aid);
}
