package com.cache.controllers;

import com.cache.common.result.ResultWrapper;
import com.cache.model.EmployeeVO;
import com.cache.services.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 员工实体类的一些数据更改操作
 *
 * @author zetu
 * @date 2021/3/26
 */
@RestController
@RequestMapping("api/emp")
@Api(tags = "员工相关操作接口")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    @PostMapping("add")
    @ApiOperation("创建新的员工数据")
    public ResultWrapper<Integer> addEmp(@RequestBody EmployeeVO emp) {
        Integer empId = employeeService.addEmp(emp);
        return ResultWrapper.of(empId);
    }

    @PutMapping("update/{id}")
    @ApiOperation("更新员工信息")
    public ResultWrapper<Boolean> updateEmp(
            @PathVariable(value = "id") Integer id,
            @RequestBody EmployeeVO emp
    ) {
        Boolean result = employeeService.updateEmp(id, emp);
        return ResultWrapper.of(result);
    }

    @GetMapping("{id}")
    @ApiOperation("根据ID获取员工信息")
    public ResultWrapper<EmployeeVO> getEmp(@PathVariable(value = "id") Integer id) {
        EmployeeVO result = employeeService.getById(id);
        return ResultWrapper.of(result);
    }

    @GetMapping("list")
    @ApiOperation("获取所有员工信息列表")
    public ResultWrapper<List<EmployeeVO>> getList() {
        List<EmployeeVO> result = employeeService.getList();
        return ResultWrapper.of(result);
    }

}
