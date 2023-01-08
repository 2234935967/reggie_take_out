package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.pojo.Setmeal;

import java.util.List;

/**
 * @author shkstart
 * @create 2022-12-21 19:19
 */
public interface SetmealService extends IService<Setmeal> {
    void saveWithDto(SetmealDto setmealDto);

    void deleteWithDto(List<Long> ids);


}
