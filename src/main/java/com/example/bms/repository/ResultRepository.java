package com.example.bms.repository;

import com.example.bms.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResultRepository extends JpaRepository<Result, UUID> {
    Boolean existsByBill_Uuid(UUID uuid);

    Result findByBill_Uuid(UUID uuid);
}
