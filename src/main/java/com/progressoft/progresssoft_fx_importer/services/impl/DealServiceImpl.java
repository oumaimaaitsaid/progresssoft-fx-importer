package com.progressoft.progresssoft_fx_importer.services.impl;

import com.progressoft.progresssoft_fx_importer.dto.DealRequestDto;
import com.progressoft.progresssoft_fx_importer.dto.ImportSummaryDto;
import com.progressoft.progresssoft_fx_importer.exception.InvalidDealException;
import com.progressoft.progresssoft_fx_importer.mapper.DealMapper;
import com.progressoft.progresssoft_fx_importer.model.Deal;
import com.progressoft.progresssoft_fx_importer.repository.DealRepository;
import com.progressoft.progresssoft_fx_importer.services.IDealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealServiceImpl implements IDealService {

    private final DealRepository dealRepository;
    private final DealMapper dealMapper;

    @Override
    public ImportSummaryDto importDeals(List<DealRequestDto> dtoList) {
        log.info("Starting batch import of {} deals.", dtoList.size());

        int successCount = 0;
        int failureCount = 0;

        for (DealRequestDto dto : dtoList) {
            try {

                validateDeal(dto);

                Deal dealToSave = dealMapper.toEntity(dto);

                dealRepository.save(dealToSave);

                successCount++;

            } catch (DataIntegrityViolationException e) {
                log.warn("Duplicate deal ID detected: {}. Skipping.", dto.dealUniqueId());
                failureCount++;
            } catch (InvalidDealException e) {
                log.warn("Invalid deal data for ID {}: {}. Skipping.", dto.dealUniqueId(), e.getMessage());
                failureCount++;
            } catch (Exception e) {
                log.error("Unexpected error for deal ID {}: {}. Skipping.", dto.dealUniqueId(), e.getMessage(), e);
                failureCount++;
            }
        }

        log.info("Batch import finished. Success: {}, Failures: {}.", successCount, failureCount);

        return new ImportSummaryDto(dtoList.size(), successCount, failureCount);
    }

    private void validateDeal(DealRequestDto dto) {
        if (dto.fromCurrency().equals(dto.toCurrency())) {
            throw new InvalidDealException("From and To currencies cannot be the same.");
        }
    }
}