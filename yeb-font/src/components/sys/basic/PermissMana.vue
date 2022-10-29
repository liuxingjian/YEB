<template>
    <div>
        <div class="permissionManaTool">
            <el-input size="small" placeholder="请输入英文名" v-model="role.name" @keydown.enter.native="addRole">
                <template slot="prepend">ROLE_</template>
            </el-input>
            <el-input size="small" v-model="role.nameZh" placeholder="请输入中文名" @keydown.enter.native="addRole"></el-input>
            <el-button size="small" type="primary" icon="el-icon-plus" @click="addRole">添加角色</el-button>
        </div>
        <div class="permissionManaMain">
            <el-collapse accordion v-model="activeName" @change="change">
                <el-collapse-item v-for="(item,index) in roles" :key="index" :title="item.nameZh" :name="item.id">
                    <el-card class="box-card">
                        <div slot="header" class="clearfix">
                            <span>可访问资源</span>
                            <el-button
                                    style="float: right; padding: 3px 0;color: red"
                                    icon="el-icon-delete"
                                    type="text"
                                    @click="deleteRole(item)"
                            ></el-button>
                        </div>
                        <div>
                            <el-tree
                                    show-checkbox
                                    ref="tree"
                                    :data="menus"
                                    :key="index"
                                    :props="defaultProps"
                                    :default-checked-keys="selectedMenus"
                                    node-key="id"
                            ></el-tree>
                            <div style="display: flex;justify-content: flex-end">
                                <el-button size="mini" @click="cancelUpdate">取消修改</el-button>
                                <el-button size="mini" type="primary" @click="doUpdate(item.id,index)">确认修改</el-button>
                            </div>
                        </div>
                    </el-card>
                </el-collapse-item>
            </el-collapse>
        </div>
    </div>
</template>

<script>
    export default {
        name: "PermissMana",
        data(){
            return {
               role: {
                   name: '',
                   nameZh: ''
               },
                roles: [],
                menus: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                selectedMenus: [],
                activeName: -1
            }
        },
        mounted() {
            this.initRoles();
        },
        methods: {
            deleteRole(role){
                this.$confirm('此操作将永久删除【'+role.nameZh+'】角色, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/system/basic/permiss//delete/'+role.id).then(resp=>{
                        if (resp){
                            this.initRoles();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            addRole(){
                if (this.role.name && this.role.nameZh){
                    this.postRequest('/system/basic/permiss/add/',this.role).then(resp=>{
                        if (resp){
                            this.initRoles();
                            this.role = {};
                        }
                    })
                }else {
                    this.$message.error("所有内容不能为空！")
                }
            },
            cancelUpdate(){
                this.activeName = -1
            },
            doUpdate(rid,index){
                let tree = this.$refs.tree[index]
                let selectedKeys = tree.getCheckedKeys(true)
                // console.log(selectedKeys)
                let url = '/system/basic/permiss/updateMenuRole/?rid=' + rid + '&mid=' + selectedKeys
                // selectedKeys.forEach(key => {
                //     url += '&mids=' + key
                // })
                this.putRequest(url).then(res => {
                    if (res){
                        this.initSelectedMenus(rid);
                        this.activeName = -1;
                    }
                })
            },
            initSelectedMenus(rid){
                this.getRequest('/system/basic/permiss/mid/'+rid).then(res =>{
                    if (res){
                        this.selectedMenus = res
                    }
                })
            },
            change(rid){
                if (rid){
                    this.initMenus();
                    this.initSelectedMenus(rid);
                }
            },
            initMenus(){
                this.getRequest('/system/basic/permiss/menus').then(res =>{
                    if (res){
                        this.menus = res
                    }
                })
            },
            initRoles(){
                this.getRequest('/system/basic/permiss/listAll').then(res =>{
                    if (res){
                        this.roles = res
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .permissionManaTool{
        display: flex;
        justify-content: flex-start;
    }
    .permissionManaTool .el-input{
        width: 300px;
        margin-right: 6px;
    }
    .permissionManaMain{
        width: 700px;
        margin-top: 10px;
    }
</style>
