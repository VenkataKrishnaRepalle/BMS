package com.example.bms.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Bill {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID uuid;

    @Column(name = "bags", nullable = false)
    private Integer bags;

    @Column(name = "kgs", nullable = false)
    private Integer kgs;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("bill")
    private List<BillExtra> billExtras;

    @OneToOne(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("bill")
    private Result result;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false, nullable = false)
    private Date createdTime;

    @UpdateTimestamp
    @Column(name = "updated_time", nullable = false)
    private Date updatedTime;
}