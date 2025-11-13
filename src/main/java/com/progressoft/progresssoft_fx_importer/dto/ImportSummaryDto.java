package com.progressoft.progresssoft_fx_importer.dto;

public record ImportSummaryDto(
        int totalProcessed,
        int successCount,
        int failureCount
){}
