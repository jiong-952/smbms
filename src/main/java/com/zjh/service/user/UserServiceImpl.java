package com.zjh.service.user;

import com.zjh.dao.user.UserDao;
import com.zjh.dao.user.UserDaoImpl;
import com.zjh.pojo.User;
import com.zjh.utils.JdbcUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService{
    private UserDao userDao;
    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    @Override
    public User login(String userCode, String userPassword) {
        Connection connection = null;
        User user = null;
        connection = JdbcUtils.getConnection();
        try {
            user = userDao.getLoginUser(connection, userCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.closeResource(connection,null,null);
        }
        //匹配密码
        if(user!=null){
            if(!user.getUserPassword().equals(userPassword)){
                user = null;
            }
        }
        return user;
    }

    @Override
    public boolean updatePwd(int id, String password) {
        Connection connection = null;
        connection = JdbcUtils.getConnection();
        boolean flag = false;
        try {
            if(userDao.updatePwd(connection,id,password)>0){
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.closeResource(connection,null,null);
        }
        return flag;
    }

    @Override
    public int getUserCount(String userName, int userRole) {

        Connection connection = null;
        int count = 0;
        connection = JdbcUtils.getConnection();
        try {
            count= userDao.getUserCount(connection, userName, userRole);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.closeResource(connection,null,null);
        }
        return count;
    }

    @Override
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
        Connection connection = null;
        connection = JdbcUtils.getConnection();
        List<User> userList = new ArrayList<>();
        try {
            userList= userDao.getUserList(connection, userName, userRole, currentPageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResource(connection,null,null);
        }
        return userList;
    }

    //增删改查要加事务
    @Override
    public boolean add(User user) {
        Connection connection = null;
        connection = JdbcUtils.getConnection();
        boolean flag = false;
        try {
            connection.setAutoCommit(false);//关闭自动提交
            int updateRows = userDao.add(connection, user);
            connection.commit();
            if(updateRows>0){
                flag = true;
                System.out.println("add Succeed!");
            }else {
                System.out.println("add failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("rollback========");
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            JdbcUtils.closeResource(connection,null,null);
        }
        return flag;
    }

    @Override
    public boolean delete(Integer delId) {
        Connection connection = null;
        connection = JdbcUtils.getConnection();
        boolean flag = false;
        try {
            connection.setAutoCommit(false);//关闭自动提交
            int updateRows = userDao.delete(connection,delId);
            connection.commit();
            if(updateRows>0){
                flag = true;
                System.out.println("delete Succeed!");
            }else {
                System.out.println("delete failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("rollback========");
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            JdbcUtils.closeResource(connection,null,null);
        }
        return flag;
    }

    @Override
    public boolean modify(User user) {
        Connection connection = null;
        connection = JdbcUtils.getConnection();
        boolean flag = false;
        try {
            connection.setAutoCommit(false);//关闭自动提交
            int updateRows = userDao.modify(connection,user);
            connection.commit();
            if(updateRows>0){
                flag = true;
                System.out.println("update Succeed!");
            }else {
                System.out.println("update failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("rollback========");
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            JdbcUtils.closeResource(connection,null,null);
        }
        return flag;
    }

    @Override
    public User getUserById(String id) {
        Connection connection = null;
        User user = null;
        connection = JdbcUtils.getConnection();
        try {
            user = userDao.getUserById(connection,id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            user = null;
        }finally {
            JdbcUtils.closeResource(connection,null,null);
        }
        return user;
    }

    @Override
    public User userCodeExist(String userCode) {
        Connection connection = null;
        User user = null;
        connection = JdbcUtils.getConnection();
        try {
            user = userDao.getLoginUser(connection,userCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.closeResource(connection,null,null);
        }
        return user;
    }
}
