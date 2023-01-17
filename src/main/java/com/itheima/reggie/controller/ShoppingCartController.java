package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itheima.reggie.pojo.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import com.itheima.reggie.util.BaseContext;
import com.itheima.reggie.util.MyException;
import com.itheima.reggie.util.R;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author shkstart
 * @create 2023-01-08 10:36
 */
@Api(tags = "购物车控制器")
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;


    @DeleteMapping("/clean")
    public R<String> clean(){
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(ShoppingCart::getUserId,userId);
        shoppingCartService.remove(qw);
        return R.success("清空成功");
    }
    //shoppingCart/sub
    @ApiOperation("减少数量")
    @ApiImplicitParams({                                             //是否必须
            @ApiImplicitParam(name = "shoppingCart",value = "购物车",required = true)
    })
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        //查询到该商品
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        if (shoppingCart.getDishId() != null){
            wrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else {
            wrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart cart = shoppingCartService.getOne(wrapper);
        //减set操作
        if (cart == null){
            throw new MyException("未知错误");
        }
        Integer number = cart.getNumber();
        if (number > 1 ){
            cart.setNumber(number - 1);
            shoppingCartService.updateById(cart);
        }else {
            cart.setNumber(number-1);
            shoppingCartService.removeById(cart);
        }
        return R.success(cart);
    }

    @ApiOperation("查询用户购物车菜品")
    @GetMapping("/list")
    public R<List<ShoppingCart>> listR(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        return R.success(shoppingCartService.list(queryWrapper));
    }

    @ApiOperation("添加菜品到购物车")
    @PostMapping("/add")
    public R<ShoppingCart> add(
        @ApiParam("菜品") @RequestBody ShoppingCart shoppingCart
    ){
        //设置shoppingcart的用户id
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        //查找菜品是否在购物车中
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        if (shoppingCart.getDishId() != null){ //如果是菜品
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else { //是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);
        if (cart == null){ //添加
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cart=shoppingCart;
        }else { //份数添加
            Integer number = cart.getNumber();
            cart.setNumber(number+1);
            shoppingCartService.updateById(cart);
        }
        return R.success(cart);
    }

}
