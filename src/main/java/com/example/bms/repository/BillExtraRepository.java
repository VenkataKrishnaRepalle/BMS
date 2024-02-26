package com.example.bms.repository;

import com.example.bms.entity.BillExtra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BillExtraRepository extends JpaRepository<BillExtra, UUID> {
    Boolean existsByBill_Uuid(UUID uuid);

    List<BillExtra> getAllByBill_Uuid(UUID uuid);
}
