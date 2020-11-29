package com.report.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateTime;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.report.bean.ArticleInfo;
import com.report.dao.ArticleDao;
import com.report.dao.TagsDao;
import com.report.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by mingku on 2020/11/20.
 */
@Service
@Transactional
public class ArticleService {
    private static String imgPath = ResourceUtils.CLASSPATH_URL_PREFIX + "static/images/logo.jpg";

    @Autowired
    ArticleDao articleDao;

    @Autowired
    TagsDao tagsDao;

    /**
     * 添加文章
     * @return
     */
    public int addNewArticle(ArticleInfo article) {
        //处理文章摘要
        if (article.getSummary() == null || "".equals(article.getSummary())) {
            //直接截取
            String stripHtml = stripHtml(article.getHtmlContent());
            article.setSummary(stripHtml.substring(0, stripHtml.length() > 50 ? 50 : stripHtml.length()));
        }
        if (article.getId() == -1) {
            //添加操作
            //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            DateTime timestamp = new DateTime(System.currentTimeMillis());
            if (article.getState() == 1) {
                //设置发表日期
                article.setPublishDate(timestamp);
            }
            article.setEditTime(timestamp);
            //设置当前用户
            article.setUid(Util.getCurrentUser().getId());
            int i = articleDao.addNewArticle(article);
            //打标签
            String[] dynamicTags = article.getDynamicTags();
            if (dynamicTags != null && dynamicTags.length > 0) {
                int tags = addTagsToArticle(dynamicTags, article.getId());
                if (tags == -1) {
                    return tags;
                }
            }
            return i;
        } else {
            //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            DateTime timestamp = new DateTime(System.currentTimeMillis());
            if (article.getState() == 1) {
                //设置发表日期
                article.setPublishDate(timestamp);
            }
            //更新
            article.setEditTime(new DateTime(System.currentTimeMillis()));
            int i = articleDao.updateArticle(article);
            //修改标签
            String[] dynamicTags = article.getDynamicTags();
            if (dynamicTags != null && dynamicTags.length > 0) {
                int tags = addTagsToArticle(dynamicTags, article.getId());
                if (tags == -1) {
                    return tags;
                }
            }
            return i;
        }
    }

    /**
     * 给文章添加标签
     * @param dynamicTags
     * @param aid
     * @return
     */
    private int addTagsToArticle(String[] dynamicTags, Integer aid) {
        //1.删除该文章目前所有的标签
        tagsDao.deleteTagsByAid(aid);
        //2.将上传上来的标签全部存入数据库
        tagsDao.saveTags(dynamicTags);
        //3.查询这些标签的id
        List<Integer> tIds = tagsDao.getTagsIdByTagName(dynamicTags);
        System.out.println(tIds);
        //4.重新给文章设置标签
        int i = tagsDao.saveTags2ArticleTags(tIds, aid);
        return i == dynamicTags.length ? i : -1;
    }

    /**
     * 将文章内容变成html形式的输出
     * @param content
     * @return
     */
    public String stripHtml(String content) {
        content = content.replaceAll("<p .*?>", "");
        content = content.replaceAll("<br\\s*/?>", "");
        content = content.replaceAll("\\<.*?>", "");
        return content;
    }

    /**
     * 通过状态获取文章
     * @param state
     * @param count
     * @param page
     * @param keywords
     * @return
     */
    public List<ArticleInfo> getArticleByState(Integer state,
                                               Integer page, Integer count,
                                               String keywords) {
        int start = (page - 1) * count;
        Integer uid = Util.getCurrentUser().getId();
        return articleDao.getArticleByState(state, start, count, uid,keywords);
    }

    /**
     * 通过状态获取相同状态文章数量
     * @param state
     * @param uid
     * @param keywords
     * @return
     */
    public int getArticleCountByState(Integer state, Integer uid,String keywords) {
        return articleDao.getArticleCountByState(state, uid,keywords);
    }

    /**
     * 批量更新文章状态
     * @param aids
     * @param state
     * @return
     */
    public int updateArticleState(Integer[] aids, Integer state) {
        if (state == 2) {
            return articleDao.deleteArticleById(aids);
        } else {
            return articleDao.updateArticleState(aids, 2);//放入到回收站中
        }
    }

    /**
     * 更新单个文章状态(将文章从回收站拿回来)
     * @param articleId
     * @return
     */
    public int restoreArticle(Integer articleId) {
        return articleDao.updateArticleStateById(articleId, 1); // 从回收站还原在原处
    }


    /**
     * 通过id获取文章
     * @param aid
     * @return
     */
    public ArticleInfo getArticleById(Integer aid) {
        ArticleInfo article = articleDao.getArticleById(aid);
        //articleDao.pvIncrement(aid);
        return article;
    }

    public void pvStatisticsPerDay() {
        articleDao.pvStatisticsPerDay();
    }

    /**
     * 通过状态获取相同状态文章数量
     * @param content
     * @return
     */
    public String crateQRCode(String content) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        //BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB).setData();
        //BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\S2\\Desktop\\report - 副本\\report-serve\\src\\main\\resources\\static\\logo.jpg"))
        //        .getSubimage(250, 250, 250, 250);
        BufferedImage bufferedImage = ImageIO.read(new File("/root/softwave/logo.jpg"))
                .getSubimage(250, 250, 250, 250);
        try {
            bufferedImage = QrCodeUtil.generate(content, //二维码内容
                    QrConfig.create().setQrVersion(null)
                            //.setImg(ResourceUtils.CLASSPATH_URL_PREFIX + "static/images/logo.jpg") //附带logo
                            .setImg("/root/softwave/logo.jpg")
                            .setErrorCorrection(ErrorCorrectionLevel.H)
                            .setMargin(3)
            );
        } catch (Exception e) {
            System.out.println(e);
        }
        ImageIO.write(bufferedImage, "png", os);
        String resultImage = new String("data:image/png;base64," + Base64.encode(os.toByteArray()));
        return resultImage;
    }

    /**
     * 获取最近七天的日期
     * @return
     */
    public List<String> getCategories() {
        return articleDao.getCategories(Util.getCurrentUser().getId());
    }

    /**
     * 获取最近七天的数据
     * @return
     */
    public List<Integer> getDataStatistics() {
        return articleDao.getDataStatistics(Util.getCurrentUser().getId());
    }
}
