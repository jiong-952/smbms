package com.zjh.dao.bill;

import com.mysql.jdbc.StringUtils;
import com.zjh.dao.BaseDao;
import com.zjh.pojo.Bill;
import com.zjh.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BillDaoImpl implements BillDao{
    @Override
    public List<Bill> getBillList(Connection connection, Bill bill) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Bill> billList = new ArrayList<Bill>();
        if(connection != null){
            StringBuffer sql = new StringBuffer();
            sql.append("select b.*,p.proName as providerName from smbms_bill b, smbms_provider p where b.providerId = p.id");
            List<Object> list = new ArrayList<Object>();
            if(!StringUtils.isNullOrEmpty(bill.getProductName())){
                sql.append(" and productName like ?");
                list.add("%"+bill.getProductName()+"%");
            }
            if(bill.getProviderId() > 0){
                sql.append(" and providerId = ?");
                list.add(bill.getProviderId());
            }
            if(bill.getIsPayment() > 0){
                sql.append(" and isPayment = ?");
                list.add(bill.getIsPayment());
            }
            Object[] params = list.toArray();
            System.out.println("sql --------- > " + sql.toString());
            rs = BaseDao.execute(connection, sql.toString(), rs, ps, params);
            while(rs.next()){
                Bill _bill = new Bill();
                _bill.setId(rs.getInt("id"));
                _bill.setBillCode(rs.getString("billCode"));
                _bill.setProductName(rs.getString("productName"));
                _bill.setProductDesc(rs.getString("productDesc"));
                _bill.setProductUnit(rs.getString("productUnit"));
                _bill.setProductCount(rs.getBigDecimal("productCount"));
                _bill.setTotalPrice(rs.getBigDecimal("totalPrice"));
                _bill.setIsPayment(rs.getInt("isPayment"));
                _bill.setProviderId(rs.getInt("providerId"));
                _bill.setProviderName(rs.getString("providerName"));
                _bill.setCreationDate(rs.getTimestamp("creationDate"));
                _bill.setCreatedBy(rs.getInt("createdBy"));
                billList.add(_bill);
            }
            JdbcUtils.closeResource(null, ps, rs);
        }
        return billList;
    }

    @Override
    public int add(Connection connection, Bill bill) throws Exception {
        PreparedStatement ps = null;
        int flag = 0;
        if(null != connection){
            String sql = "insert into smbms_bill (billCode,productName,productDesc," +
                    "productUnit,productCount,totalPrice,isPayment,providerId,createdBy,creationDate) " +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {bill.getBillCode(),bill.getProductName(),bill.getProductDesc(),
                    bill.getProductUnit(),bill.getProductCount(),bill.getTotalPrice(),bill.getIsPayment(),
                    bill.getProviderId(),bill.getCreatedBy(),bill.getCreationDate()};
            flag = BaseDao.execute(connection,sql,ps,params);
            JdbcUtils.closeResource(null, ps, null);
            System.out.println("dao--------flag " + flag);
        }
        return flag;
    }

    @Override
    public int delete(Connection connection, String delId) throws Exception {
        PreparedStatement ps = null;
        int flag = 0;
        if(null != connection){
            String sql = "delete from smbms_bill where id=?";
            Object[] params = {delId};
            flag = BaseDao.execute(connection,sql,ps,params);
            JdbcUtils.closeResource(null, ps, null);
        }
        return flag;
    }

    @Override
    public int modify(Connection connection, Bill bill) throws Exception {
        int flag = 0;
        PreparedStatement ps = null;
        if(null != connection){
            String sql = "update smbms_bill set productName=?," +
                    "productDesc=?,productUnit=?,productCount=?,totalPrice=?," +
                    "isPayment=?,providerId=?,modifyBy=?,modifyDate=? where id = ? ";
            Object[] params = {bill.getProductName(),bill.getProductDesc(),
                    bill.getProductUnit(),bill.getProductCount(),bill.getTotalPrice(),bill.getIsPayment(),
                    bill.getProviderId(),bill.getModifyBy(),bill.getModifyDate(),bill.getId()};
            flag = BaseDao.execute(connection,sql,ps,params);
            JdbcUtils.closeResource(null, ps, null);
        }
        return flag;
    }

    @Override
    public Bill getBillById(Connection connection, String id) throws Exception {
        Bill bill = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        if(null != connection){
            String sql = "select b.*,p.proName as providerName from smbms_bill b, smbms_provider p " +
                    "where b.providerId = p.id and b.id=?";
            Object[] params = {id};
            rs = BaseDao.execute(connection, sql, rs, ps, params);
            if(rs.next()){
                bill = new Bill();
                bill.setId(rs.getInt("id"));
                bill.setBillCode(rs.getString("billCode"));
                bill.setProductName(rs.getString("productName"));
                bill.setProductDesc(rs.getString("productDesc"));
                bill.setProductUnit(rs.getString("productUnit"));
                bill.setProductCount(rs.getBigDecimal("productCount"));
                bill.setTotalPrice(rs.getBigDecimal("totalPrice"));
                bill.setIsPayment(rs.getInt("isPayment"));
                bill.setProviderId(rs.getInt("providerId"));
                bill.setProviderName(rs.getString("providerName"));
                bill.setModifyBy(rs.getInt("modifyBy"));
                bill.setModifyDate(rs.getTimestamp("modifyDate"));
            }
            JdbcUtils.closeResource(null, ps, rs);
        }
        return bill;
    }

    @Override
    public int getBillCountByProviderId(Connection connection, String providerId) throws Exception {
        int count = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        if(null != connection){
            String sql = "select count(1) as billCount from smbms_bill where" +
                    "	providerId = ?";
            Object[] params = {providerId};
            rs = BaseDao.execute(connection, sql, rs, ps, params);
            if(rs.next()){
                count = rs.getInt("billCount");
            }
            JdbcUtils.closeResource(null, ps, rs);
        }

        return count;
    }
}
