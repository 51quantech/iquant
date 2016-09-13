package com.iquant.service.impl;

import com.iquant.domain.beans.UserStrategy;
import com.iquant.domain.query.UserStrategyQuery;
import com.iquant.mapper.UserStrategyMapper;
import com.iquant.service.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yonggangli on 2016/9/1.
 */
@Service("labService")
public class LabServiceImpl implements LabService {

    @Autowired
    private UserStrategyMapper userStrategyMapper;

    @Override
    public int queryUserStrategyTotalRows(UserStrategyQuery userStrategyQuery) {
        return userStrategyMapper.queryUserStrategyTotalRows(userStrategyQuery);
    }

    @Override
    public List<UserStrategy> findUserStrategyNameList(UserStrategyQuery userStrategyQuery) {
        return userStrategyMapper.queryUserStrategyNameList(userStrategyQuery);
    }

    @Override
    public UserStrategy findUserStrategyById(int id) {
        return userStrategyMapper.queryUserStrategyById(id);
    }

    @Override
    public UserStrategy findUserStrategyById(int id, String uname) {
        UserStrategyQuery userStrategyQuery = new UserStrategyQuery();
        userStrategyQuery.setSid(id);
        userStrategyQuery.setUname(uname);
        return userStrategyMapper.queryUserStrategy(userStrategyQuery);
    }

    @Override
    public UserStrategy findUserStrategyByName(UserStrategy userStrategy) {
        return userStrategyMapper.queryUserStrategyByName(userStrategy);
    }

    @Override
    public int insertUserStrategy(UserStrategy userStrategy) {
        return userStrategyMapper.insertUserStrategy(userStrategy);
    }

    @Override
    public int deleteUserStrategyById(int id) {
        return userStrategyMapper.deleteUserStrategyById(id);
    }

    @Override
    public int updateUserStrategyById(UserStrategy userStrategy) {
        return userStrategyMapper.updateUserStrategyById(userStrategy);
    }

    @Override
    public int updateUserStrategyConditionById(UserStrategy userStrategy) {
        return userStrategyMapper.updateUserStrategyConditionById(userStrategy);
    }

}
