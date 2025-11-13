package com.progressoft.progresssoft_fx_importer.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        String message,
        String description

) {
}
