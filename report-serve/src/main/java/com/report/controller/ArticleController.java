package com.report.controller;

import com.report.bean.ArticleInfo;
import com.report.bean.RespInfo;
import com.report.service.ArticleService;
import com.report.utils.Util;
import org.apache.commons.io.IOUtils;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by mingku on 2020/11/20.
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    ArticleService articleService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public RespInfo addNewArticle(ArticleInfo article) {
        int result = articleService.addNewArticle(article);
        if (result == 1) {
            return new RespInfo("success", article.getId() + "");
        } else {
            return new RespInfo("error", article.getState() == 0 ? "文章保存失败!" : "文章发表失败!");
        }
    }


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Map<String, Object> getArticleByState(@RequestParam(value = "state", defaultValue = "-1") Integer state,
                                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                 @RequestParam(value = "count", defaultValue = "8") Integer count,
                                                 String keywords) {
        int totalCount = articleService.getArticleCountByState(state, Util.getCurrentUser().getId(),keywords);
        List<ArticleInfo> articles = articleService.getArticleByState(state, page, count,keywords);
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", totalCount);
        map.put("articles", articles);
        return map;
    }

    @RequestMapping(value = "/{aid}", method = RequestMethod.GET)
    public Map<String, Object> getArticleById(@PathVariable Integer aid) throws IOException {
        Map<String, Object> map = new HashMap<>();
        ArticleInfo articleInfo = articleService.getArticleById(aid);
        String image = articleService.crateQRCode(articleInfo.getMdContent());
        map.put("article", articleInfo);
        map.put("image", image);
        return map;
    }

    @RequestMapping(value = "/dustbin", method = RequestMethod.PUT)
    public RespInfo updateArticleState(Integer[] aids, Integer state) {
        if (articleService.updateArticleState(aids, state) == aids.length) {
            return new RespInfo("success", "删除成功!");
        }
        return new RespInfo("error", "删除失败!");
    }

    @RequestMapping(value = "/restore", method = RequestMethod.PUT)
    public RespInfo restoreArticle(Integer articleId) {
        if (articleService.restoreArticle(articleId) == 1) {
            return new RespInfo("success", "还原成功!");
        }
        return new RespInfo("error", "还原失败!");
    }

    @RequestMapping("/dataStatistics")
    public Map<String,Object> dataStatistics() {
        Map<String, Object> map = new HashMap<>();
        List<String> categories = articleService.getCategories();
        List<Integer> dataStatistics = articleService.getDataStatistics();
        map.put("categories", categories);
        map.put("ds", dataStatistics);
        return map;
    }

    /**
     * 上传图片
     *
     * @return 返回值为图片的地址
     */
    @RequestMapping(value = "/uploadimg", method = RequestMethod.POST)
    public RespInfo uploadImg(HttpServletRequest req, MultipartFile image) {
        StringBuffer url = new StringBuffer();
        String filePath = "/blogimg/" + sdf.format(new Date());
        String imgFolderPath = req.getServletContext().getRealPath(filePath);
        File imgFolder = new File(imgFolderPath);
        if (!imgFolder.exists()) {
            imgFolder.mkdirs();
        }
        url.append(req.getScheme())
                .append("://")
                .append(req.getServerName())
                .append(":")
                .append(req.getServerPort())
                .append(req.getContextPath())
                .append(filePath);
        String imgName = UUID.randomUUID() + "_" + image.getOriginalFilename().replaceAll(" ", "");
        try {
            IOUtils.write(image.getBytes(), new FileOutputStream(new File(imgFolder, imgName)));
            url.append("/").append(imgName);
            return new RespInfo("success", url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RespInfo("error", "上传失败!");
    }
}
