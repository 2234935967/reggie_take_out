package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.mapper.AddressBookMapper;
import com.itheima.reggie.pojo.AddressBook;
import com.itheima.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author shkstart
 * @create 2023-01-03 10:44
 */
@Service
public class AddressBookServiceImpl
        extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
