package com.example.bms.service;

import com.example.bms.dto.AddBillRequestDto;
import com.example.bms.dto.BillResponseDto;
import com.example.bms.entity.BillExtra;

import java.util.List;
import java.util.UUID;

public interface BillService {
    BillResponseDto addBill(AddBillRequestDto billRequestDto);

    BillResponseDto viewBill(UUID billId);

    BillResponseDto addCharges(UUID billId, List<BillExtra> billExtraDto);

    BillResponseDto editCharges(UUID billId, UUID billExtraId, BillExtra billExtraDto);

    void deleteBillById(UUID billId);
}
