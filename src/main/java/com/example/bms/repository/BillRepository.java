package com.example.bms.repository;

import com.example.bms.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BillRepository extends JpaRepository<Bill, UUID> {
}
