package com.example.bms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddBillResultResponseDto {

    private UUID uuid;

    private Integer bags;

    private Integer kgs;

    private BigDecimal bagsAmount;

    private BigDecimal kgsAmount;

    private BigDecimal finalAmount;
}