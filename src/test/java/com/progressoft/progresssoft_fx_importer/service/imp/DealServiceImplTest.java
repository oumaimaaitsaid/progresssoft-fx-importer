package com.progressoft.progresssoft_fx_importer.service.imp;


import com.progressoft.progresssoft_fx_importer.dto.DealRequestDto;
import com.progressoft.progresssoft_fx_importer.dto.ImportSummaryDto;
import com.progressoft.progresssoft_fx_importer.exception.InvalidDealException;
import com.progressoft.progresssoft_fx_importer.mapper.DealMapper;
import com.progressoft.progresssoft_fx_importer.model.Deal;
import com.progressoft.progresssoft_fx_importer.repository.DealRepository;
import com.progressoft.progresssoft_fx_importer.services.impl.DealServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DealServiceImplTest {



    @Mock
    private DealRepository dealRepository;

    @Mock
    private DealMapper dealMapper;


    @InjectMocks
    private DealServiceImpl dealService;

    private DealRequestDto validDto;
    private Deal validDeal;

    @BeforeEach
    void setUp() {

        validDto = new DealRequestDto(
                "deal-001",
                Currency.getInstance("EUR"),
                Currency.getInstance("USD"),
                LocalDateTime.now(),
                BigDecimal.TEN
        );

        validDeal = new Deal();
        validDeal.setDealUniqueId("deal-001");

    }


    @Test
    void testImportDeals_Success_WhenDealIsNew() {
        when(dealRepository.existsById("deal-001")).thenReturn(false);
        when(dealMapper.toEntity(validDto)).thenReturn(validDeal);

        ImportSummaryDto summary = dealService.importDeals(List.of(validDto));


        assertEquals(1, summary.totalProcessed());
        assertEquals(1, summary.successCount());
        assertEquals(0, summary.failureCount());

        verify(dealRepository, times(1)).save(validDeal);
    }


    @Test
    void testImportDeals_Failure_WhenDealIsDuplicate() {

        when(dealRepository.existsById("deal-001")).thenReturn(true);

        ImportSummaryDto summary = dealService.importDeals(List.of(validDto));

        assertEquals(1, summary.totalProcessed());
        assertEquals(0, summary.successCount()); // Khaso y-kon 0
        assertEquals(1, summary.failureCount()); // Khaso y-kon 1


        verify(dealRepository, never()).save(any(Deal.class));
    }


    @Test
    void testImportDeals_Failure_WhenCurrenciesAreSame() {

        DealRequestDto invalidDto = new DealRequestDto(
                "deal-002",
                Currency.getInstance("USD"),
                Currency.getInstance("USD"),
                LocalDateTime.now(),
                BigDecimal.TEN
        );

        ImportSummaryDto summary = dealService.importDeals(List.of(invalidDto));

        assertEquals(1, summary.totalProcessed());
        assertEquals(0, summary.successCount());
        assertEquals(1, summary.failureCount());

        verify(dealRepository, never()).existsById(anyString());
        verify(dealRepository, never()).save(any(Deal.class));
    }
}