package com.progressoft.progresssoft_fx_importer.helper;

import com.progressoft.progresssoft_fx_importer.dto.DealRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@Component
@Slf4j
public class CsvHelper {

    public List<DealRequestDto> csvToDeals(InputStream inputStream) {
        List<DealRequestDto> deals = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {

            String line;
            boolean isFirstLine = true;

            while ((line = fileReader.readLine()) != null) {

                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] fields = line.split(","); // Kan-f-rqo b l-virgule

                if (fields.length < 5) {
                    log.warn("Skipping malformed line: {}", line);
                    continue;
                }

                try {
                    DealRequestDto dto = new DealRequestDto(
                            fields[0],
                            Currency.getInstance(fields[1]),
                            Currency.getInstance(fields[2]),
                            LocalDateTime.parse(fields[3]),
                            new BigDecimal(fields[4])
                    );
                    deals.add(dto);

                } catch (IllegalArgumentException | DateTimeParseException | ArithmeticException e) {
                    log.warn("Skipping invalid data row [{}]: {}", line, e.getMessage());
                }
            }

            log.info("Successfully parsed {} deals from CSV.", deals.size());
            return deals;

        } catch (IOException e) {
            log.error("Failed to read CSV file: {}", e.getMessage());
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
    }
}