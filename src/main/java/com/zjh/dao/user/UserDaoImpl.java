package com.zjh.dao.user;

import com.mysql.jdbc.StringUtils;
import com.zjh.dao.BaseDao;
import com.zjh.pojo.User;
import com.zjh.utils.JdbcUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao{
    @Override
    public User getLoginUser(Connection connection, String userCode) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        if(connection != null){
            String sql = "select * from smbms_user where userCode=?";
            Object[] params = {userCode};
            rs = BaseDao.execute(connection, sql, rs, ps, params);
            if(rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
            }
            JdbcUtils.closeResource(null,ps,rs);
        }
        return user;
    }

    @Override
    public int updatePwd(Connection connection, int id, String password) throws SQLException {
        PreparedStatement ps = null;
        int flag = 0;
        if(connection != null){
            String sql = "update smbms_user set userPassword= ? where id = ?";
            Object[] params = {password,id};
            flag = BaseDao.execute(connection, sql, ps, params);
            JdbcUtils.closeResource(null,ps,null);
            }
        return flag;
    }

    @Override
    public int getUserCount(Connection connection, String userName, int userRole) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        if(connection != null){
            //动态sql，拼接
            StringBuffer sql = new StringBuffer();
            sql.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<>();
            if(!StringUtils.isNullOrEmpty(userName)){
                //注意空格，模糊查询
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if(userRole>0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            Object[] params = list.toArray();
            rs = BaseDao.execute(connection, sql.toString(), rs, ps, params);
            if(rs.next()){
                count = rs.getInt("count");
            }
            JdbcUtils.closeResource(null,ps,rs);
        }
        return count;
    }

    @Override
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<User> userList = new ArrayList<>();
        if(connection != null){
            //动态sql，拼接
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<>();
            if(!StringUtils.isNullOrEmpty(userName)){
                //注意空格，模糊查询
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if(userRole>0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            //分页处理+排序
            sql.append(" order by creationDate DESC limit ?,?");
            //传入的currentPageNo是页面上的，需要转化成数据库的
            currentPageNo = (currentPageNo-1)*pageSize;
            list.add(currentPageNo);
            list.add(pageSize);
            Object[] params = list.toArray();
            System.out.println(sql.toString());
            rs = BaseDao.execute(connection, sql.toString(), rs, ps, params);
            while (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setUserRole(rs.getInt("userRole"));
                user.setUserRoleName(rs.getString("userRoleName"));
                userList.add(user);
            }
            JdbcUtils.closeResource(null,ps,rs);
        }
        return userList;
    }

    @Override
    public int add(Connection connection, User user) throws SQLException {
        PreparedStatement ps = null;
        int flag = 0;
        if(connection != null){
            String sql = "insert into smbms_user (userCode,userName,userPassword," +
                    "userRole,gender,birthday,phone,address,creationDate,createdBy) " +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {user.getUserCode(),user.getUserName(),user.getUserPassword(),
                    user.getUserRole(),user.getGender(),user.getBirthday(),
                    user.getPhone(),user.getAddress(),user.getCreationDate(),user.getCreatedBy()};
            flag = BaseDao.execute(connection, sql, ps, params);
            JdbcUtils.closeResource(null,ps,null);
        }
        return flag;
    }

    @Override
    public int delete(Connection connection, Integer delId) throws SQLException {
        PreparedStatement ps = null;
        int flag = 0;
        if(connection != null){
            String sql = "delete from smbms_user where id=?";
            Object[] params = {delId};
            flag = BaseDao.execute(connection, sql, ps, params);
            JdbcUtils.closeResource(null,ps,null);
        }
        return flag;
    }

    @Override
    public int modify(Connection connection, User user) throws SQLException {
        PreparedStatement ps = null;
        int flag = 0;
        if(connection != null){
            String sql = "update smbms_user set userName=?,"+
                    "gender=?,birthday=?,phone=?,address=?,userRole=?,modifyBy=?,modifyDate=? where id = ? ";
            Object[] params = {user.getUserName(),user.getGender(),user.getBirthday(),
                    user.getPhone(),user.getAddress(),user.getUserRole(),user.getModifyBy(),
                    user.getModifyDate(),user.getId()};
            flag = BaseDao.execute(connection, sql, ps, params);
            JdbcUtils.closeResource(null,ps,null);
        }
        return flag;
    }

    @Override
    public User getUserById(Connection connection, String id) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        if(connection != null){
            String sql = "select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.id=? and u.userRole = r.id";
            Object[] params = {id};
            rs = BaseDao.execute(connection, sql, rs, ps, params);
            if(rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
                user.setUserRoleName(rs.getString("userRoleName"));
            }
            JdbcUtils.closeResource(null,ps,rs);
        }
        return user;
    }


}
