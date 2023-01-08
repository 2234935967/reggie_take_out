package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.pojo.Dish;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author shkstart
 * @create 2022-12-21 18:34
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    //@Update("sql语句+#{}") 也可在方法前写sql
    //使用#{} 在当有多个参数时，参数前需要添加注解:@Param("sql中#{这里}的内容")
    int updateStatus(@Param("sts") Integer sts,@Param("ids") List<Long> ids);

    /**
     * 查找所有
     * @return
     */
    List<Dish> listAll();
}
