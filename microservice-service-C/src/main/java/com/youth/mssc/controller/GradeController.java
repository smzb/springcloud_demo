package com.youth.mssc.controller;

import com.youth.mssc.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Author:zhangkaifei
 * Date:2020/1/2
 * Description:
 */
@RestController
public class GradeController {
    @Resource
    private UserService userService;

    @GetMapping("/getGrade")
    public String getGrade() {
        return userService.getUser();
    }

    @GetMapping("/getGrade1")
    public String getGrade1() {
        return userService.getUserList();
    }

}
