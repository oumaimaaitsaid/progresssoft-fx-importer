package com.progressoft.progresssoft_fx_importer.services;
import com.progressoft.progresssoft_fx_importer.dto.DealRequestDto;
import com.progressoft.progresssoft_fx_importer.dto.ImportSummaryDto;
import java.util.List;
public interface IDealService {
    ImportSummaryDto importDeals(List<DealRequestDto> dtoList);
}
