package com.iquant.service;

/**
 * Created by yonggangli on 2016/8/24.
 */
public interface LoginService {

    void insertUser(String uname, String upwd);

    int queryUserTotalRows(String uname, String upwd);

}
