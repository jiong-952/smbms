package com.zjh.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.zjh.pojo.Role;
import com.zjh.pojo.User;
import com.zjh.service.role.RoleService;
import com.zjh.service.role.RoleServiceImpl;
import com.zjh.service.user.UserService;
import com.zjh.service.user.UserServiceImpl;
import com.zjh.utils.Constants;
import com.zjh.utils.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {
    private UserService userService;
    public UserServlet() {
        userService = new UserServiceImpl();
    }

    //servlet复用
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method!=null&&method.equals("savepwd")){
            this.updatePwd(req,resp);
        }else if(method!=null&&method.equals("pwdmodify")){
            this.pwdmodify(req,resp);
        }else if(method!=null&&method.equals("query")){
            this.query(req,resp);
        }else if(method!=null&&method.equals("view")){  //查看先用id查用户
            this.getUserById(req,resp,"userview.jsp");
        }else if(method!=null&&method.equals("add")){
            this.add(req,resp);
        }else if(method!=null&&method.equals("deluser")){
            this.delete(req,resp);
        }else if(method!=null&&method.equals("modify")){ //修改先用id查用户
            this.getUserById(req,resp,"usermodify.jsp");
        }else if(method!=null&&method.equals("modifyexe")){
            this.modify(req,resp);
        }else if(method != null && method.equals("getrolelist")){
            this.getRoleList(req, resp);
        }else if(method != null && method.equals("ucexist")){
            this.userCodeExist(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    //改密码
    private void updatePwd(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        boolean flag = false;
        if(o!=null&&!StringUtils.isNullOrEmpty(newpassword)){
            flag=userService.updatePwd(((User)o).getId(),newpassword);
            if(flag){
                    req.setAttribute(Constants.MESSAGE,"修改密码成功，请退出并使用新密码重新登录!");
                    req.getSession().removeAttribute(Constants.USER_SESSION);//注销session
            }else {
                req.setAttribute(Constants.MESSAGE,"修改密码失败");
            }
        }else {
            req.setAttribute(Constants.MESSAGE,"新密码有问题");
        }
        req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
    }
    //查旧密码
    private void pwdmodify(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");
        Map<String, String> resultMap = new HashMap<>();
        if(o==null){
            //session过期了
            resultMap.put("result","sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){
            //旧密码输入为空
            resultMap.put("result","error");
        }else {
            String userPassword = ((User) o).getUserPassword();
            if(oldpassword.equals(userPassword)){
                //旧密码正确
                resultMap.put("result","true");
            }else {
                //旧密码错误
                resultMap.put("result","false");
            }
        }
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }
    //用户管理查询
    private void query(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        //获取前端参数
        String queryUserName = req.getParameter("queryname");
        String tempUserRole = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        //设定角色以及页码的初始值
        int queryUserRole = 0;
        int currentPageNo = 1;
        int totalPageCount = 1;
        if(queryUserName == null){
            queryUserName = "";
        }
        if(!StringUtils.isNullOrEmpty(tempUserRole)){
            queryUserRole = Integer.parseInt(tempUserRole);
        }
        if(pageIndex !=null){
            try{
                currentPageNo = Integer.parseInt(pageIndex);
            }catch (NumberFormatException e){
                resp.sendRedirect("error.jsp");
            }
        }
        //获得总数量
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        //分页处理
        PageSupport pageSupport = new PageSupport();
        pageSupport.setPageSize(Constants.PAGESIZE);
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setTotalCount(totalCount);
        currentPageNo = pageSupport.getCurrentPageNo();
        totalPageCount = pageSupport.getTotalPageCount();
        System.out.println(queryUserName);
        System.out.println(queryUserRole);
        System.out.println(currentPageNo);
        //获得用户列表
        List<User> userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo,Constants.PAGESIZE);
        System.out.println(userList);
        //获得角色列表
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();

        req.setAttribute("userList",userList);
        req.setAttribute("roleList",roleList);
        req.setAttribute("queryUserName",queryUserName);
        req.setAttribute("queryUserRole",queryUserRole);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.getRequestDispatcher("userlist.jsp").forward(req,resp);
    }
    //增加用户
    private void add(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        //获取前端参数
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");
        //封装数据
        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        if(userService.add(user)){
            //成功返回用户列表
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else {
            //失败刷新
            req.getRequestDispatcher("useradd.jsp").forward(req, resp);
        }

    }
    //删除用户
    private void delete(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        //获取前端参数
        String id = req.getParameter("uid");
        Integer delId = 0;
        //手动抛出异常
        try {
            delId = Integer.parseInt(id);
        }catch (Exception e){
            delId = 0;
        }
        Map<String, String> resultMap = new HashMap<>();
        if(delId <= 0){
            resultMap.put("delResult", "notexist");
        }else {
            if(userService.delete(delId)){
                resultMap.put("delResult", "true");
            }else {
                resultMap.put("delResult", "false");
            }
        }
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }
    //更新用户
    private void modify(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        //获取前端参数
        String id = req.getParameter("uid");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");
        //封装数据
        User user = new User();
        user.setId(Integer.valueOf(id));
        user.setUserName(userName);
        user.setGender(Integer.valueOf(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());
        if(userService.modify(user)){
            //成功返回用户列表
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else {
            //失败刷新
            req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
        }

    }
    //通过用户id查看用户信息,用户管理界面处理单个用户
    private void getUserById(HttpServletRequest req, HttpServletResponse resp,String url)throws ServletException, IOException{
        //获取前端参数
        String id = req.getParameter("uid");
        if(!StringUtils.isNullOrEmpty(id)){
            User user = userService.getUserById(id);
            req.setAttribute("user",user);
            req.getRequestDispatcher(url).forward(req,resp);
        }
    }
    //添加用戶角色下拉
    private void getRoleList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Role> roleList = null;
        RoleService roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();
        //把roleList转换成json对象输出
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(roleList));
        writer.flush();
        writer.close();
    }
    //添加用戶提交驗證用戶（其實可以設計成输入用户名之后就验证）
    private void userCodeExist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取前端参数
        String userCode = req.getParameter("userCode");
        Map<String, String> resultMap = new HashMap<>();
        if(StringUtils.isNullOrEmpty(userCode)){
            resultMap.put("userCode", "exist");
        }else{
            UserService userService = new UserServiceImpl();
            User user = userService.userCodeExist(userCode);
            if(null != user){
                resultMap.put("userCode","exist");
            }else{
                resultMap.put("userCode", "notexist");
            }
        }
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }
}
