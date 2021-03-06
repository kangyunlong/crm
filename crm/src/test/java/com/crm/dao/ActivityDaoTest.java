package com.crm.dao;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * 动力节点 康云龙
 * 2021/6/30
 */
public class ActivityDaoTest extends BaseTest {

    @Autowired
    private ActivityMapper activityMapper;
    @Test
    public void testSelectActivityForPageByCondition(){
        HashMap map=new HashMap<>();
        map.put("beginNo",0);
        map.put("pageSize",10);
        map.put("name","%%");
        map.put("owner","%%");
        map.put("startDate","2021-06-01");
        map.put("endDate","2021-09-12");

        List<Activity> activityList=activityMapper.selectActivityForPageByCondition(map);
        System.out.println(activityList.size());
    }

    @Test
    public void testSelectCountByCondition(){
        HashMap map = new HashMap();
        long count = activityMapper.selectCountOfActivityByCondition(map);
        System.out.println(count);
    }
}
