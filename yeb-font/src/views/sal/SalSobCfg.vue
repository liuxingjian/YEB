<template>
    <div>
        <el-table
                :data="emps"
                stripe
                size="mini"
                style="width: 100%">
            <el-table-column
                    type="selection"
                    align="left"
                    width="55">
            </el-table-column>
            <el-table-column
                    prop="name"
                    label="姓名"
                    align="center"
                    fixed
                    width="80">
            </el-table-column>
            <el-table-column
                    prop="workID"
                    label="工号"
                    align="center"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="email"
                    label="邮箱地址"
                    align="center"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="phone"
                    label="电话号码"
                    align="center"
                    width="130">
            </el-table-column>
            <el-table-column
                    prop="department.name"
                    label="所属部门"
                    align="center"
                    width="130">
            </el-table-column>
            <el-table-column
                    label="工资账套"
                    align="center">
                <template slot-scope="scope">
                    <el-tooltip placement="right" v-if="scope.row.salary">
                        <template #content>
                           <table>
                               <tr>
                                   <td>基本工资:</td>
                                   <td>{{scope.row.salary.basicSalary}}</td>
                               </tr>
                               <tr>
                                   <td>奖金:</td>
                                   <td>{{scope.row.salary.bonus}}</td>
                               </tr>
                               <tr>
                                   <td>午餐补助:</td>
                                   <td>{{scope.row.salary.lunchSalary}}</td>
                               </tr>
                               <tr>
                                   <td>交通补助:</td>
                                   <td>{{scope.row.salary.trafficSalary}}</td>
                               </tr>
                               <tr>
                                   <td>养老金比率:</td>
                                   <td>{{scope.row.salary.pensionBase}}</td>
                               </tr>
                               <tr>
                                   <td>养老金基数:</td>
                                   <td>{{scope.row.salary.pensionPer}}</td>
                               </tr>
                               <tr>
                                   <td>医疗保险基数:</td>
                                   <td>{{scope.row.salary.medicalBase}}</td>
                               </tr>
                               <tr>
                                   <td>医疗保险比率:</td>
                                   <td>{{scope.row.salary.medicalPer}}</td>
                               </tr>
                               <tr>
                                   <td>公积金基数:</td>
                                   <td>{{scope.row.salary.accumulationFundBase}}</td>
                               </tr>
                               <tr>
                                   <td>公积金:</td>
                                   <td>{{scope.row.salary.accumulationFundPer}}</td>
                               </tr>

                           </table>
                        </template>
                        <el-tag>{{scope.row.salary.name}}</el-tag>
                    </el-tooltip>
                    <el-tag v-else>暂未设置</el-tag>
                </template>
            </el-table-column>
            <el-table-column
                    label="操作"
                    align="center">
                <template slot-scope="scope">
                    <el-popover
                            placement="left"
                            title="编辑工资账套"
                            :width="200"
                            @show="showSalary(scope.row.salary)"
                            @hide="hideSalary(scope.row)"
                            trigger="click"
                    >
                        <div>
                            <el-select v-model="currentSalary" size="mini" placeholder="请选择">
                                <el-option
                                        v-for="item in salaries"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id"
                                ></el-option>
                            </el-select>
                        </div>
                        <el-button slot="reference" type="danger">修改工资账套</el-button>
                    </el-popover>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                style="display: flex;justify-content: flex-end"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[10, 20, 30, 40, 50, 100]"
                :page-size="size"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total">
        </el-pagination>
    </div>
</template>

<script>
    export default {
        name: "SalSobCfg",
        data(){
            return {
                emps: [],
                salaries:[],
                currentSalary: null,
                currentPage: 1,
                size: 10,
                total: 0,
            }
        },
        mounted() {
            this.initEmp()
            this.initSalary()
        },
        methods:{
            handleCurrentChange(currentPage) {
                this.currentPage = currentPage
                this.initEmp()
            },
            handleSizeChange(size) {
                this.size = size
                this.initEmp()
            },
            hideSalary(data){
                if (data.salary){
                    if (this.currentSalary !== data.salary.id){
                        this.postRequest('/salary/sobcfg/update/'+data.id+'/'+this.currentSalary)
                            .then(res =>{
                                if (res){
                                    this.initEmp()
                                }
                            })
                    }
                }else {
                    if (this.currentSalary){
                        this.postRequest('/salary/sobcfg/update/'+data.id+'/'+this.currentSalary)
                            .then(res =>{
                                if (res){
                                    this.initEmp()
                                }
                            })
                    }
                }

            },
            showSalary(sal){
                if (sal){
                    this.currentSalary = sal.id
                }else {
                    this.currentSalary = null
                }
            },
            initSalary(){
                this.getRequest('/salary/sob/listAll').then(res =>{
                    if (res){
                        this.salaries = res
                    }
                })
            },
            initEmp(){
                this.postRequest('/salary/sobcfg/listAll/?currentPage='+this.currentPage+'&size='+this.size)
                .then(res => {
                    if (res){
                        this.emps = res.data
                        this.total = res.total
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>
