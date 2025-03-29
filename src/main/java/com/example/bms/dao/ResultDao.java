package com.example.bms.dao;

import com.example.bms.entity.Result;
import org.apache.ibatis.annotations.Param;

import java.util.UUID;

public interface ResultDao {
    Result getById(@Param("uuid") UUID uuid);

    Result getByBillId(@Param("billUuid") UUID billUuid);

    int insert(@Param("result") Result result);

    int update(@Param("result") Result result);

    int delete(@Param("uuid") UUID uuid);
}
