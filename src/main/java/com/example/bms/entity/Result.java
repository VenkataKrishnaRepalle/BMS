package com.example.bms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {

    private UUID uuid;

    private UUID billUuid;

    private Integer bags;

    private Integer kgs;

    private BigDecimal bagsAmount;

    private BigDecimal kgsAmount;

    private BigDecimal finalAmount;

    private BigDecimal total;

    private Instant createdTime;

    private Instant updatedTime;
}