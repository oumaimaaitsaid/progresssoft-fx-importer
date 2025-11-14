package com.progressoft.progresssoft_fx_importer.controller;

import com.progressoft.progresssoft_fx_importer.dto.DealRequestDto;
import com.progressoft.progresssoft_fx_importer.dto.ImportSummaryDto;
import com.progressoft.progresssoft_fx_importer.exception.InvalidDealException;
import com.progressoft.progresssoft_fx_importer.helper.CsvHelper;
import com.progressoft.progresssoft_fx_importer.services.IDealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
@Slf4j
public class DealController {

    private final IDealService dealService;
    private final CsvHelper csvHelper;

    @PostMapping("/import")
    public ResponseEntity<ImportSummaryDto> importDeals(@RequestParam("file") MultipartFile file) {
        log.info("Received file import request: {}", file.getOriginalFilename());

        if (file.isEmpty()) {
            throw new InvalidDealException("File is empty. Please upload a valid CSV file.");
        }

        List<DealRequestDto> dtoList;
        try {
            dtoList = csvHelper.csvToDeals(file.getInputStream());
        } catch (IOException e) {
            log.error("Failed to read file stream", e);
            throw new InvalidDealException("Failed to read file: " + e.getMessage());
        }

        if (dtoList.isEmpty()) {
            throw new InvalidDealException("File contains no valid deal rows (it might only have a header).");
        }

        ImportSummaryDto summary = dealService.importDeals(dtoList);

        return ResponseEntity.ok(summary);
    }

}