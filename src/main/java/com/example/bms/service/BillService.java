package com.example.bms.service;

import com.example.bms.dto.AddBillExtraRequestDto;
import com.example.bms.dto.AddBillRequestDto;
import com.example.bms.dto.BillResponseDto;

import java.util.List;
import java.util.UUID;

public interface BillService {
    BillResponseDto addBill(AddBillRequestDto addBillRequestDto);

    BillResponseDto viewBill(UUID billId);

    BillResponseDto addCharges(UUID billId, List<AddBillExtraRequestDto> billExtraRequestDto);

    BillResponseDto editCharges(UUID billId, UUID billExtraId, AddBillExtraRequestDto addBillExtraRequestDto);
}
