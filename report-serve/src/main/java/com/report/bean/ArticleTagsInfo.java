package com.report.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "t_article_tags")
public class ArticleTagsInfo implements Serializable {
    @Id
    @Field("_id")
    private ObjectId _id;

    @Field("id")
    private Integer id;
    private Integer aid;
    private Integer tid;
}
