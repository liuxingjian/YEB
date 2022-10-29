<template>
    <div>
        <div>
            <el-input
                    class="addJlInput"
                    size="small"
                    placeholder="添加职称"
                    suffix-icon="el-icon-plus"
                    v-model=jl.name>
            </el-input>
            <el-select size="small" v-model="jl.titleLevel" placeholder="职称等级">
                <el-option
                        v-for="item in titleLevels"
                        :key="item"
                        :label="item"
                        :value="item">
                </el-option>
            </el-select>
            <el-button size="small" type="primary" icon="el-icon-plus" style="margin-left: 10px" @click="addJobLevel">
                添加职称
            </el-button>
        </div>
        <div style="margin:10px 0px">
            <el-table
                    border
                    stripe
                    :data="jls"
                    style="width: 100%;text-align: center"
                    @selection-change="handleSelectionChange">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="id"
                        label="编号"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="职称"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="titleLevel"
                        label="职称等级"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="createDate"
                        label="创建时间"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="enabled"
                        label="是否启用"
                        width="120">
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.enabled" type="success">已启用</el-tag>
                        <el-tag v-else type="danger">未启用</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button
                                size="small"
                                @click="showEditView(scope.row)">编辑
                        </el-button>
                        <el-button
                                size="small"
                                type="danger"
                                @click="deleteHandler(scope.row)">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
             
            <el-button type="danger" size="small" style="margin-top: 10px"
                       :disabled="multipleSelection.length === 0"
                       @click="deleteBatch">批量删除
                           
            </el-button>
        </div>
        <el-dialog
                title="职称编辑"
                :visible.sync="dialogVisible"
                width="30%"
        >
            <div>
                <table>
                    <tr>
                        <td>
                            <el-tag>职称</el-tag>
                        </td>
                        <td>
                            <input v-model="updateJl.name"></input>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag>职称级别</el-tag>
                        </td>
                        <td>
                            <el-select size="small" v-model="updateJl.titleLevel" placeholder="职称等级">
                                <el-option
                                        v-for="item in titleLevels"
                                        :key="item"
                                        :label="item"
                                        :value="item">
                                </el-option>
                            </el-select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag>是否启用</el-tag>
                        </td>
                        <td>
                            <el-switch
                                    v-model="updateJl.enabled"
                                    active-color="#13ce66"
                                    inactive-color="#ff4949"
                                    active-text="启用"
                                    inactive-text="禁用">
                            </el-switch>
                        </td>
                    </tr>
                </table>
            </div>
            <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doUpdate">确 定</el-button>
  </span>
        </el-dialog>

    </div>
</template>

<script>
    export default {
        name: "JoblevelMana",
        data() {
            return {
                jl: {
                    name: '',
                    titleLevel: ''
                },
                updateJl: {
                    name: '',
                    titleLevel: '',
                    enabled: false
                },
                dialogVisible: false,
                multipleSelection: [],
                jls: [],
                titleLevels: [
                    '正高级',
                    '副高级',
                    '中级',
                    '初级',
                    '高级'
                ]
            }
        },
        mounted() {
            this.initJls();
        },
        methods: {
            deleteBatch() {
                this.$confirm('此操作将永久删除【' + this.multipleSelection.length + '】条职称, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    //把id放在ids数组里面
                    let ids = [];
                    this.multipleSelection.forEach(item => {
                        ids.push(item.id);
                    });
                    this.deleteRequest('/system/basic/joblevel/deleteBatch/?ids='+ids).then(resp => {
                        if (resp) {
                            this.initJls();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });

            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            doUpdate() {
                this.postRequest('/system/basic/joblevel/update/', this.updateJl).then(
                    resp => {
                        if (resp) {
                            this.initJls();
                            this.dialogVisible = false;
                        }
                    }
                )

            },
            addJobLevel() {
                if (this.jl.name && this.jl.titleLevel) {
                    this.postRequest('/system/basic/joblevel/add/', this.jl).then(resp => {
                        if (resp) {
                            this.initJls();
                        }
                    })
                } else {
                    this.$message.error('请输入职称名!')
                }
            },
            initJls() {
                this.getRequest('/system/basic/joblevel/listAll').then(resp => {
                    if (resp) {
                        this.jls = resp;
                        this.jl = {
                            name: '',
                            titleLevel: ''
                        }
                    }
                })
            },
            showEditView(data) {
                Object.assign(this.updateJl, data);
                this.updateJl.createDate = '';
                this.dialogVisible = true;
            },
            deleteHandler(data) {
                this.$confirm('此操作将永久删除【' + data.name + '】职称, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/system/basic/joblevel/delete/' + data.id).then(resp => {
                        if (resp) {
                            this.initJls();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });

            }
        }
    }
</script>

<style scoped>
    .addJlInput {
        width: 300px;
        margin-right: 10px;
    }

</style>
