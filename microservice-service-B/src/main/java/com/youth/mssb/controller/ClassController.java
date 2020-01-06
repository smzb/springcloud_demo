package com.youth.mssb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * Author:zhangkaifei
 * Date:2020/1/2
 * Description:
 */
@RestController
@RequestMapping("/class")
public class ClassController {
    private static final String REST_URL_PREFIX = "http://SERVICEA";

    @Resource
    private RestTemplate restTemplate;


    @GetMapping("/getUserClass")
    public String getUserClass() {
        return restTemplate.getForEntity(REST_URL_PREFIX + "/user/getUser", String.class).getBody();
    }
}
