package com.zjh.dao.user;

import com.zjh.pojo.User;
import org.apache.ibatis.annotations.Param;


import java.util.List;

public interface UserMapper {
    //获得登录信息
    User getLoginUser(@Param("userCode") String userCode) ;
    //修改密码
    int updatePwd(@Param("id") int id, @Param("password") String password) ;
    //查询用户总数
    int getUserCount(@Param("userName") String userName, @Param("userRole") int userRole);
    //查询用户列表
    List<User> getUserList(@Param("userName") String userName, @Param("userRole") int userRole, @Param("currentPageNo") int currentPageNo, @Param("pageSize") int pageSize);
    //增加用户
    int add(User user);
    //删除用户
    int delete(Integer delId);
    //更新用户
    int modify(User user);
    //通过用户id查看用户
    User getUserById(@Param("id") String id);
}
