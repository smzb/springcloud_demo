package com.youth.mssd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;


/**
 *  springboot2.0  actuator下默认短点没有refresh断点 需要在yml文件中进行配置
 *  刷新地址 http://ip:port/actuator/refresh
 */
@SpringBootApplication
@EnableEurekaClient
public class MssdApplication {

    public static void main(String[] args) {
        SpringApplication.run(MssdApplication.class, args);
    }

}
