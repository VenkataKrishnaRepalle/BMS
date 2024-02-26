package com.example.bms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddBillRequestDto {

    private Integer bags;

    private Integer kgs;

    private BigDecimal price;

    private List<AddBillExtraRequestDto> additionalBills;
}
