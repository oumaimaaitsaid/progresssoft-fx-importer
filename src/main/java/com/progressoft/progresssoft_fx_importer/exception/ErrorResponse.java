package com.progressoft.progresssoft_fx_importer.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        int statusCode,
        LocalDateTime timestamp,
        String message,
        String description,
        Object errors

) {
}
