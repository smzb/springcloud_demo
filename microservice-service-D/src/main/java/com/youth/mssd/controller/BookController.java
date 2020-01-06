package com.youth.mssd.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author:zhangkaifei
 * Date:2020/1/2
 * Description:
 *
 * 访问配置中心地址
 * http://localhost:9090/serviceD-dev.yml
 * RefreshScope配置自动刷新拉取config-server中的配置文件
 *
 * 需要手动刷新配置
 *
 * 解决方案：配置git的webhooks
 * 集群解决方案：Spring Cloud Bus Topic模式
 */
@RestController
@RefreshScope
public class BookController {

    @Value("${name}")
    String name;


    @GetMapping("/getBook")
    public String getBook(){
        return "books";
    }

    @GetMapping("/getName")
    public String getName(){
        return name;
    }

}
