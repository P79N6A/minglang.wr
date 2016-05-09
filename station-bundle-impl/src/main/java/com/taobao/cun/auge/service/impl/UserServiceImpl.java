package com.taobao.cun.auge.service.impl;

import org.springframework.beans.factory.annotation.Value;

import com.taobao.cun.auge.service.UserService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * user service implementation
 *
 * @author leijuan
 */
@HSFProvider(serviceInterface = UserService.class)
public class UserServiceImpl implements UserService {


	
    public String getNick(Long id) {
    	
        return "nick:" + id;
    }
}
