package com.progressoft.progresssoft_fx_importer.service.imp;

// Zid had l-imports kamlin
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

/**
 * Hada howa l-Test dyal l-Service dyalna.
 * Kay-sta3ml Mockito bach y-3-zel (isoler) l-Service.
 */
@ExtendWith(MockitoExtension.class) // Kay-goli l-JUnit: "Kh-dem Mockito"
class DealServiceImplTest {

    // 1. L-Momatilin (Les Mocks)
    // Hado homa l-dépendances li ghadi n-k-dbo 3lihom

    @Mock // Kay-créé "mock" (momatil khawi) l-l-Repository
    private DealRepository dealRepository;

    @Mock // Kay-créé "mock" l-l-Mapper
    private DealMapper dealMapper;

    // 2. L-Classe li kan-testiw
    @InjectMocks // Kay-créé instance 7aqiqia dyal l-Service, o kay-3tih l-Mocks l-foqaniyin
    private DealServiceImpl dealService;

    private DealRequestDto validDto;
    private Deal validDeal;

    // Hada kay-t-lanca 9bl kol test
    @BeforeEach
    void setUp() {
        // Kan-qado data nqia l-l-tests
        validDto = new DealRequestDto(
                "deal-001",
                Currency.getInstance("EUR"),
                Currency.getInstance("USD"),
                LocalDateTime.now(),
                BigDecimal.TEN
        );

        validDeal = new Deal(); // Hada l-objet 7aqiqi
        validDeal.setDealUniqueId("deal-001");
        // Ma 7tajinach n-3-mro l-baqi 7it l-Mapper mock
    }

    /**
     * Test 1: L-Test dyal Naja7 (Happy Path)
     * Kan-testiw deal jdid o s-7i7
     */
    @Test
    void testImportDeals_Success_WhenDealIsNew() {
        // --- 1. Arrange (L-W-jdan) ---
        // Kan-golo l-l-Mocks chno y-diro

        // Ila 3ey-tna l-existsById b "deal-001", r-je3 'false' (jdid)
        when(dealRepository.existsById("deal-001")).thenReturn(false);

        // Ila 3ey-tna l-l-Mapper, r-je3 l-Entity
        when(dealMapper.toEntity(validDto)).thenReturn(validDeal);

        // --- 2. Act (L-Khdma) ---
        // Kan-3eyto l-l-Service b-l-7aqiqi
        ImportSummaryDto summary = dealService.importDeals(List.of(validDto));

        // --- 3. Assert (N-t-2-kdo) ---
        assertEquals(1, summary.totalProcessed());
        assertEquals(1, summary.successCount());
        assertEquals(0, summary.failureCount());

        // N-t-2-kdo anaho 3eyet l-save() merra wa7da
        verify(dealRepository, times(1)).save(validDeal);
    }

    /**
     * Test 2: L-Test dyal l-Duplicate (L-Mohim)
     * Kan-testiw deal déja kayn
     */
    @Test
    void testImportDeals_Failure_WhenDealIsDuplicate() {
        // --- 1. Arrange (L-W-jdan) ---

        // L-POINT L-QWI: Kan-golo l-existsById y-r-je3 'true' (qdim)
        when(dealRepository.existsById("deal-001")).thenReturn(true);

        // --- 2. Act (L-Khdma) ---
        ImportSummaryDto summary = dealService.importDeals(List.of(validDto));

        // --- 3. Assert (N-t-2-kdo) ---
        // Hna l-farq: Ma kan-t-snawch Exception!
        // Kan-t-snaw rapport nqi
        assertEquals(1, summary.totalProcessed());
        assertEquals(0, summary.successCount()); // Khaso y-kon 0
        assertEquals(1, summary.failureCount()); // Khaso y-kon 1

        // L-POINT L-QWI 2: N-t-2-kdo anaho JAMAI 3eyet l-save()
        verify(dealRepository, never()).save(any(Deal.class));
    }

    /**
     * Test 3: L-Test dyal l-Business Ghalat (Same Currency)
     */
    @Test
    void testImportDeals_Failure_WhenCurrenciesAreSame() {
        // --- 1. Arrange (L-W-jdan) ---
        // Kan-qado data ghalta
        DealRequestDto invalidDto = new DealRequestDto(
                "deal-002",
                Currency.getInstance("USD"), // USD
                Currency.getInstance("USD"), // USD (Ghalat)
                LocalDateTime.now(),
                BigDecimal.TEN
        );

        // --- 2. Act (L-Khdma) ---
        ImportSummaryDto summary = dealService.importDeals(List.of(invalidDto));

        // --- 3. Assert (N-t-2-kdo) ---
        assertEquals(1, summary.totalProcessed());
        assertEquals(0, summary.successCount());
        assertEquals(1, summary.failureCount());

        // N-t-2-kdo anaho ma w-s-lch l-l-database aslan
        verify(dealRepository, never()).existsById(anyString());
        verify(dealRepository, never()).save(any(Deal.class));
    }
}