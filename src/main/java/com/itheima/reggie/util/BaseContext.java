package com.itheima.reggie.util;


import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * @author shkstart
 * @create 2022-12-21 13:04
 * 基于threadlocal线程封装工具类，来获得和保存用户登录的id
 */
public class BaseContext{
   private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

   public static void setCurrentId(long id){
       threadLocal.set(id);
   }
    public static Long getCurrentId(){
       return threadLocal.get();
    }

}
