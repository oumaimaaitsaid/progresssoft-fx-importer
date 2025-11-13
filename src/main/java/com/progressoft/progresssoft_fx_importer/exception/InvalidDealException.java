package com.progressoft.progresssoft_fx_importer.exception;

import com.progressoft.progresssoft_fx_importer.dto.DealRequestDto;

public class InvalidDealException extends  RuntimeException {

    public InvalidDealException(String message) {
        super(message);
    }
}
