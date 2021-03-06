package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.domain.TranRemark;
import com.bjpowernode.crm.workbench.mapper.TranRemarkMapper;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranHistoryService;
import com.bjpowernode.crm.workbench.service.TranRemarkService;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 动力节点 康云龙
 * 2021/7/8
 */
@Controller
public class TranController {

    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TranService tranService;
    @Autowired
    private TranRemarkService tranRemarkService;

    @Autowired
    private TranHistoryService tranHistoryService;

    @RequestMapping("/workbench/transaction/typeahead.do")
    public @ResponseBody Object typeahead(String customerName){
        List<Customer> customerList=new ArrayList<>();
        Customer customer=new Customer();
        //模拟一些数据

        customer.setId("001");
        customer.setName("动力节点");
        customerList.add(customer);

        customer=new Customer();
        customer.setId("002");
        customer.setName("字节跳动");
        customerList.add(customer);

        customer=new Customer();
        customer.setId("003");
        customer.setName("国庆节");
        customerList.add(customer);

        return customerList;
    }

    @RequestMapping("/workbench/transaction/index.do")
    public String index(){
        return "workbench/transaction/index";
    }

    //添加页面
    @RequestMapping("/workbench/transaction/createTran.do")
    public String createTran(Model model){

        List<User> userList=userService.queryAllUsers();

        //阶段
        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");
        //来源
        List<DicValue> sourceList=dicValueService.queryDicValueByTypeCode("source");

        //类型
        List<DicValue> transactionTypeList=dicValueService.queryDicValueByTypeCode("transactionType");

        model.addAttribute("userList",userList);
        model.addAttribute("sourceList",sourceList);
        model.addAttribute("transactionTypeList",transactionTypeList);
        model.addAttribute("stageList",stageList);
        return "workbench/transaction/save";
    }

    //自动补全
    @RequestMapping("/workbench/transaction/queryCustomerByName.do")
    public @ResponseBody Object queryCustomerByName(String customerName){
        List<Customer> customerList=customerService.queryCustomerByName(customerName);
        return customerList;
    }

    //可能性与分值对应
    @RequestMapping("/workbench/transaction/getPossibilityByStageValue.do")
    public @ResponseBody Object getPossibilityByStageValue(String stageValue){
        ResourceBundle resourceBundle=ResourceBundle.getBundle("possibility");

        String possibility=resourceBundle.getString(stageValue);

        return possibility;
    }

    //创建交易
    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    public @ResponseBody Object saveCreateTran(Tran tran, String customerName, HttpSession session){
    User user=(User)session.getAttribute(Contants.SESSION_USER);
    tran.setId(UUIDUtils.getUUID());
    tran.setCreateBy(user.getId());
    tran.setCreateTime(DateUtils.formatDateTime(new Date()));

        Map<String,Object> map=new HashMap<>();
        map.put("tran", tran);
        map.put("customorName", customerName);
        map.put("sessionUser", user);

        ReturnObject returnObject=new ReturnObject();

        try {
            //调用
            tranService.saveCreateTran(map);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAILO);
            returnObject.setMessage("创建交易失败");
        }
        return returnObject;

    }

    //交易详情
    @RequestMapping("/workbench/transaction/detailTran.do")
    public String detailTran(String id,Model model){

        //交易详情
        Tran tran=tranService.queryTranForDetailById(id);

        ResourceBundle bundle=ResourceBundle.getBundle("possibility");
        String possibility=bundle.getString(tran.getStage());
        tran.setPossibility(possibility);

        //交易备注
        List<TranRemark> remarkList=tranRemarkService.queryTranRemarkForDetailByTranId(id);

        //取阶段历史
        List<TranHistory> tranHistories=tranHistoryService.queryTranHistoryForDetailByTranId(id);
        //交易阶段
        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");


        model.addAttribute("tran",tran);
        model.addAttribute("remarkList",remarkList);
        model.addAttribute(tranHistories);
        model.addAttribute("stageList",stageList);

        return "workbench/transaction/detail";
    }

}
