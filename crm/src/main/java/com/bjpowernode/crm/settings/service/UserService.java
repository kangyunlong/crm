package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.TblUser;
import com.bjpowernode.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

/**
 * 动力节点 康云龙
 * 2021/6/23
 */
public interface UserService {

    public TblUser selectById(String id);

    User queryUserByLoginActAndPwd(Map<String,Object> map);

    List<User> queryAllUsers();
}
