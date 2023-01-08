package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.pojo.AddressBook;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shkstart
 * @create 2023-01-03 10:43
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
