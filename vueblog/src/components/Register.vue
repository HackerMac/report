<template>
  <div id="register" v-title data-title="注册 - UeC报告管理系统">
    <!-- <video preload="auto" class="me-video-player" autoplay="autoplay" loop="loop">
          <source src="../../static/vedio/sea.mp4" type="video/mp4">
      </video> -->

    <div class="me-login-box me-login-box-radius">
      <h1>UeC 注册</h1>

      <el-form ref="loginForm" :model="loginForm" :rules="rules" v-loading="loading">
        <el-form-item prop="username">
          <el-input placeholder="用户名" v-model="loginForm.username"></el-input>
        </el-form-item>

        <el-form-item prop="nickname">
          <el-input placeholder="昵称" v-model="loginForm.nickname"></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input placeholder="密码" v-model="loginForm.password" type="password"></el-input>
        </el-form-item>

        <el-form-item prop="email">
          <el-input placeholder="邮箱" v-model="loginForm.email"></el-input>
        </el-form-item>

        <el-form-item size="small" class="me-login-button">
          <el-button type="primary" @click.native.prevent="submitClick">注册</el-button>
        </el-form-item>
      </el-form>

      <div class="me-login-design">
        <p>Designed by
          <strong>
            <router-link to="/register" class="me-login-design-color">UeC-Ming</router-link>
          </strong>
        </p>
      </div>
    </div>
  </div> 
</template>

<script>
  import {postRequest} from '../utils/api'
  import {putRequest} from '../utils/api'

  export default{
    data(){
      return {
        rules: {
          username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
          nickname: [{required: true, message: '请输入用户昵称', trigger: 'blur'}],
          checkPass: [{required: true, message: '请输入密码', trigger: 'blur'}],
          email: [{required: true, message: '请输入邮箱', trigger: 'blur'}]
        },
        checked: true,
          loginForm: {
          username: '',
          nickname: '',
          password: '',
          email: ''
        },
        loading: false
      }
    },

    methods: {
      submitClick: function () {
        var _this = this;
        this.loading = true;
        postRequest('/reg', {
          username: this.loginForm.username,
          nickname: this.loginForm.nickname,
          password: this.loginForm.password,
          email: this.loginForm.email
        }).then(resp=> {
          _this.loading = false;
          if (resp.status == 200) {
            //成功
            var json = resp.data;
            if (json.status == 'success') {
              _this.$alert('注册成功! 点击前往登录页面!', '成功');
              setTimeout(() => {
                  _this.$router.push({path: '/'});
              }, 3000);
            } else {
              _this.$alert('注册失败!', '失败!');
            }
          } else {
            //失败
            _this.$alert('注册失败!', '失败!');
          }
        }, resp=> {
          _this.loading = false;
          _this.$alert('找不到服务器⊙﹏⊙∥!', '失败!');
        });
      }
    }
  }
 </script>

<style scoped>
  #register {
    width: 100%;
    height: 100%;
  }
  #login {
    min-width: 100%;
    min-height: 100%;
  }

  .me-video-player {
    background-color: transparent;
    width: 100%;
    height: 100%;
    object-fit: fill;
    display: block;
    position: absolute;
    left: 0;
    z-index: 0;
    top: 0;
  }

  .me-login-box {
    position: absolute;
    width: 320px;
    height: 400px;
    /* background-color: yellowgreen; */
    background-image: url('../assets/1.jpg');
    background-size: cover;
    background-position: center center;
    margin-top: 150px;
    margin-left: -180px;
    left: 50%;
    padding: 30px;
  }

  .me-login-box-radius {
    border-radius: 10px;
    box-shadow: 0px 0px 1px 1px rgba(161, 159, 159, 0.1);
  }

  .me-login-box h1 {
    text-align: center;
    font-size: 24px;
    margin-bottom: 20px;
    vertical-align: middle;
  }

  .me-login-design {
    text-align: center;
    font-family: 'Open Sans', sans-serif;
    font-size: 18px;
  }

  .me-login-design-color {
    color: #5FB878 !important;
  }

  .me-login-button {
    text-align: center;
  }

  .me-login-button button {
    width: 100%;
  }

</style>