package com.itheima.reggie.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.pojo.Dish;

import java.util.List;

/**
 * @author shkstart
 * @create 2022-12-21 18:36
 */
public interface DishService extends IService<Dish> {

    void saveWithFlavor(DishDto dishDto);

    void removeByIdsMithFlavor(List<Long> ids);

    void updateStatus(Integer sts, List<Long> ids);
    //测试
    List<Dish> listAll();
}
