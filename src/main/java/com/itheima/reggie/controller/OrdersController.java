package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.pojo.OrderDetail;
import com.itheima.reggie.pojo.Orders;
import com.itheima.reggie.pojo.User;
import com.itheima.reggie.service.OrderDetailService;
import com.itheima.reggie.service.OrdersService;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.util.BaseContext;
import com.itheima.reggie.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author shkstart
 * @create 2023-01-08 14:22
 * 订单管理
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    ///order/again
    @PostMapping("/again")
    public R<Orders> returnOrder(@RequestBody Long id){
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getNumber,id);
        Orders orders = ordersService.getOne(queryWrapper);
        return R.success(orders);
    }

    @PutMapping
   // @CacheEvict(value = "userpage",allEntries = true)
    public R<String> update(@RequestBody Orders orders){
        ordersService.updateById(orders);
        redisTemplate.delete("order*");
        return R.success("ok");
    }

    @GetMapping("/page")
    public R<Page> pageR(Integer page,Integer pageSize,String number){
        Page<Orders> ordersPage = new Page<>(page,pageSize);
        Page<OrdersDto> DtoPage = new Page<>();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(number != null,Orders::getNumber,number);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(ordersPage,queryWrapper);
        BeanUtils.copyProperties(ordersPage, DtoPage,"records");
        //设置dto
        List<Orders> list = ordersPage.getRecords();
        List<OrdersDto> dtolist= list.stream().map((item)->{
            OrdersDto dto = new OrdersDto();
            BeanUtils.copyProperties(item,dto);

            User user = userService.getById(item.getUserId());
            if (user != null){
                dto.setUserName(user.getName());
            }
            return dto;
        }).collect(Collectors.toList());
        DtoPage.setRecords(dtolist);

        return R.success(DtoPage);
    }


    @PostMapping("/submit")
   // @CacheEvict(value = "userpage",allEntries = true)
    public R<String> stringR(@RequestBody Orders orders){
        //IdWorker.getId()
        ordersService.submit(orders);
        redisTemplate.delete("order*");
        return R.success("ok");
    }

    //userPage?page=1&pageSize=1
    @GetMapping("/userPage")
    //@Cacheable(value = "userpage",key = "#page+'_'+#pageSize")
    public R<Page> pageR(Integer page,Integer pageSize){
        Page<OrdersDto> DtoPage = null;
        Long userId = BaseContext.getCurrentId();
        String key = "order_"+userId+"_"+page+"_"+pageSize;
        DtoPage= (Page<OrdersDto>) redisTemplate.opsForValue().get(key);
        if (DtoPage != null){
            return R.success(DtoPage);
        }

        Page<Orders> ordersPage = new Page<>(page,pageSize);
        DtoPage = new Page<>();

        //查询到订单所有内容
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(ordersPage,queryWrapper);
        BeanUtils.copyProperties(ordersPage, DtoPage,"records");
        //设置dto
        List<Orders> list = ordersPage.getRecords();
        List<OrdersDto> dtolist= list.stream().map((item)->{
            OrdersDto dto = new OrdersDto();
            BeanUtils.copyProperties(item,dto);

            Long id = item.getId();
            LambdaQueryWrapper<OrderDetail> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(OrderDetail::getOrderId,id);
            List<OrderDetail> details = orderDetailService.list(queryWrapper1);
            dto.setOrderDetails(details);
            return dto;
        }).collect(Collectors.toList());
        DtoPage.setRecords(dtolist);
        redisTemplate.opsForValue().set(key,DtoPage,30, TimeUnit.MINUTES);
        return R.success(DtoPage);
    }
}
