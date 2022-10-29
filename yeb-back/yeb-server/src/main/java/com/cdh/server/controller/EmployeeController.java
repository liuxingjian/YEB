package com.cdh.server.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.cdh.server.pojo.*;
import com.cdh.server.pojo.rest.PageBean;
import com.cdh.server.pojo.rest.Result;
import com.cdh.server.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Api(tags = "员工管理")
@RestController
@RequestMapping("employee/basic")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private IPoliticsStatusService  politicsStatusService;
    @Autowired
    private INationService nationService;
    @Autowired
    private IPositionService positionService;
    @Autowired
    private IJoblevelService joblevelService;
    @Autowired
    private IDepartmentService departmentService;

    @ApiOperation(value = "分页查询所有员工")
    @PostMapping("listAll")
    public PageBean listAll(@RequestParam(defaultValue = "1") Integer currentPage,
                            @RequestParam(defaultValue = "10") Integer size,
                            Employee employee,
                            LocalDate[] dateScope){
        return employeeService.getEmployeeByPage(currentPage,size,employee,dateScope);
    }

    @ApiOperation(value = "查询所有政治面貌")
    @GetMapping("politicsStatusAll")
    public List<PoliticsStatus> listAllPoliticsStatus(){
        return politicsStatusService.list();
    }

    @ApiOperation(value = "查询所有民族")
    @GetMapping("nation")
    public List<Nation> listAllNation(){
        return nationService.list();
    }

    @ApiOperation(value = "查询最大工号")
    @GetMapping("maxId")
    public Result getMaxId(){
        return employeeService.getMaxId();
    }

    @ApiOperation(value = "添加员工")
    @PostMapping("add")
    public Result addEmp(@RequestBody Employee employee){
        return employeeService.addEmp(employee);
    }

    @ApiOperation(value = "更新员工")
    @PutMapping("update")
    public Result updateEmp(@RequestBody Employee employee){
        if (employeeService.updateById(employee)){
            return Result.success("员工信息更新成功");
        }
        return Result.error("员工信息更新失败");
    }

    @ApiOperation(value = "删除员工")
    @DeleteMapping("delete/{id}")
    public Result deleteEmp(@PathVariable("id") Integer id){
        if(employeeService.removeById(id)){
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    @ApiOperation(value = "导出员工数据")
    @GetMapping(value = "export",produces = "application/octet-stream")
    public void exportEmp(HttpServletResponse response){
        List<Employee> employeeList = employeeService.getEmployee(null);
        ExportParams params = new ExportParams("员工表","员工表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, Employee.class, employeeList);
        ServletOutputStream outputStream = null;
        try{
            //流形式
            response.setHeader("Content-type","application/octet-stream");
            //防止中文乱码
            response.setHeader("content-disposition","attachment;filename="
                    + URLEncoder.encode("员工表.xls","UTF-8"));
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (null != outputStream){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiOperation(value = "导入员工数据")
    @PostMapping(value = "import")
    public Result importEmp(MultipartFile file){
        ImportParams params = new ImportParams();
        //去掉标题行
        params.setTitleRows(1);
        List<Nation> nationList = nationService.list();
        List<Department> departmentList = departmentService.list();
        List<Joblevel> jobLevelList = joblevelService.list();
        List<PoliticsStatus> politicsStatusList = politicsStatusService.list();
        List<Position> positionList = positionService.list();
        try {
            List<Employee> list = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, params);
            list.forEach(employee -> {
                employee.setNationId(nationList.get(nationList.indexOf(new Nation(employee.getNation().getName()))).getId());
                employee.setPoliticId(politicsStatusList.get(politicsStatusList.indexOf(new PoliticsStatus(employee.getPoliticsStatus().getName()))).getId());
                employee.setPosId(positionList.get(positionList.indexOf(new Position(employee.getPosition().getName()))).getId());
                employee.setJobLevelId(jobLevelList.get(jobLevelList.indexOf(new Joblevel(employee.getJoblevel().getName()))).getId());
                employee.setDepartmentId(departmentList.get(departmentList.indexOf(new Department(employee.getDepartment().getName()))).getId());
            });

            if (employeeService.saveBatch(list)){
                return Result.success("员工数据导入成功！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return Result.error("员工数据导入失败！");
    }
}
