package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.DicType;

import java.util.List;

/**
 * 动力节点 康云龙
 * 2021/6/27
 */
public interface DicTypeService {

    List<DicType> queryAllDicType();

    DicType queryDicTypeByCode(String code);

    //保存
    int saveCreateDicType(DicType dicType);

    int deleteDicTypeByCodes(String[] codes);

    //更新
    int saveEditDicType(DicType dicType);



}
