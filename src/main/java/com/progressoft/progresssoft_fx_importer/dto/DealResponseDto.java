package com.progressoft.progresssoft_fx_importer.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public record DealResponseDto (

        String dealUniqueId,
        Currency fromCurrency,
        Currency toCurrency,
        LocalDateTime dealTimestamp,
        BigDecimal dealAmount
){
}
