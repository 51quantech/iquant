package com.iquant.service.impl;

import com.iquant.domain.beans.User;
import com.iquant.service.UserService;
import com.iquant.domain.query.UserQuery;
import com.iquant.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yonggangli on 2016/8/18.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int queryUserTotalRows(UserQuery userQuery) {
        return 0;
    }

    @Override
    public List<User> queryUserList(UserQuery userQuery, int pageIndex, int pageSize) {
        return null;
    }

    @Override
    public User queryUserByUname(String uname) {
        return null;
    }

    @Override
    public User queryUserById(int id) {
        return null;
    }

    @Override
    public int insertUser(User user) {
        return 0;
    }

    @Override
    public int deleteUserByUname(String uname) {
        return 0;
    }

    @Override
    public int updateUserByUname(User user) {
        return 0;
    }

    @Override
    public int deleteUserById(int id) {
        return 0;
    }

    @Override
    public int updateUserById(User user) {
        return 0;
    }
}
