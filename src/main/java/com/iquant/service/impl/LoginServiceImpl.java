package com.iquant.service.impl;

import com.iquant.domain.beans.User;
import com.iquant.service.LoginService;
import com.iquant.domain.query.UserQuery;
import com.iquant.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yonggangli on 2016/8/24.
 */
@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void insertUser(String uname, String upwd) {
        User user = new User();
        user.setUname(uname);
        user.setUpwd(upwd);
        userMapper.insertUser(user);
    }

    @Override
    public int queryUserTotalRows(String uname, String upwd) {
        UserQuery userQuery = new UserQuery();
        userQuery.setUname(uname);
        userQuery.setUpwd(upwd);
        return userMapper.queryUserTotalRows(userQuery);
    }
}
