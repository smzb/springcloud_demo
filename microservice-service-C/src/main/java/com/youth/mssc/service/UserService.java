package com.youth.mssc.service;

import com.youth.mssc.callback.UserServiceCallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Author:zhangkaifei
 * Date:2020/1/2
 * Description:
 */

/**
 * value指定调用的服务
 * fallbackFactory熔断器的降级提示
 */
@FeignClient(value = "SERVICEA", fallbackFactory = UserServiceCallBack.class)
public interface UserService {
    @GetMapping(value = "/user/getUser")
    String getUser();

    @GetMapping(value = "/user/getUserList")
    String getUserList();
}
