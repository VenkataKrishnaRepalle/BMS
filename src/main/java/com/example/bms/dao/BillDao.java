package com.example.bms.dao;

import com.example.bms.entity.Bill;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

public interface BillDao {

    Bill getById(@Param("uuid") UUID uuid);

    List<Bill> getAll();

    int insert(@Param("bill") Bill bill);

    int update(@Param("bill") Bill bill);

    int delete(@Param("uuid") UUID uuid);
}
