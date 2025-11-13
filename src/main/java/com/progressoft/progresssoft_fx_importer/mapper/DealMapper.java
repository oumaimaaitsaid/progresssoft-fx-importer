package com.progressoft.progresssoft_fx_importer.mapper;

import com.progressoft.progresssoft_fx_importer.dto.DealRequestDto;
import com.progressoft.progresssoft_fx_importer.dto.DealResponseDto;
import com.progressoft.progresssoft_fx_importer.model.Deal;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface DealMapper {
    Deal toEntity(DealRequestDto dto);
    DealResponseDto toResponseDto(Deal deal);
}
