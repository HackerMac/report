package com.report.bean;

import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "t_article")
public class ArticleInfo implements Serializable {
    @Id
    @Field("_id")
    private Integer id;
    private String title;
    private String mdContent;
    private String htmlContent;
    private String summary;
    private Integer cid;
    private Integer uid;
    private Integer state;
    private Integer pageView;
    private String[] dynamicTags;
    private String nickname;
    private String cateName;
    private List<TagsInfo> tags;
    private String stateStr;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private DateTime publishDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private DateTime editTime;
}
