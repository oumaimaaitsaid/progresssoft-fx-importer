package com.progressoft.progresssoft_fx_importer.controller;

import com.progressoft.progresssoft_fx_importer.dto.DealRequestDto;
import com.progressoft.progresssoft_fx_importer.dto.ImportSummaryDto;
import com.progressoft.progresssoft_fx_importer.helper.CsvHelper;
import com.progressoft.progresssoft_fx_importer.services.IDealService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DealController.class)
class DealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IDealService dealService;

    @MockBean
    private CsvHelper csvHelper;

    @Test
    void testImportDeals_Success() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "deals.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "csv content...".getBytes()
        );

        when(csvHelper.csvToDeals(any())).thenReturn(List.of(new DealRequestDto(null, null, null, null, null)));

        ImportSummaryDto summary = new ImportSummaryDto(5, 3, 2);
        when(dealService.importDeals(any())).thenReturn(summary);

        mockMvc.perform(multipart("/api/deals/import").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalProcessed").value(5))
                .andExpect(jsonPath("$.successCount").value(3))
                .andExpect(jsonPath("$.failureCount").value(2));
    }
}