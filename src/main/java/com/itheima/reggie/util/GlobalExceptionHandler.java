package com.itheima.reggie.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;


/**
 * @author shkstart
 * @create 2022-12-17 19:15
 * 处理异常
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
@ResponseBody //响应json
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> ErrorHandler(SQLIntegrityConstraintViolationException scve){
        log.error("捕获异常{}",scve.getMessage());
        String message = scve.getMessage();
        boolean duplicate_entry = message.contains("Duplicate entry");
        if (duplicate_entry){
            String[] m = message.split(" ");
            String msg= m[2]+"账号已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }
    //捕获自定义异常
    @ExceptionHandler(MyException.class)
    public R<String> ErrorHandler(MyException scve){
        log.error("捕获异常{}",scve.getMessage());
        return R.error(scve.getMessage());
    }

}
