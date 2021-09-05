package com.zjh.service.role;

import com.zjh.dao.role.RoleDao;
import com.zjh.dao.role.RoleDaoImpl;
import com.zjh.pojo.Role;
import com.zjh.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleServiceImpl implements RoleService{
    private RoleDao roleDao;
    public RoleServiceImpl() {
        roleDao = new RoleDaoImpl();
    }

    @Override
    public List<Role> getRoleList() {
        Connection connection = null;
        connection = JdbcUtils.getConnection();
        List<Role> roleList = new ArrayList<>();
        try {
            roleList = roleDao.getRoleList(connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.closeResource(connection,null,null);
        }
        return roleList;
    }
}
