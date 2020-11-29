<template>
  <div>
    <el-button type="primary" @click="qrCode()" style="margin-left: 20px"
      >生成二维码</el-button
    >
    <el-dialog title="手机观看" :visible.sync="dialogVisible" width="30%">
     <p style="color:red">注意！文本内容超过150字，生成二维码手机无法识别</p>
     <div class="qrCode">
       <img :src="codeUrl" class="login-code-img"/>
     </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false"
          >确 定</el-button
        >
      </span>
    </el-dialog>
    <el-row v-loading="loading">
      <el-col :span="24">
        <div style="text-align: left">
          <el-button
            type="text"
            icon="el-icon-back"
            @click="goBack"
            style="padding-bottom: 0px"
            >返回</el-button
          >
        </div>
      </el-col>
      <el-col :span="24">
        <div>
          <div>
            <h3 style="margin-top: 0px; margin-bottom: 0px">
              {{ article.title }}
            </h3>
          </div>
          <div
            style="
              width: 100%;
              margin-top: 5px;
              display: flex;
              justify-content: flex-end;
              align-items: center;
            "
          >
            <div
              style="
                display: inline;
                color: #20a0ff;
                margin-left: 50px;
                margin-right: 20px;
                font-size: 12px;
              "
            >
              {{ article.nickname }}
            </div>
            <span style="color: #20a0ff; margin-right: 20px; font-size: 12px"
              >浏览 {{ article.pageView == null ? 0 : article.pageView }}</span
            >
            <span style="color: #20a0ff; margin-right: 20px; font-size: 12px">
              {{ article.editTime | formatDateTime }}</span
            >
            <el-tag
              type="success"
              v-for="(item, index) in article.tags"
              :key="index"
              size="small"
              style="margin-left: 8px"
              >{{ item.tagName }}
            </el-tag>
            <span style="margin: 0px 50px 0px 0px"></span>
          </div>
        </div>
      </el-col>
      <el-col>
        <div style="text-align: left" v-html="article.htmlContent"></div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { getRequest } from "../utils/api";
import QRCode from "qrcodejs2";

export default {
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    qrCode() {
      // let link =
        // "https://rd.wechat.com/qrcode/confirm?block_type=101&content=" +
        // this.article.mdContent +
        // "&lang=zh_CN&scene=4";
      // console.log(link);
      this.dialogVisible = true;
      this.codeUrl = this.image;
      // console.log(this.radio,this.radio1);
      // this.$nextTick(function () {
        // let qrcode = new QRCode("qrcode", {
        //   width: 500,
        //   height: 500,
        //   text: link, // 二维码内容
        //   // url: 'https://rd.wechat.com/qrcode/confirm?block_type=101&content=' + this.article.mdContent + '&lang=zh_CN&scene=4',
        //   colorDark: "#109dff",
        //   colorLight: "#d9d9d9",
        //   correctLevel: QRCode.CorrectLevel.H,
        // });
        
      //   console.log(this.image);
      // });
      // this.dialogVisible = false;
    },
  },
  mounted: function () {
    var aid = this.$route.query.aid;
    this.activeName = this.$route.query.an;
    var _this = this;
    this.loading = true;
    getRequest("/article/" + aid).then(
      (resp) => {
        if (resp.status == 200) {
          _this.article = resp.data.article,
          _this.image = resp.data.image
        }
        _this.loading = false;
      },
      (resp) => {
        _this.loading = false;
        _this.$message({ type: "error", message: "页面加载失败!" });
      }
    );
  },
  data() {
    return {
      article: {},
      loading: false,
      activeName: "",
      dialogVisible: false
    };
  },
  data2() {
    return {
      dialogVisible: false,
      image: "",
    };
  },
  components: { QRCode },
};
</script>