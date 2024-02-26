package com.example.bms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillResponseDto {

    private UUID uuid;

    private Integer bags;

    private Integer kgs;

    private BigDecimal price;

    private List<AddBillExtraResponseDto> additionalBills;

    private AddBillResultResponseDto result;

    private Date createdTime;
}