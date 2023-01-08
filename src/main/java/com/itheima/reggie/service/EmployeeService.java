package com.itheima.reggie.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.pojo.Employee;

/**
 * @author shkstart
 * @create 2022-12-17 10:19
 */
public interface EmployeeService extends IService<Employee> {
    IPage<Employee> getAllEmployee(Page<Employee> employeePage, String name);
}
