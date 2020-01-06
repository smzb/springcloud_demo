package com.youth.mssa.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author:zhangkaifei
 * Date:2020/1/2
 * Description:
 * <p>
 * 因为tomcat启动之后会创建一个线程池来处理用户请求, 而线程池中的线程是有限的,
 * 当超出承载的最大请求数量时, 那用户的请求可就要开始排队了, 所以就会出现一直转圈的情况,
 * 所以虽然访问的是getUserList接口 但是他们两个是用了同一个线程池 所以都受到了影响
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Value("${server.port}")
    String port;


    @HystrixCommand(fallbackMethod = "fallback")
    @GetMapping("/getUser")
    public String getUser() {
        try {
            Thread.sleep(5000);
            return "user from serviceA" + port;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/getUserList")
    public String getUserList() {
        return "getUserList可以正常访问" + Thread.currentThread().getName();
    }

    String fallback() {
        return "服务器繁忙,请稍后再试";
    }
}
