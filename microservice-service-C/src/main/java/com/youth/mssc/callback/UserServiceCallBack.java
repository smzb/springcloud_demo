package com.youth.mssc.callback;

import com.youth.mssc.service.UserService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * Author:zhangkaifei
 * Date:2020/1/2
 * Description:
 */
@Component
public class UserServiceCallBack implements FallbackFactory<UserService> {
    @Override
    public UserService create(Throwable throwable) {
        return new UserService() {
            @Override
            public String getUser() {
                return "serviceA getUser 以熔断";
            }

            @Override
            public String getUserList() {
                return "serviceA getUserList 以熔断";
            }
        };
    }
}
