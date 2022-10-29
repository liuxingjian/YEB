<template>
    <div style="width: 500px">
        <el-input
                prefix-icon="el-icon-search"
                placeholder="输入关键字进行部门过滤..."
                v-model="filterText">
        </el-input>

        <el-tree
                :data="deptData"
                :props="defaultProps"
                default-expand-all
                :filter-node-method="filterNode"
                :expand-on-click-node="false"
                ref="tree">
            <span class="custom-tree-node"
                  slot-scope="{ node, data }"
                  style="display: flex;justify-content: space-between;width: 100%">
                <span>{{ data.name }}</span>
                <span>
                      <el-button
                              class="btnStyle"
                              type="primary"
                              size="mini"
                              @click="() => showAdd(data)">
                        添加部门
                      </el-button>
                      <el-button
                              class="btnStyle"
                              type="danger"
                              size="mini"
                              @click="() => deleteDept(data)">
                        删除部门
                      </el-button>
                    </span>
            </span>
        </el-tree>
        <el-dialog
                title="添加部门"
                :visible.sync="dialogVisible"
                width="30%">
            <div>
                <table>
                    <tr>
                        <td><el-tag>上级部门</el-tag></td>
                        <td>{{pName}}</td>
                    </tr>
                    <tr>
                        <td><el-tag>部门名称</el-tag></td>
                        <td><el-input v-model="dept.name" placeholder="请输入部门名称..."></el-input></td>
                    </tr>
                </table>
            </div>
            <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="addDept">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "DepMana",
        data() {
            return {
                filterText: '',
                deptData: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                dialogVisible: false,
                dept: {
                    name: '',
                    parentId: -1
                },
                pName: ''
            }
        },
        watch: {
            filterText(val) {
                this.$refs.tree.filter(val);
            }
        },
        mounted() {
            this.initDept();
        },
        methods: {
            showAdd(dept){
                this.dept.parentId = dept.id
                this.pName = dept.name
                this.dialogVisible = true
            },
            addDept(){
                this.postRequest('/system/basic/department/add',this.dept).then(res => {
                    if (res) {
                        this.initDept();
                        this.dept = {};
                        this.pName= '';
                        this.dialogVisible = false
                    }
                });
            },
            deleteDept(dept){
                this.$confirm('此操作将永久删除【'+dept.name+'】部门, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/system/basic/department/delete/'+dept.id).then(resp=>{
                        if (resp){
                            this.initDept();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            filterNode(value, data) {
                if (!value) return true;
                return data.name.indexOf(value.trim()) !== -1;
            },
            initDept() {
                this.getRequest('/system/basic/department/listAll').then(res => {
                    if (res) {
                        this.deptData = res
                    }
                });
            },
        }
    }
</script>

<style scoped>
    .btnStyle{
        padding: 2px;
    }
</style>
