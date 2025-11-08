package com.mk.www.smsmonitor.application;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mk.www.smsmonitor.infrastructure.GoogleSheetConstants.*;

@Service
public class GoogleSheetService {

    public Sheets getSheetsService() throws IOException, GeneralSecurityException {
        GoogleCredentials credentials =
                GoogleCredentials
                    .fromStream(new FileInputStream(GOOGLE_SHEET_CREDENTIALS_FILE_PATH))
                    .createScoped(List.of(GOOGLE_SHEET_API_URL));

        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName(APPLICATION_NAME)
                .build();
    }

    public boolean updateStatusIfMatch(String searchCustomerPhoneNumber, String searchPrice) throws IOException, GeneralSecurityException {
        Sheets sheetsService = getSheetsService();
        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, SHEET_RANGE)
                .execute();

        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) return false;

        for (int i = 0; i < values.size(); i++) {
            List<Object> row = values.get(i);

            if (row.size() < MIN_REQUIRED_COLUMN_SIZE) {
                continue;
            }

            String customerPhoneNumber = Optional.ofNullable(row.get(CUSTOMER_PHONE_INDEX))
                    .map(Object::toString).map(String::trim).filter(s -> !s.isEmpty()).orElse(null);
            String price = Optional.ofNullable(row.get(PRICE_INDEX))
                    .map(Object::toString).map(String::trim).filter(s -> !s.isEmpty()).orElse(null);

            if (searchCustomerPhoneNumber.equals(customerPhoneNumber) && searchPrice.equals(price)) {
                int actualRowNum = i + DATA_START_ROW_OFFSET;
                String targetCell = STATE_CELL_ALPHA + actualRowNum;

                ValueRange body = new ValueRange().setValues(List.of(List.of(PAY_COMPLETE_STATE)));
                sheetsService.spreadsheets().values()
                        .update(SPREADSHEET_ID, STATE_SHEET_NAME + targetCell, body)
                        .setValueInputOption(VALUE_INPUT_OPTION)
                        .execute();
                return true;
            }
        }
        return false;
    }
}