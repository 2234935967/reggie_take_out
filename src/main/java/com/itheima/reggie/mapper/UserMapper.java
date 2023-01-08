package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shkstart
 * @create 2023-01-03 10:01
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
