package com.itheima.reggie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author shkstart
 * @create 2023-01-10 17:25
 */
@Controller
public class indexController {
    @GetMapping("/")
    public String index() {
        return "/front/page/login.html";
    }
}
