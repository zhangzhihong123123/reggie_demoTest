package com.itheima.reggile.controller;

import com.itheima.reggile.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@Controller
@Slf4j
@Service
public class UploadCommon {

    @Value("${reggie.path}")
    private String basePath;


    @ResponseBody
    @PostMapping("/common/upload")
    public R<String> upload(MultipartFile file) throws IOException {

        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用UUID防止覆盖
        String fileName=UUID.randomUUID().toString();
        File file1 = new File(basePath + fileName + substring);
        if(!file1.exists())
        {
           file1.mkdir();
        }

        file.transferTo(new File(basePath+fileName+substring));
        log.info(file.getName());
        return R.success(fileName + substring);
    }

    @RequestMapping("/common/download")
    public void  downlown(String name, HttpServletResponse response) throws Exception {
       //输出流读取文件
        System.out.println(basePath+name);
        response.setContentType("imge/jpeg");
        FileInputStream fileInputStream=new FileInputStream(basePath+name);
        ServletOutputStream outputStream=response.getOutputStream();
        int len=0;
        byte[] bytes=new byte[1024];
        while ((len=fileInputStream.read(bytes))!=-1) {
            outputStream.write(bytes,0,len);
            outputStream.flush();
        }
        outputStream.close();
        fileInputStream.close();
    }
}
