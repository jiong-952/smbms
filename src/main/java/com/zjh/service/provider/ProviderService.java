package com.zjh.service.provider;

import com.zjh.pojo.Provider;

import java.sql.Connection;
import java.util.List;

public interface ProviderService {
    //获取供应商列表
    List<Provider> getProviderList(String proName, String proCode);
    //增
    boolean add(Provider provider);
    //删
    int delete(String delId);
    //改
    boolean modify(Provider provider);
    //用供应商Id查供应商信息
    Provider getProviderById(String id);
}
