package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shkstart
 * @create 2022-12-17 10:18
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
