package com.iquant.domain.beans;

import java.util.Date;

/**
 * Created by yonggangli on 2016/8/24.
 */
public class UserStrategy {

    private int id;
    private String uname;
    private String strategyName;
    private String strategyText;
    private Date modified;
    private Date created;
    private String strategyResult;
    private String strategyDetailResult;
    private Integer strategyStatus;
    private String imageId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getStrategyText() {
        return strategyText;
    }

    public void setStrategyText(String strategyText) {
        this.strategyText = strategyText;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getStrategyResult() {
        return strategyResult;
    }

    public void setStrategyResult(String strategyResult) {
        this.strategyResult = strategyResult;
    }

    public String getStrategyDetailResult() {
        return strategyDetailResult;
    }

    public void setStrategyDetailResult(String strategyDetailResult) {
        this.strategyDetailResult = strategyDetailResult;
    }

    public Integer getStrategyStatus() {
        return strategyStatus;
    }

    public void setStrategyStatus(Integer strategyStatus) {
        this.strategyStatus = strategyStatus;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "UserStrategy [id=" + id + ", uname=" + uname
                + ", strategyName=" + strategyName + ", strategyText="
                + strategyText + ", modified=" + modified + ", created="
                + created + ", strategyResult=" + strategyResult
                + ", strategyDetailResult=" + strategyDetailResult
                + ", strategyStatus=" + strategyStatus + ", imageId=" + imageId
                + "]";
    }

}
