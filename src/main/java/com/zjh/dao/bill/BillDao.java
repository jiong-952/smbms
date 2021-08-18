package com.zjh.dao.bill;

import com.zjh.pojo.Bill;

import java.sql.Connection;
import java.util.List;

public interface BillDao {

    //获取商品列表
    List<Bill> getBillList(Connection connection, Bill bill)throws Exception;
    //增
    int add(Connection connection, Bill bill)throws Exception;
    //删
    int delete(Connection connection, String delId)throws Exception;
    //改
    int modify(Connection connection, Bill bill)throws Exception;
    //通过Id查询商品信息
    Bill getBillById(Connection connection, String id)throws Exception;
    //根据供应商ID查询订单数量
    int getBillCountByProviderId(Connection connection, String providerId)throws Exception;
}
