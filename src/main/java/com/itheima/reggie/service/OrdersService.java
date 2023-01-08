package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.pojo.Orders;

/**
 * @author shkstart
 * @create 2023-01-08 14:20
 */
public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
