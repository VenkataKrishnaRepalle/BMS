package com.example.bms.dto;

import com.example.bms.entity.Bill;
import com.example.bms.entity.BillExtra;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddBillRequestDto {
    private Bill bill;

    private List<BillExtra> billExtras;
}
