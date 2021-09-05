package com.zjh.service.user;

import com.zjh.dao.user.UserMapper;
import com.zjh.pojo.User;
import com.zjh.utils.JdbcUtils;
import com.zjh.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService{
    private UserMapper userMapper;
    @Override
    public User login(String userCode, String userPassword) {
        User user = null;
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        user = mapper.getLoginUser(userCode);
        //匹配密码
        if(user!=null){
            if(!user.getUserPassword().equals(userPassword)){
                user = null;
            }
        }
        sqlSession.close();
        return user;
    }


    @Override
    public boolean updatePwd(int id, String password) {
        boolean flag = false;
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        if (mapper.updatePwd(id, password) > 0) {
            flag = true;
        }
        sqlSession.close();
        return flag;
    }
    @Override
        public int getUserCount(String userName, int userRole) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int count = 0;
        count= mapper.getUserCount(userName, userRole);
        sqlSession.close();
        return count;
    }

    @Override
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = new ArrayList<>();
        userList= mapper.getUserList(userName, userRole, currentPageNo, pageSize);
        sqlSession.close();
        System.out.println(userList);
        return userList;
    }

    //增删改查要加事务
    @Override
    public boolean add(User user) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        boolean flag = false;
        int updateRows = mapper.add(user);
        sqlSession.commit();
        if(updateRows>0){
            flag = true;
            System.out.println("add Succeed!");
        }else {
            System.out.println("add failed!");
        }
        sqlSession.close();
        return flag;
    }

    @Override
    public boolean delete(Integer delId) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        boolean flag = false;
        int updateRows = mapper.delete(delId);
        sqlSession.commit();
        if(updateRows>0){
            flag = true;
            System.out.println("delete Succeed!");
        }else {
            System.out.println("delete failed!");
        }
        sqlSession.close();
        return flag;
    }

    @Override
    public boolean modify(User user) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        boolean flag = false;
        int updateRows = mapper.modify(user);
        sqlSession.commit();
        if(updateRows>0){
            flag = true;
            System.out.println("update Succeed!");
        }else {
            System.out.println("update failed!");
        }
        sqlSession.close();
        return flag;
    }

    @Override
    public User getUserById(String id) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = null;
        user = mapper.getUserById(id);
        sqlSession.close();
        return user;
    }

    @Override
    public User userCodeExist(String userCode) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = null;
        user = mapper.getLoginUser(userCode);
        sqlSession.close();
        return user;
    }
}
