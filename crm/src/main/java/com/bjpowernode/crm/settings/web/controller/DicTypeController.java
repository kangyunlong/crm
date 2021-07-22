package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.service.DicTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 动力节点 康云龙
 * 2021/6/28
 */
@Controller
public class DicTypeController {

    @Autowired
    private DicTypeService dicTypeService;

    @RequestMapping("/settings/dictionary/type/index.do")
    public String index(Model model){
        List<DicType> dicTypeList= dicTypeService.queryAllDicType();
        model.addAttribute("dicTypeList",dicTypeList);
        return "settings/dictionary/type/index";
    }

    @RequestMapping("/settings/dictionary/type/toSave.do")
    public String toSave(){ 

        return "settings/dictionary/type/save";
    }

    @RequestMapping("/settings/dictionary/type/checkCode.do")
    public @ResponseBody Object checkCode(String code){
        DicType dicType= dicTypeService.queryDicTypeByCode(code);
        ReturnObject returnObject=new ReturnObject();

        if (dicType==null){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAILO);
            returnObject.setMessage("编码已经存在");
        }
        return returnObject;
    }

    @RequestMapping("/settings/dictionary/type/saveCreateDicType.do")
    public @ResponseBody Object saveCreateDicType(DicType dicType){
       int ret=dicTypeService.saveCreateDicType(dicType);
        ReturnObject returnObject=new ReturnObject();

       if (ret>0){
           returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
       }else {
           returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAILO);
           returnObject.setMessage("保存失败");
       }
        return returnObject;
    }

    @RequestMapping("/settings/dictionary/type/editDicType.do")
    public String editDicType(String code,Model model){
        DicType dicType = dicTypeService.queryDicTypeByCode(code);
        model.addAttribute("dicType",dicType);

        return "settings/dictionary/type/edit";
    }

    @RequestMapping("/settings/dictionary/type/saveEditDicType.do")
    public @ResponseBody Object saveEditDicType(DicType dicType){
        int ret=dicTypeService.saveEditDicType(dicType);
        ReturnObject returnObject=new ReturnObject();

        if (ret>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAILO);
            returnObject.setMessage("更新失败");
        }
        return returnObject;
    }

    @RequestMapping("/settings/dictionary/type/deleteDicTypeByCodes.do")
    public @ResponseBody Object deleteDicTypeByCodes (String[] code){
        int ret=dicTypeService.deleteDicTypeByCodes(code);
        ReturnObject returnObject=new ReturnObject();

        if (ret>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAILO);
            returnObject.setMessage("删除失败");
        }
        return returnObject;
    }

}
