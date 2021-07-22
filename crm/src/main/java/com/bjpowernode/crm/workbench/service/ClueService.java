package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.Map;

/**
 * 动力节点 康云龙
 * 2021/7/5
 */
public interface ClueService {
    int saveCreateClue(Clue clue);

    Clue queryClueForDetailById(String id);

    void saveConvert(Map<String,Object> map);
}
