package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.Map;

/**
 * 动力节点 康云龙
 * 2021/7/9
 */
public interface TranService {
    int saveCreateTran(Map<String,Object> map);

    Tran queryTranForDetailById(String id);
}
