package com.zjh.service.bill;

import com.zjh.dao.BaseDao;
import com.zjh.dao.bill.BillDao;
import com.zjh.dao.bill.BillDaoImpl;
import com.zjh.pojo.Bill;
import com.zjh.pojo.User;
import com.zjh.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BillServiceImpl implements BillService{
    private BillDao billDao;
    public BillServiceImpl() {
        billDao = new BillDaoImpl();
    }

    @Override
    public List<Bill> getBillList(Bill bill) {
        Connection connection = null;
        List<Bill> billList = null;
        try {
            connection = JdbcUtils.getConnection();
            billList = billDao.getBillList(connection, bill);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JdbcUtils.closeResource(connection, null, null);
        }
        return billList;
    }

    @Override
    public boolean add(Bill bill) {
        Connection connection = null;
        connection = JdbcUtils.getConnection();
        boolean flag = false;
        try {
            connection.setAutoCommit(false);//关闭自动提交
            int updateRows = billDao.add(connection, bill);
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
    public boolean delete(String delId) {
        Connection connection = null;
        connection = JdbcUtils.getConnection();
        boolean flag = false;
        try {
            connection.setAutoCommit(false);//关闭自动提交
            int updateRows = billDao.delete(connection,delId);
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
    public boolean modify(Bill bill) {
        Connection connection = null;
        connection = JdbcUtils.getConnection();
        boolean flag = false;
        try {
            connection.setAutoCommit(false);//关闭自动提交
            int updateRows = billDao.modify(connection,bill);
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
    public Bill getBillById(String id) {
        Connection connection = null;
        Bill bill = null;
        connection = JdbcUtils.getConnection();
        try {
            bill = billDao.getBillById(connection,id);
        } catch (Exception e) {
            e.printStackTrace();
            bill = null;
        } finally {
            JdbcUtils.closeResource(connection,null,null);
        }
        return bill;
    }
}
