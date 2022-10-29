<template>
    <div>
        <div>
            <div style="display: flex;justify-content: space-between">
                <div>
                    <el-input clearable @clear="initEmpData"
                              style="width: 300px;margin-right: 10px"
                              type="text"
                              v-model="empName"
                              @keydown.enter.native="initEmpData"
                              placeholder="请输入员工名进行搜索..."
                              prefix-icon="el-icon-search"></el-input>
                    <el-button type="primary" icon="el-icon-search" size="small" @click="initEmpData">搜索</el-button>
                    <el-button type="primary" size="small" @click="showAdvanceSearch = !showAdvanceSearch">
                        <i :class="showAdvanceSearch ? 'fa fa-angle-double-up' : 'fa fa-angle-double-down'"
                           style="margin-right: 5px" aria-hidden="true"></i>高级搜索</el-button>
                </div>
                <div>
                    <el-upload action="/employee/basic/import"
                               style="display: inline-block;margin-right: 8px"
                               :before-upload="beforeUpload"
                               :disabled="importDataDisabled"
                               :headers="headers"
                               :show-file-list="false"
                               :on-success="onSuccess"
                               :on-error="onError">
                        <el-button type="success" size="small" :icon="importDataIcon" :disabled="importDataDisabled">{{importDataBtn}}</el-button>
                    </el-upload>
                    <el-button type="success" size="small" icon="el-icon-download" @click="exportEmp">导出数据</el-button>
                    <el-button type="primary" icon="el-icon-plus" size="small" @click="showAddEmp">添加员工</el-button>
                </div>
            </div>
        </div>
        <el-collapse-transition>
            <div v-show="showAdvanceSearch" class="searchStyle">
                <el-row>
                    <el-col :span="5">
                        政治面貌:
                        <el-select v-model="empSearch.politicId" size="mini" style="width: 130px" placeholder="政治面貌">
                            <el-option
                                    v-for="item in politicsStatus"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-col>
                    <el-col :span="4">
                        民族:
                        <el-select v-model="empSearch.nationId" size="mini" style="width: 130px" placeholder="民族">
                            <el-option
                                    v-for="item in nations"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-col>
                    <el-col :span="4">
                        职位:
                        <el-select v-model="empSearch.posId" size="mini" style="width: 150px" placeholder="职位">
                            <el-option
                                    v-for="item in positions"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-col>
                    <el-col :span="4">
                        职称:
                        <el-select v-model="empSearch.jobLevelId" size="mini" style="width: 150px" placeholder="职称">
                            <el-option
                                    v-for="item in jobLevels"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-col>
                    <el-col :span="7">
                        聘用形式:
                        <el-radio-group v-model="empSearch.engageForm">
                            <el-radio label="劳动合同">劳动合同</el-radio>
                            <el-radio label="劳务合同">劳务合同</el-radio>
                        </el-radio-group>
                    </el-col>
                </el-row>
                <el-row style="margin-top: 10px">
                    <el-col :span="5">
                        所属部门:
                        <el-popover
                                placement="bottom"
                                title="请选择部门"
                                width="260"
                                trigger="manual"
                                v-model="visible2"
                        >
                            <el-tree :data="allDeps"
                                     :props="defaultProps"
                                     default-expand-all
                                     @node-click="handleNodeClick2"></el-tree>
                            <div slot="reference" class="deptSearch" @click="showDeptView">{{deptName}}</div>
                        </el-popover>
                    </el-col>
                    <el-col :span="10">
                        入职日期:
                        <el-date-picker
                                v-model="empSearch.beginDateScope"
                                type="daterange"
                                unlink-panels
                                value-format="yyyy-MM-dd"
                                size="mini"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期">
                        </el-date-picker>
                    </el-col>
                    <el-col :span="5" :offset="4">
                        <el-button size="mini">取消</el-button>
                        <el-button size="mini" icon="el-icon-search" type="primary" @click="initEmpData('advance')">搜索</el-button>
                    </el-col>
                </el-row>
            </div>
        </el-collapse-transition>
        <div style="margin-top: 10px">
            <el-table
                    :data="empData"
                    show-header
                    height="500"
                    border
                    v-loading="loading"
                    element-loading-text="数据加载中..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    style="width: 100%">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="name"
                        fixed
                        label="姓名"
                        align="center"
                        width="70">
                </el-table-column>
                <el-table-column
                        prop="workID"
                        label="工号"
                        align="center"
                        width="85">
                </el-table-column>
                <el-table-column
                        prop="gender"
                        label="性别"
                        align="center"
                        width="50">
                </el-table-column>
                <el-table-column
                        prop="birthday"
                        label="出生日期"
                        align="left"
                        width="95">
                </el-table-column>
                <el-table-column
                        prop="idCard"
                        label="身份证号"
                        align="left"
                        width="170">
                </el-table-column>
                <el-table-column
                        prop="wedlock"
                        label="婚姻状况"
                        align="center"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="nation.name"
                        label="民族"
                        align="center"
                        width="50">
                </el-table-column>
                <el-table-column
                        prop="nativePlace"
                        label="籍贯"
                        align="center"
                        width="80">
                </el-table-column>
                <el-table-column
                        prop="politicsStatus.name"
                        label="政治面貌"
                        align="center"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="email"
                        label="邮箱"
                        align="center"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="phone"
                        label="电话号码"
                        align="center"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="address"
                        label="联系地址"
                        align="center"
                        width="300">
                </el-table-column>
                <el-table-column
                        prop="department.name"
                        label="所属部门"
                        align="center"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="joblevel.name"
                        label="职称"
                        align="center"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="position.name"
                        label="职位"
                        align="center"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="engageForm"
                        label="聘用形式"
                        align="center"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="tiptopDegree"
                        label="最高学历"
                        align="center"
                        width="80">
                </el-table-column>
                <el-table-column
                        prop="workState"
                        label="毕业院校"
                        align="center"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="beginDate"
                        label="入职日期"
                        align="center"
                        width="95">
                </el-table-column>
                <el-table-column
                        prop="conversionTime"
                        label="转正日期"
                        align="center"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="beginContract"
                        label="合同起始日期"
                        align="center"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="endContract"
                        label="合同截止日期"
                        align="center"
                        width="120">
                </el-table-column>
                <el-table-column
                        label="合同期限"
                        align="center"
                        width="100">
                    <template slot-scope="scope">
                        <el-tag>{{scope.row.contractTerm}}</el-tag>
                        年
                    </template>
                </el-table-column>
                <el-table-column
                        label="操作"
                        fixed="right"
                        align="center"
                        width="240">
                    <template slot-scope="scope">
                        <el-button style="padding: 3px" type="primary" icon="el-icon-edit" size="mini" @click="showEditView(scope.row)">编辑</el-button>
                        <el-button style="padding: 3px" type="info" icon="el-icon-view" size="mini" @click="showEmpInfo(scope.row)">查看高级资料</el-button>
                        <el-button style="padding: 3px" type="danger" icon="el-icon-delete" size="mini" @click="deleteEmp(scope.row)">删除</el-button>
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
        <el-dialog
                :title="title"
                :visible.sync="dialogVisible"
                modal
                width="80%">
            <div>
                <el-form ref="empForm" :model="emp" :rules="empRule">
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="姓名：" prop="name">
                                <el-input style="width: 150px" v-model="emp.name" size="mini" placeholder="请输入员工姓名" prefix-icon="el-icon-edit"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="性别：" prop="gender">
                                <el-radio-group v-model="emp.gender" style="margin-top: 10px">
                                    <el-radio label="男">男</el-radio>
                                    <el-radio label="女">女</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="出生日期：" size="mini" prop="birthday">
                                <el-date-picker
                                        v-model="emp.birthday"
                                        type="date"
                                        style="width: 150px"
                                        value-format="yyyy-MM-dd"
                                        placeholder="出生日期">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="政治面貌：" prop="politicId">
                                <el-select v-model="emp.politicId" size="mini" style="width: 150px" placeholder="政治面貌">
                                    <el-option
                                            v-for="item in politicsStatus"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="民族：" prop="nationId">
                                <el-select v-model="emp.nationId" size="mini" style="width: 150px" placeholder="民族">
                                    <el-option
                                            v-for="item in nations"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="籍贯：" prop="nativePlace">
                                <el-input style="width: 150px" v-model="emp.nativePlace" size="mini" placeholder="请输入籍贯" prefix-icon="el-icon-edit"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="电子邮箱：" size="mini" prop="email">
                                <el-input style="width: 150px" v-model="emp.email" size="mini" placeholder="请输入电子邮箱" prefix-icon="el-icon-message"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="联系地址：" size="mini" prop="address">
                                <el-input style="width: 150px" v-model="emp.address" size="mini" placeholder="请输入联系地址" prefix-icon="el-icon-edit"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="职位：" prop="posId">
                                <el-select v-model="emp.posId" size="mini" style="width: 150px" placeholder="职位">
                                    <el-option
                                            v-for="item in positions"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="职称：" prop="jobLevelId">
                                <el-select v-model="emp.jobLevelId" size="mini" style="width: 150px" placeholder="职称">
                                    <el-option
                                            v-for="item in jobLevels"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="所属部门：" size="mini" prop="departmentId">

                                <el-popover
                                        placement="bottom"
                                        title="请选择部门"
                                        width="200"
                                        trigger="manual"
                                        v-model="visible"
                                >
                                    <el-tree :data="allDeps"
                                             :props="defaultProps"
                                             default-expand-all
                                             @node-click="handleNodeClick"></el-tree>
                                    <div slot="reference" class="deptStyle" @click="showDept">{{deptName}}</div>
                                </el-popover>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="电话号码：" size="mini" prop="phone">
                                <el-input style="width: 150px" v-model="emp.phone" size="mini" placeholder="请输入电话号码" prefix-icon="el-icon-edit"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="工号：" prop="workID">
                                <el-input disabled style="width: 150px" v-model="emp.workID" size="mini" placeholder="请输入工号"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="学历：" prop="tiptopDegree">
                                <el-select v-model="emp.tiptopDegree" size="mini" style="width: 150px" placeholder="学历">
                                    <el-option
                                            v-for="item in tiptopDegrees"
                                            :key="item"
                                            :label="item"
                                            :value="item">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="毕业院校：" size="mini" prop="school">
                                <el-input style="width: 150px" size="mini" v-model="emp.school" placeholder="请输入毕业院校" prefix-icon="el-icon-edit"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="专业名称：" size="mini" prop="specialty">
                                <el-input style="width: 150px" size="mini" v-model="emp.specialty" placeholder="请输入专业名称" prefix-icon="el-icon-edit"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="入职日期：" prop="workID" size="mini">
                                <el-date-picker
                                        v-model="emp.beginDate"
                                        type="date"
                                        style="width: 150px"
                                        value-format="yyyy-MM-dd"
                                        placeholder="入职日期">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="转正日期：" prop="conversionTime" size="mini">
                                <el-date-picker
                                        v-model="emp.conversionTime"
                                        type="date"
                                        style="width: 150px"
                                        value-format="yyyy-MM-dd"
                                        placeholder="转正日期">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="合同起始日期：" size="mini" prop="beginContract">
                                <el-date-picker
                                        v-model="emp.beginContract"
                                        type="date"
                                        style="width: 150px"
                                        value-format="yyyy-MM-dd"
                                        placeholder="合同起始日期">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="合同截止日期：" size="mini" prop="endContract">
                                <el-date-picker
                                        v-model="emp.endContract"
                                        type="date"
                                        style="width: 150px"
                                        value-format="yyyy-MM-dd"
                                        placeholder="合同截止日期">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="身份证号：" prop="idCard">
                                <el-input style="width: 200px" v-model="emp.idCard" size="mini" placeholder="请输入身份证号" prefix-icon="el-icon-edit"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="聘用形式：" prop="engageForm">
                                <el-radio-group v-model="emp.engageForm" style="margin-top: 10px">
                                    <el-radio label="劳动合同">劳动合同</el-radio>
                                    <el-radio label="劳务合同">劳务合同</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="婚姻状况：" prop="wedlock">
                                <el-radio-group v-model="emp.wedlock" style="margin-top: 10px">
                                    <el-radio label="已婚">已婚</el-radio>
                                    <el-radio label="未婚">未婚</el-radio>
                                    <el-radio label="离异">离异</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
            <template #footer>
                    <span class="dialog-footer">
                      <el-button @click="dialogVisible = false">取 消</el-button>
                      <el-button type="primary" @click="addEmp">确 定</el-button>
                    </span>
            </template>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "EmpBasic",
        data() {
            return {
                nations:[],
                jobLevels:[],
                politicsStatus:[],
                positions:[],
                empData: [],
                loading: false,
                tiptopDegrees:['博士','硕士','本科','大专','高中','初中','小学','其他'],
                allDeps: [],
                deptName: '',
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                visible: false,
                visible2: false,
                showAdvanceSearch: false,
                empSearch: {
                    nationId: null,
                    politicId: null,
                    departmentId: null,
                    posId: null,
                    jobLevelId: null,
                    engageForm: "",
                    beginDateScope: null
                },
                emp: {
                    id: null,
                    name: "",
                    gender: "",
                    birthday: "",
                    idCard: "",
                    wedlock: "",
                    nationId: null,
                    nativePlace: "",
                    politicId: null,
                    email: "",
                    phone: "",
                    address: "",
                    departmentId: null,
                    jobLevelId: null,
                    posId: null,
                    engageForm: "",
                    tiptopDegree: "",
                    specialty: "",
                    school: "",
                    beginDate: "",
                    workState: "在职",
                    workID: "",
                    contractTerm: null,
                    conversionTime: "",
                    notWorkDate: null,
                    beginContract: "",
                    endContract: "",
                    workAge: null,
                },
                empRule: {
                    name: [{ required: true, message: '请输入员工姓名', trigger: 'blur' }],
                    gender: [{ required: true, message: '请输入员工性别', trigger: 'blur' }],
                    birthday: [{ required: true, message: '请输入出生日期', trigger: 'blur' }],
                    idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' },
                        { pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2})$/,
                            message: '身份证号格式不正确',
                            trigger: 'blur' }],
                    wedlock: [{ required: true, message: '请选择婚姻状况', trigger: 'blur' }],
                    nationId: [{ required: true, message: '请选择民族', trigger: 'blur' }],
                    nativePlace: [{ required: true, message: '请输入籍贯', trigger: 'blur' }],
                    politicId: [{ required: true, message: '请选择政治面貌', trigger: 'blur' }],
                    email: [{ required: true, message: '请输入邮箱', trigger: 'blur' },{
                        type: 'email', message: '邮箱格式不正确', trigger: 'blur'
                    }],
                    phone: [{ required: true, message: '请输入电话号码', trigger: 'blur' }],
                    address: [{ required: true, message: '请输入地址', trigger: 'blur' }],
                    departmentId: [{ required: true, message: '请选择部门', trigger: 'blur' }],
                    jobLevelId: [{ required: true, message: '请选择职称', trigger: 'blur' }],
                    posId: [{ required: true, message: '请选择职位', trigger: 'blur' }],
                    engageForm: [{ required: true, message: '请选择聘用形式', trigger: 'blur' }],
                    tiptopDegree: [{ required: true, message: '请选择学历', trigger: 'blur' }],
                    specialty: [{ required: true, message: '请输入专业', trigger: 'blur' }],
                    school: [{ required: true, message: '请输入毕业院校', trigger: 'blur' }],
                    beginDate: [{ required: true, message: '请输入入职日期', trigger: 'blur' }],
                    contractTerm: [{ required: true, message: '请输入合同日期', trigger: 'blur' }],
                    conversionTime: [{ required: true, message: '请输入转正日期', trigger: 'blur' }],
                    notWorkDate: [{ required: true, message: '请输入离职日期', trigger: 'blur' }],
                    beginContract: [{ required: true, message: '请输入合同起始日期', trigger: 'blur' }],
                    endContract: [{ required: true, message: '请输入合同截止日期', trigger: 'blur' }],
                },
                currentPage: 1,
                size: 10,
                total: 0,
                empName: '',
                dialogVisible: false,
                title: '',
                importDataIcon: 'el-icon-upload2',
                importDataBtn: '导入数据',
                importDataDisabled: false,
                headers:{
                    Authorization: window.sessionStorage.getItem('tokenStr')
                }
            }
        },
        mounted() {
            this.initEmpData()
            this.initSelectData()
            this.initPositions()
        },
        methods: {
            showEmpInfo(data){},
            showEditView(data){
                this.dialogVisible = true
                this.title = '编辑员工信息'
                this.deptName = data.department.name
                this.initPositions()
                this.emp = data
            },
            deleteEmp(data){
                this.$confirm('此操作将永久删除【'+data.name+'】, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/employee/basic/delete/'+data.id).then(res =>{
                        if (res){
                            this.initEmpData()
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'warning',
                        message: '取消删除'
                    });
                });
            },
            addEmp(){
                this.$refs['empForm'].validate(valid => {
                    if (valid){
                        if (this.emp.id){
                            this.putRequest('/employee/basic/update',this.emp).then(res => {
                                if (res){
                                    this.dialogVisible = false
                                    this.initEmpData()
                                }
                            })
                        }else {
                            this.postRequest('/employee/basic/add',this.emp).then(res => {
                                if (res){
                                    this.dialogVisible = false
                                    this.initEmpData()
                                }
                            })
                        }
                    }else {
                        this.$message.warning("请完善表单信息！")
                    }
                })
            },
            showDept(){
                this.visible = !this.visible
            },
            showDeptView(){
                this.visible2 = !this.visible2
            },
            handleNodeClick(data){
                this.emp.departmentId = data.id
                this.deptName = data.name
                this.visible = !this.visible
            },
            handleNodeClick2(data){
                this.empSearch.departmentId = data.id
                this.deptName = data.name
                this.visible2 = !this.visible2
            },
            initPositions(){
                this.getRequest('/system/basic/pos/listAll').then(res =>{
                    if (res){
                       this.positions = res
                    }
                })
            },
            initSelectData(){
                let nation = window.sessionStorage.getItem('nations')
                let jobLevel = window.sessionStorage.getItem('jobLevels')
                let politicsStatus = window.sessionStorage.getItem('politicsStatus')
                let allDept = window.sessionStorage.getItem('allDeps')
                if (!allDept){
                    this.getRequest('/system/basic/department/listAll').then(res =>{
                        if (res){
                            this.allDeps = res;
                            window.sessionStorage.setItem('allDeps',JSON.stringify(res))
                        }
                    })
                }else {
                    this.allDeps = JSON.parse(allDept)
                }
                if (!politicsStatus){
                    this.getRequest('/employee/basic/politicsStatusAll').then(res =>{
                        if (res){
                            this.politicsStatus = res;
                            window.sessionStorage.setItem('politicsStatus',JSON.stringify(res))
                        }
                    })
                }else {
                    this.politicsStatus = JSON.parse(politicsStatus)
                }
                if (!jobLevel){
                    this.getRequest('/system/basic/joblevel/listAll').then(res =>{
                        if (res){
                            this.jobLevels = res;
                            window.sessionStorage.setItem('jobLevels',JSON.stringify(res))
                        }
                    })
                }else {
                    this.jobLevels = JSON.parse(jobLevel)
                }
                if (!nation){
                    this.getRequest('/employee/basic/nation').then(res =>{
                        if (res){
                            this.nations = res;
                            window.sessionStorage.setItem('nations',JSON.stringify(res))
                        }
                    })
                }else {
                    this.nations = JSON.parse(nation)
                }
            },
            getMaxId(){
                this.getRequest('/employee/basic/maxId').then(res => {
                    if (res){
                        this.emp.workID = res.obj
                    }
                })
            },
            beforeUpload(){
                this.importDataBtn = '正在导入'
                this.importDataIcon = 'el-icon-loading'
                this.importDataDisabled = true
            },
            onSuccess(){
                this.importDataBtn = '导入数据'
                this.importDataIcon = 'el-icon-upload2'
                this.importDataDisabled = false
                this.initEmpData()
            },
            onError(){
                this.importDataBtn = '导入数据'
                this.importDataIcon = 'el-icon-upload2'
                this.importDataDisabled = false
            },
            exportEmp(){
                this.downloadRequest('/employee/basic/export')
            },
            showAddEmp(){
                this.initPositions()
                this.getMaxId()
                this.title = '添加员工'
                this.deptName = ''
                this.emp = {}
                this.dialogVisible = !this.dialogVisible
            },
            handleCurrentChange(currentPage) {
                this.currentPage = currentPage
                this.initEmpData()
            },
            handleSizeChange(size) {
                this.size = size
                this.initEmpData()
            },
            initEmpData(type) {
                let url = '/employee/basic/listAll/?currentPage=' + this.currentPage + '&size=' + this.size
                if (type && type === 'advance'){
                    if (this.empSearch.politicId){
                        url += '&politicId=' + this.empSearch.politicId
                    }
                    if (this.empSearch.nationId){
                        url += '&nationId=' + this.empSearch.nationId
                    }
                    if (this.empSearch.posId){
                        url += '&posId=' + this.empSearch.posId
                    }
                    if (this.empSearch.jobLevelId){
                        url += '&jobLevelId=' + this.empSearch.jobLevelId
                    }
                    if (this.empSearch.engageForm){
                        url += '&engageForm=' + this.empSearch.engageForm
                    }
                    if (this.empSearch.departmentId){
                        url += '&departmentId=' + this.empSearch.departmentId
                    }
                    if (this.empSearch.beginDateScope){
                        url += '&beginDateScope=' + this.empSearch.beginDateScope
                    }
                }else {
                    url += '&name=' + this.empName
                }
                this.postRequest(url).then(res => {
                    this.loading = true
                    if (res) {
                        this.loading = false
                        this.empData = res.data
                        this.total = res.total
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .deptStyle{
        width: 150px;
        height: 28px;
        font-size: 13px;
        border: 1px solid #dedede;
        display: inline-flex;
        border-radius: 5px;
        box-sizing: border-box;
        padding-left: 15px;
        cursor: pointer;
        align-items: center;
    }
    .deptSearch{
        width: 130px;
        height: 28px;
        font-size: 13px;
        display: inline-flex;
        border: 1px solid #dedede;
        border-radius: 5px;
        box-sizing: border-box;
        padding-left: 15px;
        cursor: pointer;
        align-items: center;
    }
    .searchStyle{
        border: 1px solid #409eff;
        border-radius: 5px;
        box-sizing: border-box;
        margin: 10px;
        padding: 5px;
    }
</style>
