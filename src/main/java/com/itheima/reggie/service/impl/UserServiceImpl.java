package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.mapper.UserMapper;
import com.itheima.reggie.pojo.User;
import com.itheima.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author shkstart
 * @create 2023-01-03 10:02
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
