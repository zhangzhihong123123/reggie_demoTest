package com.itheima.reggile;



import com.itheima.reggile.service.Impl.EmployeeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
@SpringBootApplication(scanBasePackages ="com.itheima.reggile.config")
@Slf4j
@ServletComponentScan
public class ReggieApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ReggieApplication.class, args);
        EmployeeServiceImpl bean = run.getBean(EmployeeServiceImpl.class);
        System.out.println(bean);
        log.info("项目启动成功");
    }


}
