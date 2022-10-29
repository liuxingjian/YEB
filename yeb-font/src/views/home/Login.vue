<template>
    <div>
        <el-form
                :rules="loginRule"
                ref="loginForm"
                :model="loginForm"
                class="loginStyle"
                v-loading="loading"
                element-loading-text="正在登陆中..."
                element-loading-spinner="el-icon-loading"
                element-loading-background="rgba(0, 0, 0, 0.8)"
        >
            <h3 class="loginTitle">系统登录</h3>
            <el-form-item  prop="username">
                <el-input type="text" v-model="loginForm.username" placeholder="请输入用户名"
                          auto-complete="false"></el-input>
            </el-form-item>
            <el-form-item  prop="password">
                <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"
                          auto-complete="false"></el-input>
            </el-form-item>
            <el-form-item  prop="code">
                <el-input style="width: 250px;margin-right: 5px"  type="text" v-model="loginForm.code" @keydown.enter.native="submitLogin" placeholder="点击图片获取验证码" auto-complete="false"></el-input>
                <img :src="captchaUrl" @click="updateCaptcha">
            </el-form-item>
            <el-checkbox v-model="checked" class="loginRemember">记住我</el-checkbox>
            <el-button type="primary" style="width: 100%" @click="submitLogin">登录</el-button>
        </el-form>
    </div>
</template>

<script>
    export default {
        name: "Login",
        data() {
            return {
                captchaUrl: '/captcha?time=' + new Date(),
                loginForm: {
                    username: '',
                    password: '',
                    code: ''
                },
                checked: true,
                loading: false,
                loginRule: {
                    username: [{required: true, message: '请输入用户名', trigger: blur}],
                    password: [{required: true, message: '请输入密码', trigger: blur}],
                    code: [{required: true, message: '请输入验证码', trigger: blur}],
                }
            }
        },
        methods: {
            updateCaptcha(){
                this.captchaUrl = '/captcha?time=' + new Date();
            },
            submitLogin() {
                this.$refs.loginForm.validate((valid) => {
                    if (valid) {
                        this.loading = true
                        this.postRequest('/login',this.loginForm).then(res=>{
                           if (res){
                               this.loading = false
                               //存储用户token
                               const tokenStr = res.obj.tokenHead + res.obj.token;
                               window.sessionStorage.setItem('tokenStr',tokenStr);

                               //跳转页面
                               let path = this.$route.query.redirect;
                               this.$router.replace(
                                   (path === '/' || path === undefined) ? '/home' : path);
                           }else {
                               this.loading = false
                               this.$router.replace('/')
                               this.updateCaptcha();
                           }
                        })
                    } else {
                        this.$message.error('请完善表单信息');
                        return false;
                    }
                });
            }
        }

    }
</script>

<style>
    .loginStyle {
        border-radius: 15px;
        background-clip: padding-box;
        margin: 180px auto;
        width: 350px;
        padding: 15px 35px 15px 35px;
        background: white;
        border: 1px solid #eaeaea;
        box-shadow: 0 0 25px #cac6c6;
    }
    .loginTitle {
        text-align: center;
    }
    .loginRemember {
        text-align: left;
        margin: 0px 0px 15px 0px;
    }
    .el-form-item__content{
        display: flex;
        align-items: center;
    }

</style>
