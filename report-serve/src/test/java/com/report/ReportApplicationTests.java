package com.report;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.report.bean.ArticleInfo;
import com.report.bean.CategoryInfo;
import com.report.bean.RoleInfo;
import com.report.dao.CategoryDao;
import com.report.dao.RolesDao;
import com.report.dao.UserDao;
import com.report.service.ArticleService;
import com.report.service.CategoryService;
import com.report.service.UserService;
import com.sun.el.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

@Slf4j
@SpringBootTest
class ReportApplicationTests {
    @Autowired
    MongoTemplate template;

    @Autowired
    UserDao userDao;

    @Autowired
    RolesDao rolesDao;

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ArticleService articleService;


    @Autowired
    CategoryDao categoryDao;

    @Test
    void contextLoads() {

        //template.createCollection("report");
        // 时间测试
        //System.out.println(new DateTime(System.currentTimeMillis()));
        //System.out.println(template.findAll(CategoryInfo.class));

    }

    @Test
    void ArticleTagTest() {
        //ArticleInfo articleInfo = new ArticleInfo();
        //List<ArticleInfo> all = template.findAll(ArticleInfo.class);
        //Integer count = 1;
        //for (ArticleInfo a : all) {
        //    if (a.getId() != count) {
        //        articleInfo.setId(count);
        //    }
        //    count += 1;
        //}
        //if (articleInfo.getId() == null) {
        //    articleInfo.setId(count);
        //}
        ////System.out.println(iterator.);
        //ArticleInfo articleInfo = new ArticleInfo();
        //articleInfo.setEditTime(new DateTime(System.currentTimeMillis()));
        //articleInfo.setId(12);
        //articleInfo.setPublishDate(new DateTime(System.currentTimeMillis()));
        //articleInfo.setUid(1);
        //articleInfo.setCateName("公司报告");
        //articleInfo.setTitle("测试");
        //articleInfo.setHtmlContent("");
        //articleInfo.setMdContent("");
        //articleInfo.setNickname("洪铭凯");
        //articleInfo.setState(1);
        //int i = articleService.addNewArticle(articleInfo);
        //System.out.println(i);
    }

    @Test
    void UserTest() {
        //System.out.println(userService.getUserByNickname("洪铭凯"));
        //System.out.println(userService.loadUserByUsername("ming"));
    }

    @Test
    void TagTest() {

    }

    //@Test
    //void QrCodeTest() throws IOException {
    //    //QrConfig config = new QrConfig(300, 300);
    //    ////// 设置边距，既二维码和背景之间的边距
    //    ////config.setMargin(3);
    //    //config.setQrVersion(null);
    //    ////// 设置前景色，既二维码颜色（青色）
    //    ////config.setForeColor(Color.CYAN.getRGB());
    //    ////// 设置背景色（灰色）
    //    ////config.setBackColor(Color.GRAY.getRGB());
    //    ////
    //    ////// 生成二维码到文件，也可以到流
    //    //QrCodeUtil.generate("经验证，本报告真实有效！\n" +
    //    //        "报告防伪查询电话：028-86638807\n" +
    //    //        "报告编号：泰宇房评（2020）09291号\n" +
    //    //        "项目名称：高渐朋、王雪娟所属位于高新区桂溪路318号4栋2单元26层2603号住宅用途房地产抵押价值评估\n" +
    //    //        "估价机构：成都泰宇房地产资产评估有限责任公司\n" +
    //    //        "评估面积： 101.42 平方米\n" +
    //    //        "评估总价： 212.98 万元\n" +
    //    //        "估价报告出具日期： 2020年9月29日", config.setImg(ResourceUtils.CLASSPATH_URL_PREFIX + "templates/logo.jpg"),
    //    //        FileUtil.file("d:/qrcode.jpg"));
    //    //articleService.getQrCode("");
    //    ByteArrayOutputStream os = new ByteArrayOutputStream();
    //    BufferedImage bufferedImage = QrCodeUtil.generate("经验证，本报告真实有效！\n" +
    //                    "报告防伪查询电话：028-86638807\n" +
    //                    "报告编号：泰宇房评（2020）09291号\n" +
    //                    "项目名称：高渐朋、王雪娟所属位于高新区桂溪路318号4栋2单元26层2603号住宅用途房地产抵押价值评估\n" +
    //                    "估价机构：成都泰宇房地产资产评估有限责任公司\n" +
    //                    "评估面积： 101.42 平方米\n" +
    //                    "评估总价： 212.98 万元\n" +
    //                    "估价报告出具日期： 2020年9月29日", //二维码内容
    //            QrConfig.create().setQrVersion(null)
    //                    .setImg(ResourceUtils.CLASSPATH_URL_PREFIX + "templates/logo.jpg") //附带log
    //                    .setErrorCorrection(ErrorCorrectionLevel.H)
    //                    .setMargin(3)
    //    );
    //    ImageIO.write(bufferedImage, "png", os);
    //    String resultImage = new String("data:image/png;base64," + Base64.encode(os.toByteArray()));
    //    //return resultImage;
    //    System.out.println(resultImage);
    //}

}
