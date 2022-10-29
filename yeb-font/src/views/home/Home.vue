<template>
    <el-container>
        <el-header class="homeHeader">
            <div class="title" align="center">在线办公平台</div>
            <div>
                <el-button
                        @click="goChat"
                        size="normal"
                        style="color: black;margin-right: 8px"
                        icon="el-icon-bell"
                        type="text">
                </el-button>
                <el-dropdown class="userInfo" @command="commandHandler">
                      <span class="el-dropdown-link">
                        {{user.name}}<i><img :src="user.userFace"></i>
                      </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="userInfo">个人中心</el-dropdown-item>
                        <el-dropdown-item command="setting">设置</el-dropdown-item>
                        <el-dropdown-item command="logout">注销登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
        </el-header>
        <el-container>
            <el-aside width="200px">
                <el-menu router unique-opened>
                    <el-submenu :index="index+''"
                                v-for="(item,index) in routes"
                                :key="index"
                                v-if="!item.hidden">
                        <template slot="title">
                            <i :class="item.iconCls" style="color: #7f81ff;margin-right: 5px"></i>
                            <span>{{item.name}}</span>
                        </template>
                            <el-menu-item
                                    :key="item.id"
                                    :index="children.path"
                                    v-for="(children,index) in item.children">
                                    {{children.name}}
                            </el-menu-item>
                    </el-submenu>
                </el-menu>
            </el-aside>
            <el-main>
                <el-breadcrumb separator-class="el-icon-arrow-right"  v-if="this.$router.currentRoute.path!='/home'">
                    <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                    <el-breadcrumb-item>{{this.$router.currentRoute.name}}</el-breadcrumb-item>
                </el-breadcrumb>
                <div v-else class="homeWelcome" >
                    欢迎来到云E办系统
                </div>
                <router-view class="homeRouterView"/>
            </el-main>
        </el-container>
    </el-container>
</template>

<script>
    export default {
        name: "Home",
        data(){
            return {
                // user:JSON.parse(window.sessionStorage.getItem('user'))
            }
        },
        computed:{
            routes(){
                return this.$store.state.routes;
            },
            user(){
                return this.$store.state.currentAdmin;
            }
        },
        methods:{
            goChat(){
                this.$router.push('/chat')
            },
            commandHandler(command){
                // debugger
                if (command === "logout"){
                    this.$confirm('此操作将注销登录, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        //注销登录
                        this.getRequest('/logout');
                        //清空菜单
                        this.$store.commit('initRoutes',[]);
                        //清除token和用户信息
                        window.sessionStorage.removeItem('tokenStr');
                        window.sessionStorage.removeItem('user');
                        this.$router.replace("/")
                    }).catch(() => {
                        this.$message({
                            type: 'warning',
                            message: '小哥哥，您还不想注销哟！'
                        });
                    });

                }

                if (command === 'userInfo'){
                    this.$router.push('/userInfo')
                }

            }
        }
    }
</script>

<style scoped>
    .homeHeader{
        background: cornflowerblue ;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0px 15px;
        box-sizing: border-box;
    }
    .homeHeader .title{
        font-size: 40px;
        font-family:华文行楷;
        color: white;
    }
    .homeHeader .userInfo{
        cursor: pointer;
    }
    .el-dropdown-link img{
        width: 40px;
        height: 40px;
        border-radius: 24px;
        margin-left: 8px;
    }
    .homeWelcome{
        text-align: center;
        font-size: 40px;
        font-family: 华文行楷;
        color: #409eff;
        padding-top: 200px;
    }
    .homeRouterView{
        margin-top: 20px;
        text-align: left;
    }

</style>
