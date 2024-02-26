package com.example.bms.dto;

import com.example.bms.entity.Bill;
import com.example.bms.entity.Result;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddBillResultWrapper {
    private Bill bill;

    private Result result;
}
