package com.progressoft.progresssoft_fx_importer.helper;

import com.progressoft.progresssoft_fx_importer.dto.DealRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvHelperTest {

    private CsvHelper csvHelper;

    @BeforeEach
    void setUp() {
        csvHelper = new CsvHelper();
    }

    @Test
    void testCsvToDeals_Success() {

        String csvContent = "id,from,to,timestamp,amount\n" +
                "deal-001,EUR,USD,2025-11-13T10:00:00,1500.50\n" +
                "deal-002,JPY,USD,2025-11-13T10:01:00,50000";

        InputStream fakeInputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        List<DealRequestDto> deals = csvHelper.csvToDeals(fakeInputStream);

        assertEquals(2, deals.size()); // Khas y-lqa 2
        assertEquals("deal-001", deals.get(0).dealUniqueId());
        assertEquals("deal-002", deals.get(1).dealUniqueId());
    }


    @Test
    void testCsvToDeals_SkipInvalidRows() {

        String csvContent = "id,from,to,timestamp,amount\n" +
                "deal-001,EUR,USD,2025-11-13T10:00:00,1500.50\n" +
                "deal-003,ABC,USD,2025-11-13T10:03:00,100\n" +
                "deal-004,GBP,EUR,2025-11-13T10:04:00,50.25";

        InputStream fakeInputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));


        List<DealRequestDto> deals = csvHelper.csvToDeals(fakeInputStream);
        assertEquals(2, deals.size());
        assertEquals("deal-001", deals.get(0).dealUniqueId());
        assertEquals("deal-004", deals.get(1).dealUniqueId());
    }
}