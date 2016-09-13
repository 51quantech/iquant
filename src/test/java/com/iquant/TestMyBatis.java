package com.iquant;

import com.iquant.service.UserService;
import com.iquant.interceptor.TestHandlerInterceptor;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


/**
 * Created by yonggangli on 2016/8/18.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class TestMyBatis {

    private static Logger LOG = Logger.getLogger(TestHandlerInterceptor.class);

    @Resource
    private UserService userService = null;

    @Test
    public void test(){

    }

}
