package com.progressoft.progresssoft_fx_importer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public record DealRequestDto (

    @NotBlank(message = "Deal Unique Id cannot be blank")
    String dealUniqueId,
    @NotNull(message = "From Currency cannot be null")
    Currency fromCurrency,
    @NotNull(message = "To Currency cannot be null")
    Currency toCurrency,
    @NotNull(message = "Deal timestamp cannot be null")
    LocalDateTime dealTimestamp,
    @NotNull(message = "Deal amount cannot be null")
    @Positive(message = "Deal amount must be positive")
    BigDecimal dealAmount

){

}
