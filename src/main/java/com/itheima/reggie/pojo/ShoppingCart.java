package com.itheima.reggie.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车
 * Serializable 序列化
 */
@Data
@ApiModel("套餐") //swagger注解，由在数据库对应模型中
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("主键") //swagger注解，由在数据库对应模型中
    private Long id;

    //名称
    private String name;

    //用户id
    private Long userId;

    //菜品id
    private Long dishId;

    //套餐id
    private Long setmealId;

    //口味
    private String dishFlavor;

    //数量
    private Integer number;

    //金额
    private BigDecimal amount;

    //图片
    private String image;

    private LocalDateTime createTime;
}
