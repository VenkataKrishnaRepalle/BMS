package com.example.bms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Result {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID uuid;

    @OneToOne
    @JoinColumn(name = "bill_uuid")
    private Bill bill;

    @Column(name = "bags", nullable = false)
    private Integer bags;

    @Column(name = "kgs", nullable = false)
    private Integer kgs;

    @Column(name = "bags_amount", nullable = false)
    private BigDecimal bagsAmount;

    @Column(name = "kgs_amount", nullable = false)
    private BigDecimal kgsAmount;

    @Column(name = "final_amount", nullable = false)
    private BigDecimal finalAmount;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false, nullable = false)
    private Date createdTime;

    @UpdateTimestamp
    @Column(name = "updated_time", nullable = false)
    private Date updatedTime;
}