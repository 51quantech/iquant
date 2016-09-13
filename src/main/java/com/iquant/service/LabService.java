package com.iquant.service;

import com.iquant.domain.beans.UserStrategy;
import com.iquant.domain.query.UserStrategyQuery;

import java.util.List;

/**
 * Created by yonggangli on 2016/9/1.
 */
public interface LabService {

    // 用户策略
    int queryUserStrategyTotalRows(UserStrategyQuery userStrategyQuery);

    List<UserStrategy> findUserStrategyNameList(UserStrategyQuery userStrategyQuery);

    UserStrategy findUserStrategyById(int id);

    UserStrategy findUserStrategyById(int id, String uname);

    UserStrategy findUserStrategyByName(UserStrategy userStrategy);

    int insertUserStrategy(UserStrategy userStrategy);

    int deleteUserStrategyById(int id);

    int updateUserStrategyById(UserStrategy userStrategy);

    int updateUserStrategyConditionById(UserStrategy userStrategy);

}
