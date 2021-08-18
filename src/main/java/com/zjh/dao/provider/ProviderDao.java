package com.zjh.dao.provider;

import com.zjh.pojo.Provider;

import java.sql.Connection;
import java.util.List;

public interface ProviderDao {
    //获取供应商列表
    List<Provider> getProviderList(Connection connection, String proName, String proCode)throws Exception;
    //增
    int add(Connection connection, Provider provider)throws Exception;
    //删
    int delete(Connection connection, String delId)throws Exception;
    //改
    int modify(Connection connection, Provider provider)throws Exception;
    //用供应商Id查供应商信息
    Provider getProviderById(Connection connection, String id)throws Exception;
}
