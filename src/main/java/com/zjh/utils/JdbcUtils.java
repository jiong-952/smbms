package com.zjh.utils;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JdbcUtils {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;
    //静态加载
    static {
        Properties properties = new Properties();
        //用类加载器读取资源
        InputStream is = JdbcUtils.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver = properties.getProperty("driver");
        url = properties.getProperty("jdbc.url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
        if(driver!=null && url!=null && username!=null && password!=null){
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //连接数据库
    public static Connection getConnection(){
        Connection connection = null;
        //加载驱动，获取连接
        try {
            connection = DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }


    //释放资源
    public static boolean closeResource(Connection conn, PreparedStatement ps, ResultSet rs){
        boolean flag = true;
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag = false;
            }
        }
        if(ps!=null){
            try {
                ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag = false;
            }
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }
}
