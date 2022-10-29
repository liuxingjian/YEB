<template>
    <div>
        <el-tabs v-model="activeName" type="card">
            <el-tab-pane label="部门管理" name="depmana">
                <DepMana></DepMana>
            </el-tab-pane>
            <el-tab-pane label="职位管理" name="posmana">
                <pos-mana></pos-mana>
            </el-tab-pane>
            <el-tab-pane label="职称管理" name="joblevelmana">
               <joblevel-mana></joblevel-mana>
            </el-tab-pane>
            <el-tab-pane label="奖惩规则" name="ecmana">
               <EcMana></EcMana>
            </el-tab-pane>
            <el-tab-pane label="权限组" name="permissmana">
                <permiss-mana></permiss-mana>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    import DepMana from "../../components/sys/basic/DepMana";
    import EcMana from "../../components/sys/basic/EcMana";
    import PermissMana from "../../components/sys/basic/PermissMana";
    import PosMana from "../../components/sys/basic/PosMana";
    import JoblevelMana from "../../components/sys/basic/JoblevelMana";

    export default {
        name: "SysBasic",
        components: {JoblevelMana, PermissMana, EcMana, PosMana, DepMana},
        data(){
            return{
                activeName:'depmana'
            }
        }
    }
</script>

<style scoped>

</style>
