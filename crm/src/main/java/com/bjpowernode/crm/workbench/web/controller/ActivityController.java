package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * 动力节点 康云龙
 * 2021/6/30
 */
@Controller
public class ActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(Model model){
        List<User> userList= userService.queryAllUsers();    model.addAttribute("userList",userList);
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/queryActivityForPageByCondition.do")
    public @ResponseBody Object queryActivityForPageByCondition(int pageNo,int pageSize,String name,String owner,String startDate,String endDate){
        Map<String,Object> map =new HashMap<>();
        map.put("beginNo", (pageNo-1)*pageSize);
        map.put("pageSize", pageSize);
        map.put("name",name );
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);

        List<Activity> activityList=activityService.queryActivityForPageByCondition(map);

        //得到总记数
        long totalRows=activityService.queryCountOfActivityByCondition(map);

        Map<String,Object> retMap = new HashMap<>();
        retMap.put("activityList",activityList);
        retMap.put("totalRows",totalRows);

        return retMap;

    }
    //创建按钮
    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    public @ResponseBody Object saveCreateActivity(Activity activity, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);

        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));
        activity.setCreateBy(user.getId());

        ReturnObject returnObject= new ReturnObject();
        int ret=activityService.saveCreateActivity(activity);

        if (ret>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAILO);
            returnObject.setMessage("保存失败");
        }
        return returnObject;
    }

    //跳转便捷页面（修改按钮）
    @RequestMapping("/workbench/activity/editActivity.do")
    public @ResponseBody Object editActivity(String id){
        Activity activity=activityService.queryActivityById(id);
        return activity;
    }

    //更新按钮
    @RequestMapping("/workbench/activity/saveEditActivity.do")
    public @ResponseBody Object saveEditActivity(Activity activity,HttpSession session){
        User user=(User) session.getAttribute(Contants.SESSION_USER);
        activity.setEditBy(user.getId());
        activity.setEditTime(DateUtils.formatDateTime(new Date()));

        ReturnObject returnObject= new ReturnObject();
        int ret=activityService.saveEditActivity(activity);

        if (ret>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAILO);
            returnObject.setMessage("更新失败");
        }
        return returnObject;
    }

    //删除
    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    public @ResponseBody Object deleteActivityByIds(String[] id){
        ReturnObject returnObject= new ReturnObject();
        int ret=activityService.deleteActivityByIds(id);

        if (ret>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAILO);
            returnObject.setMessage("删除失败");
        }
        return returnObject;

    }

    //批量导出
    @RequestMapping("/workbench/activity/exportAllActivity.do")
    public void exportAllActivity(HttpServletResponse response) throws IOException {
        //从数据库获取所有的市场活动对象
      List<Activity> activityList= activityService.queryAllActivityForDetail();

      //创建excel
        HSSFWorkbook wb=new HSSFWorkbook();
        HSSFSheet sheet=wb.createSheet("市场活动表");
        //第一行
        HSSFRow row=sheet.createRow(0);
        //第一个单元格
        HSSFCell cell=row.createCell(0);
        cell.setCellValue("ID");
        //第二个单元格
        cell=row.createCell(1);
        cell.setCellValue("所有者");
        //第3个单元格
        cell=row.createCell(2);
        cell.setCellValue("名称");

        //第4个单元格
        cell=row.createCell(3);
        cell.setCellValue("开始日期");
        //第5个单元格
        cell=row.createCell(4);
        cell.setCellValue("结束日期");
        //第6个单元格
        cell=row.createCell(5);
        cell.setCellValue("成本");
        //第7个单元格
        cell=row.createCell(6);
        cell.setCellValue("描述");

        //样式对象
        HSSFCellStyle style=wb.createCellStyle();
        //居中对齐
        style.setAlignment(HorizontalAlignment.CENTER);
        //遍历集合，将集合中每一个活动对象取出放到excel中每一行
        if(activityList!=null){
            Activity activity=null;
            for (int i=0;i<activityList.size();i++){
                activity=activityList.get(i);
                //excel中的行
                row=sheet.createRow(i+1);
                //i行第一列
                cell=row.createCell(0);
                cell.setCellValue(activity.getId());
                //i行第二列
                cell=row.createCell(1);
                cell.setCellValue(activity.getOwner());
                //i行第3列
                cell=row.createCell(2);
                cell.setCellValue(activity.getName());
                //i行第4列
                cell=row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                //i行第5列
                cell=row.createCell(4);
                cell.setCellValue(activity.getStartDate());
                //i行第6列
                cell=row.createCell(5);
                cell.setCellValue(activity.getEndDate());
                //i行第7列
                cell=row.createCell(6);
                cell.setCellValue(activity.getDescription());

            }
        }

        //下载
        //设置响应类型，默认情况，浏览器返回html文件，流
        response.setContentType("application/octet-stream;charset=UTF-8");
        //中文文件名
        String fileName= URLEncoder.encode("市场活动", "UTF-8");

        response.addHeader("Content-Disposition","attachment;filename="+fileName+".xls");

        OutputStream os =response.getOutputStream();
        wb.write(os);//输出位置由浏览器决定
        os.flush();
        os.close();


    }

    //上传表单
    @RequestMapping("/workbench/activity/fileUpload.do")
    public @ResponseBody Object fileUpload(String username, MultipartFile myfile) throws IOException {
        //文件名
        String filename= myfile.getOriginalFilename();
        File file=new File("d:\\testDir",filename);
        //将文件保存指定目录
        myfile.transferTo(file);

        ReturnObject returnObject=new ReturnObject();
        returnObject.setMessage("上传成功");
        return returnObject;
    }

    @RequestMapping("/workbench/activity/importActivity.do")
    public @ResponseBody Object importActivity(MultipartFile activityFile,String username,HttpSession session){
        User user=(User)session.getAttribute(Contants.SESSION_USER);
        Map<String,Object> retMap=new HashMap<>();

        try {
            //用来接收excel文件
            List<Activity> activityList=new ArrayList<>();
            //将上传的excel文件转成一个输入源
            InputStream is = activityFile.getInputStream();
            HSSFWorkbook wb=new HSSFWorkbook(is);
            HSSFSheet sheet=wb.getSheetAt(0);
            HSSFRow row=null;
            HSSFCell cell=null;
            Activity activity=null;

            //读取行（跳过标题）
            for (int i=1;i<=sheet.getLastRowNum();i++){
                row=sheet.getRow(i);
                activity=new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateBy(user.getId());
                activity.setCreateTime(DateUtils.formatDateTime(new Date()));

                //列
                for (int j =0;j<row.getLastCellNum();j++){
                    cell=row.getCell(j);
                    String cellValue=getCellValue(cell);
                    if (j==0){
                        activity.setName(cellValue);
                    }else if (j==1){
                        activity.setStartDate(cellValue);
                    }else if (j==2){
                        activity.setEndDate(cellValue);
                    }else if(j==3){
                        activity.setCost(cellValue);
                    }else if(j==4){
                        activity.setDescription(cellValue);
                    }
                }

                //将市场活动对象放到市场活动集合
                activityList.add(activity);



            }

            int ret=activityService.saveCreateActivityByList(activityList);
            retMap.put("code", Contants.RETURN_OBJECT_CODE_SUCCESS);
            retMap.put("count", ret);


        } catch (IOException e) {
            e.printStackTrace();
            retMap.put("code", Contants.RETURN_OBJECT_CODE_FAILO);
            retMap.put("message","录入文件失败");
        }
        return retMap;


    }

    //判断单元格类型用对应的方法来获取值,并转成string类型
    public static String getCellValue(HSSFCell cell){
        String ret="";
        switch (cell.getCellType()){
            case HSSFCell.CELL_TYPE_STRING:
                ret=cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                ret=cell.getBooleanCellValue()+"";
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                ret=cell.getNumericCellValue()+"";
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                ret=cell.getCellFormula()+"";
                break;
            default:
                ret="";

        }
        return ret;
    }
}
