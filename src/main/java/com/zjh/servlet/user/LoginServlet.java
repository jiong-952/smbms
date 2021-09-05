package com.zjh.servlet.user;

import com.zjh.pojo.User;
import com.zjh.service.user.UserService;
import com.zjh.service.user.UserServiceImpl;
import com.zjh.utils.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private UserService userService;
    public LoginServlet() {
        userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        User user = userService.login(userCode, userPassword);
        if(null!=user){
            //登录成功
            req.getSession().setAttribute(Constants.USER_SESSION,user);
            System.out.println(user);
            //页面跳转
            resp.sendRedirect("jsp/frame.jsp");
        }else {
            //登录失败
            req.setAttribute("error","用户名或密码错误");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
