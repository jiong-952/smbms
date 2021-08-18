package com.zjh.service.bill;

import com.zjh.pojo.Bill;

import java.util.List;

public interface BillService {
    List<Bill> getBillList(Bill bill);
    boolean add(Bill bill);
    boolean delete(String delId);
    boolean modify(Bill bill);
    Bill getBillById(String id);
}
