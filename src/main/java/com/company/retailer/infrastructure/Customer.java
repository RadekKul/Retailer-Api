package com.company.retailer.infrastructure;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Customer {

    @Id
    private UUID id;

    @Column(unique = true)
    private String login;

    @Column(unique = true)
    private String email;

    private String firstName;
    private String surname;

    @OneToMany(mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Transaction> transactions;

    void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
