package com.example.bms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class BillExtra {
    private UUID uuid;

    private UUID billUuid;

    private String name;

    private BigDecimal amount;

    private Date date;

    private BillExtraOperation operation;

    private Instant createdTime;

    private Instant updatedTime;
}
