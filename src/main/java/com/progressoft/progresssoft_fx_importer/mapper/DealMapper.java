package com.progressoft.progresssoft_fx_importer.mapper;

import com.progressoft.progresssoft_fx_importer.dto.DealRequestDto;
import com.progressoft.progresssoft_fx_importer.dto.DealResponseDto;
import com.progressoft.progresssoft_fx_importer.model.Deal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.Currency;
@Mapper(componentModel = "spring")
public interface DealMapper {
    @Mapping(source = "fromCurrency", target = "fromCurrencyIso")
    @Mapping(source = "toCurrency", target = "toCurrencyIso")
    Deal toEntity(DealRequestDto dto);

    @Mapping(source = "fromCurrencyIso", target = "fromCurrency")
    @Mapping(source = "toCurrencyIso", target = "toCurrency")
    DealResponseDto toResponseDto(Deal deal);
    default String fromCurrency(Currency currency) {

        return currency == null ? null : currency.getCurrencyCode();
    }
    default Currency toCurrency(String currencyCode) {
        return currencyCode == null ? null : Currency.getInstance(currencyCode);
    }
}
