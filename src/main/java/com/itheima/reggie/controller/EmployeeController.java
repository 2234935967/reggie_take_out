package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.pojo.Employee;
import com.itheima.reggie.service.EmployeeService;
import com.itheima.reggie.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * @author shkstart
 * @create 2022-12-17 10:20
 * 员工模块
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    /**
     * 登录功能开发
     * @param employee 用户
     * @param request 返回session
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(
            @RequestBody Employee employee,
            HttpServletRequest request
    ){
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(wrapper);

        if (emp == null || !emp.getPassword().equals(password)) {
            return  R.error("登录失败");
        }
        if (emp.getStatus()==0){
            return  R.error("账号已被禁用");
        }
        HttpSession session = request.getSession();
        session.setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<IPage> page(
           Integer page,
           Integer pageSize,
           String name
    ){
        log.info("第{}页，显示了{}条，模糊查询{}", page,pageSize,name);
        Page<Employee> employeePage = new Page<Employee>(page,pageSize);
        IPage<Employee> iPage= employeeService.getAllEmployee(employeePage,name);
        return R.success(iPage);
    }

    /**
     * 添加员工
     * @param employee
     * @param request
     * @return
     */
    @PostMapping
    public  R<String> save(@RequestBody Employee employee,HttpServletRequest request){
        //初始密码123456，加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //设置各个属性 ：：：：：已自动填充
       // employee.setCreateTime(LocalDateTime.now());
      //  employee.setUpdateTime(LocalDateTime.now());
       // Long empid  = (Long) request.getSession().getAttribute("employee");
       // employee.setCreateUser(empid);
       //employee.setUpdateUser(empid);
        //添加数据库中
        employeeService.save(employee);
        return R.success("添加成功");
    }

    /**
     * 修改员工信息
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> updateStuts(HttpServletRequest request,@RequestBody Employee employee){
        //已自动填充
        //long tid= (long) request.getSession().getAttribute("employee");
        //employee.setUpdateUser(tid);
        //employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询emp信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee empbyid = employeeService.getById(id);
        if (empbyid != null){
            return R.success(empbyid);
        }else {
            return R.error("员工信息查询失败");
        }
    }

}
