package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.mapper.TranHistoryMapper;
import com.bjpowernode.crm.workbench.mapper.TranMapper;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 动力节点 康云龙
 * 2021/7/9
 */
@Service
public class TranServiceImpl implements TranService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    @Override
    public int saveCreateTran(Map<String, Object> map) {
        Tran tran=(Tran)map.get("tran");
        String customerId=tran.getCustomerId();
        String customerName=(String)map.get("customerName");
        User user=(User)map.get("sessionUser");

        //向客户表插入记录
        if (customerId==null||customerId.trim().length()==0){
            Customer customer=new Customer();
            customer.setId(UUIDUtils.getUUID());
            customer.setOwner(user.getId());
            customer.setName(customerName);
            customer.setCreateBy(user.getId());
            customer.setCreateTime(DateUtils.formatDateTime(new Date()));

            //保存客户
            int ret=customerMapper.insertCustomer(customer);
            //在输入tran时，要将customerId赋给tran
            tran.setCustomerId(customer.getId());
        }

        //保存交易
        tranMapper.insertTran(tran);
        //保存到交易历史表
        TranHistory tranHistory=new TranHistory();
        tranHistory.setId(UUIDUtils.getUUID());
        tranHistory.setStage(tran.getStage());
        tranHistoryMapper.insertTranHistory(tranHistory);



        return 0;
    }

    @Override
    public Tran queryTranForDetailById(String id) {
        return tranMapper.selectTranForDetailById(id);
    }
}
