package com.hassdata.survey.controller.information;

import com.hassdata.survey.dto.UserDTO;
import com.hassdata.survey.po.Admin_User;
import com.hassdata.survey.po.Student;
import com.hassdata.survey.po.User;
import com.hassdata.survey.service.ScoreService;
import com.hassdata.survey.service.StudentService;
import com.hassdata.survey.service.UserService;
import com.hassdata.survey.util.MD5TUtils;
import com.hassdata.survey.util.ServerResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("system")
@Scope("prototype")
public class InformationCenterController {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Resource
    private UserService userService;

    @Resource
    private StudentService studentService;

    @Resource
    private ScoreService scoreService;

    @RequestMapping(value = "info" , method = RequestMethod.GET)
    public String getInformationCenter(){
        return "system/information/informationcenter";
    }


    @RequestMapping(value = "getInfoList",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getInfoList(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer limit){
        if (page == null || limit == null) {
            page = 1;
            limit = 30;
        }
        long count=userService.getScrollCount(null);
        List<User> userList=userService.getScrollData(null,"id DESC",(page-1)*limit,limit);
        List<UserDTO> userDTOList=new ArrayList<>();
        UserDTO userDTO=null;
        int aid=0;
        setUserDTO(userList, userDTOList, aid);
        return ServerResponse.createBySuccessForLayuiTable("请求成功",userDTOList,count);
    }


    @RequestMapping(value = "searchInfoList",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse searchInfoList(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit, HttpServletRequest request){
        if (page == null || limit == null) {
            page = 1;
            limit = 30;
        }
        String name=null;
        try {
            name=new String(request.getParameter("schoolname").getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        User user=new User();
        user.setAccount("%"+name+"%");
        long count=userService.getScrollByLikeCount(user);
        List<User> userList=userService.getScrollDataByLike(user,"id DESC",(page-1)*limit,limit);
        List<UserDTO> userDTOList=new ArrayList<>();
        UserDTO userDTO=null;
        int aid=0;
        setUserDTO(userList, userDTOList, aid);
        return ServerResponse.createBySuccessForLayuiTable("请求成功",userDTOList,count);
    }

    private void setUserDTO(List<User> userList, List<UserDTO> userDTOList, int aid) {
        UserDTO userDTO;
        for (User u : userList){
            if(u.getStatus()!=1) continue;
            userDTO=new UserDTO();
            aid++;
            userDTO.setId(u.getId());
            userDTO.setAid(aid);
            userDTO.setSchoolname(u.getSchoolname());
            userDTO.setHeadmaster(u.getHeadmaster());
            userDTO.setAddress(u.getAddress());
            userDTO.setPlayhousename(u.getPlayhousename());
            userDTO.setBooknumber(u.getBooknumber());
            Student student=new Student();
            student.setUid(u.getId());
            userDTO.setChildrennumber(studentService.getScrollCount(student));
            userDTO.setWithquestionnairenumber(scoreService.getUserWithQuestionnaireNumber(u.getId()).size());
            if(u.getLastlogintime()==null){
                userDTO.setLastlogintime("该用户暂未登录");
            }else{
                userDTO.setLastlogintime(format.format(u.getLastlogintime()));
            }
            userDTO.setRemarks(u.getRemarks());
            userDTOList.add(userDTO);
        }
    }

    @RequestMapping(value = "userInfoDel" , method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse userInfoDel(Integer uid){
        if(uid==null){
            return ServerResponse.createByErrorMessage("操作失败!");
        }
        if(userService.delete(uid)>0){
            return ServerResponse.createBySuccessMessage("删除成功！");
        }else{
            return ServerResponse.createByErrorMessage("操作失败!");
        }
    }


    @RequestMapping(value = "addUser" , method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addUser(User user,HttpServletRequest request){
        HttpSession session=request.getSession(true);
        if(user.getAccount().isEmpty()){
            return ServerResponse.createByErrorMessage("用户名不能为空!");
        }
        if(user.getHeadmaster().isEmpty()){
            return ServerResponse.createByErrorMessage("校长名称不能为空!");
        }
        if(user.getAddress().isEmpty()){
            return ServerResponse.createByErrorMessage("学校地址不能为空!");
        }
        if(user.getPlayhousename().isEmpty()){
            return ServerResponse.createByErrorMessage("留守儿童之家名称不能为空!");
        }
        if(user.getBooknumber()==null){
            return ServerResponse.createByErrorMessage("图书数量不能为空!");
        }
        user.setSchoolname(user.getAccount());
        user.setOperationuser(((Admin_User)session.getAttribute("CurrentAdminUser")).getId());
        user.setStatus(1);
        user.setPassword(MD5TUtils.threeMD5("123456"));
        userService.save(user);
        return ServerResponse.createBySuccessMessage("用戶创建成功");
    }

    @RequestMapping(value = "getUserAdd" , method = RequestMethod.GET)
    public String getAddUser(){
        return "system/information/addUser";
    }



}
