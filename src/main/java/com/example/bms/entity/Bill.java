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
public class Bill {

    private UUID uuid;

    private Integer bags;

    private Integer kgs;

    private BigDecimal price;

    private Instant createdTime;

    private Instant updatedTime;
}