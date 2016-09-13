package com.iquant.domain.query;

/**
 * Created by yonggangli on 2016/8/24.
 */
public class UserStrategyQuery extends BaseQuery{

    private String uname;
    private String strategyName;
    private Integer sid;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }
}
