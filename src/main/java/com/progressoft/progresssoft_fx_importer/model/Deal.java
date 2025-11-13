package com.progressoft.progresssoft_fx_importer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="fx_deals")
public class Deal
{
    @Id
    @Column(unique= true,nullable = false)
    private String dealUniqueId;

    @Column(nullable = false, length = 3)
    private String fromCurrencyIso;

    @Column(nullable = false, length = 3)
    private String toCurrencyIso;

    @Column(nullable = false)
    private LocalDateTime dealTimestamp;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal dealAmount;
}