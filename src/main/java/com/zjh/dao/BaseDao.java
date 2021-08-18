package com.zjh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseDao {
    //查询
    public static ResultSet execute(Connection conn, String sql, ResultSet rs, PreparedStatement ps,Object... params) throws SQLException {
        ps=conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i+1,params[i]);
        }
        rs=ps.executeQuery();
        return rs;
    }
    //增删改查
    public static int execute(Connection conn, String sql, PreparedStatement ps,Object... params) throws SQLException {
        ps=conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i+1,params[i]);
        }
        int updateRows = ps.executeUpdate();
        return updateRows;
    }
}
