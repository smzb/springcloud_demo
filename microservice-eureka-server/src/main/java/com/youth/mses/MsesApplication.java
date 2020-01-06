package com.youth.mses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MsesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsesApplication.class, args);
    }

}
