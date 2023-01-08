package com.itheima.reggie.controller;


import ch.qos.logback.core.util.FileUtil;
import com.itheima.reggie.util.R;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * @author shkstart
 * @create 2022-12-21 18:44
 */

@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.file.path}")
    String filePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file)  {
        //获取并产生新文件名
        String fname = file.getOriginalFilename();
        String fix = fname.substring(fname.lastIndexOf("."));
        String filename =  UUID.randomUUID().toString()+fix;
        //如果没有目录创建目录
        File filep = new File(filePath);
        if (!filep.exists()) {
            filep.mkdirs();
        }
        //下载
        try {
            file.transferTo(new File(filePath.concat(filename)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回文件名
        return R.success(filename);
    }



    @GetMapping("/download")
    public void downloadPhoto(String name, HttpServletResponse response) {
        FileInputStream InputStream =null;
        ServletOutputStream outputStream=null;
        try {
            //获取流
            InputStream = new FileInputStream(new File(filePath+name));
            outputStream = response.getOutputStream();
            //输出
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = InputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            InputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
