package com.example.developerteam.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_line")
@Getter
@Setter
@NoArgsConstructor
public class InvoiceLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceLineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worklog_id", nullable = false, unique = true)
    private WorkLog workLog;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal hours;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal lineAmount;
}
