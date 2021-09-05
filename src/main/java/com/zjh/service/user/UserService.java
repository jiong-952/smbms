package com.zjh.service.user;

import com.zjh.pojo.User;

import java.util.List;

public interface UserService {
    User login(String userCode, String userPassword);
    boolean updatePwd(int id,String password);
    int getUserCount(String userName, int userRole);
    List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize);
    boolean add(User user);
    boolean delete(Integer delId);
    boolean modify(User user);
    User getUserById(String id);
    User userCodeExist(String userCode);
}
