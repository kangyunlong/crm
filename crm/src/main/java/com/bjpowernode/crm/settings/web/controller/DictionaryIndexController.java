package com.bjpowernode.crm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 动力节点 康云龙
 * 2021/6/27
 */
@Controller
public class DictionaryIndexController {
    @RequestMapping("/settings/dictionary/index.do")
    public String index(){
        return "settings/dictionary/index";
    }

}
