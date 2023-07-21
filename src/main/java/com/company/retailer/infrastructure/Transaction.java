package com.company.retailer.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Transaction {

    @Id
    private UUID id;
    private BigDecimal amount;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private long rewardPoints;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
