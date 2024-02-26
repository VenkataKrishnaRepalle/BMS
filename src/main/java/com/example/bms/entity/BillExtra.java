package com.example.bms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Builder
@Entity
public class BillExtra {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID uuid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "given_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date givenDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BillExtraStatus status;

    @ManyToOne
    @JoinColumn(name = "bill_uuid")
    @JsonBackReference
    private Bill bill;

    @CreationTimestamp
    @Column(name = "created_time", nullable = false, updatable = false)
    private Date createdTime;

    @UpdateTimestamp
    @Column(name = "updated_time", nullable = false)
    private Date updatedTime;
}
