package com.iquant.domain.query;

/**
 * Created by yonggangli on 2016/8/24.
 */
public class UserQuery {

    private String uname;
    private String upwd;
    private Integer status;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpwd() {
        return upwd;
    }

    public void setUpwd(String upwd) {
        this.upwd = upwd;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
