package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.pojo.Dish;
import com.itheima.reggie.pojo.DishFlavor;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.util.MyException;
import com.itheima.reggie.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shkstart
 * @create 2022-12-21 18:37
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    DishFlavorService dishFlavorService;
    @Autowired
    DishMapper dishMapper;

    @Override
    //添加
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId();//菜品id

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);

    }
    //删除
    @Override
    @Transactional
    public void removeByIdsMithFlavor(List<Long> ids) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId,ids);
        queryWrapper.eq(Dish::getStatus,1);
        int count = this.count(queryWrapper);
        if (count>0){
            throw new MyException("菜品正在售卖，无法删除");
        }
        baseMapper.deleteBatchIds(ids);
        //删除关系表
        LambdaQueryWrapper<DishFlavor> QueryWrapper = new LambdaQueryWrapper<>();
        QueryWrapper.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(QueryWrapper);
    }

    @Override
    public void updateStatus(Integer sts, List<Long> ids) {
        //在mapper写sql语句 在这basemapper掉用 在mapper类中声明方法
        //baseMapper.updateStatus();
        dishMapper.updateStatus(sts, ids);
        /*LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        List<Dish> dishes = baseMapper.selectBatchIds(ids);
        for (Dish dish : dishes) {
            dish.setStatus(sts);
            baseMapper.updateById(dish);
        }*/
    }

    /**
     * 查询所有
     * 测试
     * @return
     */
    @Override
    public List<Dish> listAll() {
        List<Dish> list = dishMapper.listAll();
        return list;
    }
}
