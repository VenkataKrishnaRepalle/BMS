package com.example.bms.dao;

import com.example.bms.entity.BillExtra;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

public interface BillExtraDao {
    BillExtra getById(@Param("uuid") UUID uuid);

    List<BillExtra> getByBillId(@Param("billUuid") UUID billUuid);

    int insert(@Param("billExtra") BillExtra billExtra);

    int update(@Param("billExtra") BillExtra billExtra);

    int delete(@Param("uuid") UUID uuid);
}
