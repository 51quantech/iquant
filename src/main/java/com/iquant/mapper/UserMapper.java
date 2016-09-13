package com.iquant.mapper;

import com.iquant.domain.beans.User;
import com.iquant.domain.query.UserQuery;

import java.util.List;

/**
 * Created by yonggangli on 2016/8/24.
 */
public interface UserMapper {

    int queryUserTotalRows(UserQuery userQuery);

    List<User> queryUserList(UserQuery userQuery);

    User queryUserByUname(String uname);

    User queryUserById(int id);

    int insertUser(User user);

    int deleteUserByUname(String uname);

    int updateUserByUname(User user);

    int deleteUserById(int id);

    int updateUserById(User user);

}
