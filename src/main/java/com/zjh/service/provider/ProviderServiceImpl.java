package com.zjh.service.provider;

import com.zjh.dao.bill.BillDao;
import com.zjh.dao.bill.BillDaoImpl;
import com.zjh.dao.provider.ProviderDao;
import com.zjh.dao.provider.ProviderDaoImpl;
import com.zjh.pojo.Provider;
import com.zjh.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProviderServiceImpl implements ProviderService{
    private ProviderDao providerDao;
    public ProviderServiceImpl() {
        providerDao = new ProviderDaoImpl();
    }

    @Override
    public List<Provider> getProviderList(String proName, String proCode)  {
        Connection connection = null;
        List<Provider> providerList = null;
        try {
            connection = JdbcUtils.getConnection();
            providerList = providerDao.getProviderList(connection,proName,proCode);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JdbcUtils.closeResource(connection, null, null);
        }
        return providerList;
    }

    @Override
    public boolean add(Provider provider) {
        Connection connection = null;
        connection = JdbcUtils.getConnection();
        boolean flag = false;
        try {
            connection.setAutoCommit(false);//关闭自动提交
            int updateRows = providerDao.add(connection, provider);
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

    //如果改供應商有訂單沒處理完，則不能刪除
    @Override
    public int delete(String delId) {
        Connection connection = null;
        connection = JdbcUtils.getConnection();
        int billCount = -1;
        int updateRows = 0;
        try {
            connection.setAutoCommit(false);//关闭自动提交
            BillDao billDao = new BillDaoImpl();
            billCount = billDao.getBillCountByProviderId(connection,delId);
            if(billCount == 0){
                updateRows = providerDao.delete(connection, delId);
            }
            if(updateRows>0){
                System.out.println("delete Succeed!");
            }else {
                System.out.println("delete failed!");
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("rollback========");
            billCount = -1;
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            JdbcUtils.closeResource(connection,null,null);
        }
        return billCount;
    }

    @Override
    public boolean modify(Provider provider)  {
        Connection connection = null;
        connection = JdbcUtils.getConnection();
        boolean flag = false;
        try {
            connection.setAutoCommit(false);//关闭自动提交
            int updateRows = providerDao.modify(connection,provider);
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
    public Provider getProviderById(String id) {
        Connection connection = null;
        Provider provider = null;
        connection = JdbcUtils.getConnection();
        try {
            provider = providerDao.getProviderById(connection,id);
        } catch (Exception e) {
            e.printStackTrace();
            provider= null;
        } finally {
            JdbcUtils.closeResource(connection,null,null);
        }
        return provider;
    }
}
