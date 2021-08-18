package com.zjh.dao.role;

import com.zjh.dao.BaseDao;
import com.zjh.pojo.Role;
import com.zjh.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao{
    @Override
    public List<Role> getRoleList(Connection connection) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Role> roleList = new ArrayList<>();
        if(connection != null){
            String sql = "select * from smbms_role";
            //不能写null，会报空指针异常
            Object[] params = {};
            rs = BaseDao.execute(connection, sql.toString(), rs, ps, params);
            while(rs.next()){
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleCode(rs.getString("roleCode"));
                role.setRoleName(rs.getString("roleName"));
                roleList.add(role);
            }
            JdbcUtils.closeResource(null,ps,rs);
        }
        return roleList;
    }
}
