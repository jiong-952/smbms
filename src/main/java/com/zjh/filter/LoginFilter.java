package com.zjh.filter;

import com.zjh.utils.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp =(HttpServletResponse) response;
        Object user_session = req.getSession().getAttribute(Constants.USER_SESSION);
        if(user_session==null){
            resp.sendRedirect(req.getContextPath()+"/error.jsp");
        }else {
            filterChain.doFilter(req,resp);
        }
    }

    @Override
    public void destroy() {

    }
}
