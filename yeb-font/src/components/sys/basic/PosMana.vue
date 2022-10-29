<template>
<div>
    <div>
        <el-input
                class="addPosInput"
                size="small"
                @keydown.enter.native="addPosition"
                placeholder="添加职位"
                suffix-icon="el-icon-plus"
                v-model=pos.name>
        </el-input>
        <el-switch
                style="margin:0 10px"
                v-model="pos.enabled"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-text="启用"
                inactive-text="暂不启用">
        </el-switch>

        <el-button size="small" icon="el-icon-plus" type="primary" @click="addPosition">添加</el-button>
    </div>
    <div class="posManaMain">
        <el-table
                :data="positions"
                stripe
                border
                style="width: 100%;text-align: center"
                @selection-change="handleSelectionChange">>
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
                    label="职位"
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
                    width="150">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.enabled" type="success">已启用</el-tag>
                    <el-tag v-else type="danger">未启用</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            @click="showEditView(scope.$index, scope.row)">编辑</el-button>
                    <el-button
                            size="mini"
                            type="danger"
                            @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-button style="margin-top: 8px;margin-inside: 30px"
                   type="danger" size="small"
                   :disabled="this.multipleSelection.length === 0"
                   @click="deleteBatch">
                   批量删除
        </el-button>
    </div>
    <el-dialog
            title="职位编辑"
            :visible.sync="dialogVisible"
            width="30%">
           <div>
               <table>
                   <tr>
                       <td>
                           <el-tag>职位名称:</el-tag>
                       </td>
                       <td>
                           <el-input v-model="updatePos.name" size="small"
                                     class="updatePosInput"></el-input>
                       </td>
                   </tr>
                   <tr>
                       <td>
                           <el-tag>是否启用:</el-tag>
                       </td>
                       <td>
                           <el-switch
                                   style="margin-left: 10px"
                                   v-model="updatePos.enabled"
                                   active-color="#13ce66"
                                   inactive-color="#ff4949"
                                   active-text="启用"
                                   inactive-text="暂不启用">
                           </el-switch>
                       </td>
                   </tr>
               </table>
           </div>
            <span slot="footer" class="dialog-footer">
            <el-button size="small" @click="dialogVisible = false">取 消</el-button>
            <el-button size="small" type="primary" @click="doUpdate">确 定</el-button>
          </span>
    </el-dialog>

</div>
</template>

<script>
    export default {
        name: "PosMana",
        data(){
            return{
               pos:{
                   name:'',
                   enabled:'',
               },
                updatePos:{
                    name:'',
                    enabled:''
                },
                positions:[],
                dialogVisible:false,
                multipleSelection: []
            }
        },
        mounted() {
          this.initPositions();
        },
        methods:{
            deleteBatch(){
                this.$confirm('此操作将永久删除【'+this.multipleSelection.length+'】职位, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let ids=[];
                    this.multipleSelection.forEach(item=>{
                        // ids+='ids='+item.id+'&'
                        ids.push(item.id);
                    });
                    this.deleteRequest('/system/basic/pos/deleteBatch/?ids='+ids).then(resp=>{
                        console.log(ids);
                        if (resp){
                            this.initPositions();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });


            },
            handleSelectionChange(val){
                this.multipleSelection=val;
            },
            showEditView(index,data){
                // this.updatePos=data;
                //把data数据拷贝给updatePos回显到更新框
                Object.assign(this.updatePos,data);
                this.updatePos.createDate='';
                this.dialogVisible=true;
            },
            doUpdate(){
                this.postRequest('/system/basic/pos/update/',this.updatePos).then(resp=>{
                    if (resp){
                        this.initPositions();
                        this.updatePos.name='';
                        this.dialogVisible=false;
                    }
                })

            },
            handleDelete(index,data){
                this.$confirm('此操作将永久删除【'+data.name+'】职位, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                   this.deleteRequest('/system/basic/pos/delete/'+data.id).then(resp=>{
                       if (resp){
                           this.initPositions();
                       }
                   })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });

            },
            addPosition(){
              if (this.pos.name){
                  this.postRequest('/system/basic/pos/add/',this.pos).then(resp=>{
                      if (resp){
                         this.initPositions();
                         this.pos.name='';
                      }
                  })
              }else {
                  this.$message.error("添加职位不能为空")
              }
            },
            initPositions(){
                this.getRequest('/system/basic/pos/listAll').then(resp=>{
                    if (resp){
                        this.positions=resp;
                    }
                })

            }

        }
    }
</script>

<style scoped>
  .addPosInput{
        width: 300px;
        margin-right: 8px;
    }
    .posManaMain{
        margin-top: 10px;
    }
    .updatePosInput{
        width: 200px;
        margin-left: 8px;
    }

</style>
