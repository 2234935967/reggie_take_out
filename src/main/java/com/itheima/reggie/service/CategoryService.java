package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.pojo.Category;

/**
 * @author shkstart
 * @create 2022-12-21 13:33
 */
public interface CategoryService extends IService<Category> {
    void deleteByid(Long id);
}
