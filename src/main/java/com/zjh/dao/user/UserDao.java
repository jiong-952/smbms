package com.zjh.dao.user;

import com.zjh.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    //获得登录信息
    User getLoginUser(Connection connection,String userCode) throws SQLException;
    //修改密码
    int updatePwd(Connection connection,int id,String password) throws SQLException;
    //查询用户总数
    int getUserCount(Connection connection,String userName,int userRole)throws SQLException;
    //查询用户列表
    List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize)throws SQLException;
    //增加用户
    int add(Connection connection, User user)throws SQLException;
    //删除用户
    int delete(Connection connection, Integer delId)throws SQLException;
    //更新用户
    int modify(Connection connection, User user)throws SQLException;
    //通过用户id查看用户
    User getUserById(Connection connection,String id) throws SQLException;
}
