<template>
    <div>
        <el-card class="box-card" style="width: 400px">
            <div slot="header" class="clearfix">
                <span>{{admin.name}}</span>
            </div>
            <div>
                <div style="display: flex;justify-content: center">
                    <el-upload
                            :headers="headers"
                            :data="admin"
                            :on-success="onSuccess"
                            :show-file-list="false"
                            action="system/admin/userFace">
                        <img style="width: 100px;height: 100px;border-radius: 50px"
                             title="点击修改用户头像"
                             :src="admin.userFace"
                             alt="">
                    </el-upload>
                </div>
                <div style="margin-top: 8px">电话号码: <el-tag>{{admin.telephone}}</el-tag> </div>
                <div style="margin-top: 8px">手机号码: <el-tag>{{admin.phone}}</el-tag> </div>
                <div style="margin-top: 8px">居住地址: <el-tag>{{admin.address}}</el-tag> </div>
                <div style="margin-top: 8px">用户标签: <el-tag type="success" v-for="(r,index) in admin.roles" :key="index">{{r.nameZh}}</el-tag> </div>
                <div style="display: flex;justify-content: space-between;margin-top: 10px">
                    <el-button size="mini" type="primary" @click="showUpdateAdminInfo">修改信息</el-button>
                    <el-button size="mini" type="danger" @click="showUpdateAdminPassWord">修改密码</el-button>
                </div>
            </div>
        </el-card>

        <el-dialog
                title="编辑用户信息"
                :visible.sync="dialogVisible"
                width="30%">
            <div>
                <el-form ref="form" :model="admin2" label-width="100px">
                    <el-form-item label="用户昵称">
                        <el-input v-model="admin2.name"></el-input>
                    </el-form-item>
                    <el-form-item label="电话号码">
                        <el-input v-model="admin2.telephone"></el-input>
                    </el-form-item>
                    <el-form-item label="手机号码">
                        <el-input v-model="admin2.phone"></el-input>
                    </el-form-item>
                    <el-form-item label="居住地址">
                        <el-input v-model="admin2.address"></el-input>
                    </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="updateAdminInfo">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog
                title="修改密码"
                :visible.sync="dialogVisible2"
                width="30%">
            <div>
                <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                    <el-form-item label="请输入旧密码" prop="oldPass">
                        <el-input type="password" v-model="ruleForm.oldPass" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="请输入新密码" prop="pass">
                        <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="请确认新密码" prop="checkPass">
                        <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
                        <el-button @click="resetForm('ruleForm')">重置</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "AdminInfo",
        data(){
            var validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    if (this.ruleForm.checkPass !== '') {
                        this.$refs.ruleForm.validateField('checkPass');
                    }
                    callback();
                }
            };
            var validatePass2 = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'));
                } else if (value !== this.ruleForm.pass) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            return {
                admin: null,
                admin2: null,
                headers: {
                    Authorization : window.sessionStorage.getItem('tokenStr')
                },
                dialogVisible: false,
                dialogVisible2: false,
                ruleForm: {
                    pass: '',
                    checkPass: '',
                    oldPass: ''
                },
                rules: {
                    pass: [
                        { validator: validatePass, trigger: 'blur' }
                    ],
                    checkPass: [
                        { validator: validatePass2, trigger: 'blur' }
                    ],
                    oldPass: [
                        { validator: validatePass, trigger: 'blur' }
                    ]
                }
            }
        },
        mounted() {
            this.initAdmin()
        },
        methods: {
            onSuccess(){
                this.initAdmin()
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.ruleForm.adminId = this.admin.id
                        this.postRequest('/system/admin/pass',this.ruleForm)
                        .then(res => {
                            if (res){
                                // 修改完密码  退出登录
                                this.postRequest('/logout')
                                window.sessionStorage.removeItem('user')
                                window.sessionStorage.removeItem('tokenStr')
                                this.$store.commit('initRoutes',[])
                                this.$router.replace('/')
                            }
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            showUpdateAdminPassWord(){
                this.dialogVisible2 = true
            },
            updateAdminInfo(){
                this.postRequest('/system/admin/info',this.admin2).then(res => {
                    if (res){
                        this.dialogVisible =false
                        this.initAdmin()
                    }
                })
            },
            showUpdateAdminInfo(){
                this.dialogVisible = true
            },
            initAdmin(){
                this.getRequest('/admin/info').then(res =>{
                    if (res){
                        this.admin = res
                        this.admin2 = Object.assign({},this.admin)
                        window.sessionStorage.setItem('user',JSON.stringify(res))
                        this.$store.commit('UNION_ADMIN',res)
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>