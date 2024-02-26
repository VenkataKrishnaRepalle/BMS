package com.example.bms.dto;

import com.example.bms.entity.BillExtraStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddBillExtraResponseDto {

    private UUID uuid;

    private String name;

    private BigDecimal amount;

    private Date givenDate;

    private BillExtraStatus status;
}
