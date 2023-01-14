package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.itheima.reggie.pojo.AddressBook;
import com.itheima.reggie.service.AddressBookService;
import com.itheima.reggie.util.BaseContext;
import com.itheima.reggie.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 地址簿管理
 */
@Slf4j
@Api(tags = "用户地址控制器")
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Resource
    private AddressBookService addressBookService;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 新增
     */
    @ApiOperation("添加用户地址")
   // @CacheEvict(value = "address",allEntries = true)
    @PostMapping
    public R<AddressBook> save(
            @ApiParam("参数：用户地址") @RequestBody AddressBook addressBook
    ) {
        Long userId = BaseContext.getCurrentId();
        String key = "address_" + userId;

        addressBook.setUserId(userId);
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);

        redisTemplate.delete(key);

        return R.success(addressBook);
    }

    @ApiOperation("设置默认地址")
    //@CacheEvict(value = "address",key = "'address_default_*'")
    @PutMapping("default")
    public R<AddressBook> setDefault(
           @ApiParam("参数：用户id") @RequestBody AddressBook addressBook
    ) {
        Long userId = BaseContext.getCurrentId();
        String key = "address_default" + userId;

        log.info("addressBook:{}", addressBook);
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.set(AddressBook::getIsDefault, 0);
        //SQL:update address_book set is_default = 0 where user_id = ?
        addressBookService.update(wrapper);

        addressBook.setIsDefault(1);
        //SQL:update address_book set is_default = 1 where id = ?
        addressBookService.updateById(addressBook);

        redisTemplate.delete(key);

        return R.success(addressBook);
    }

    /**
     *
     */
    @ApiOperation("根据id查询地址")
    @GetMapping("/{id}")
    public R get(@ApiParam("用户id")@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该对象");
        }
    }

    /**
     * 查询默认地址
     */
    @ApiOperation("查询默认地址")
    //@Cacheable(value = "address",key = "'address_default_'+#result.data.userId")
    @GetMapping("default")
    public R<AddressBook> getDefault() {
        AddressBook addressBook = null;
        Long userId = BaseContext.getCurrentId();
        String key = "address_default" + userId;
        addressBook = (AddressBook) redisTemplate.opsForValue().get(key);
        if (addressBook != null) {
            return R.success(addressBook);
        }
        ////////////////////////////////
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId);
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        addressBook = addressBookService.getOne(queryWrapper);

        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            redisTemplate.opsForValue().set(key,addressBook,30,TimeUnit.MINUTES);
            return R.success(addressBook);
        }
    }

    @ApiOperation("查询指定用户的全部地址")
    //@Cacheable(value = "address",key = "address_")
    @GetMapping("/list")
    public R<List<AddressBook>> list(@ApiParam("用户id") AddressBook addressBook) {
        Long userId = BaseContext.getCurrentId();
        String key = "address_" + userId;
        List<AddressBook> list = null;
        list = (List<AddressBook>) redisTemplate.opsForValue().get(key);
        if (list != null) {
            return R.success(list);
        }

        addressBook.setUserId(userId);
        log.info("addressBook:{}", addressBook);
        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        //SQL:select * from address_book where user_id = ? order by update_time desc
         list = addressBookService.list(queryWrapper);

         redisTemplate.opsForValue().set(key,list,30, TimeUnit.MINUTES);
        return R.success(list);
    }
}
