package com.mk.www.smsmonitor.application.service;

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
import java.util.List;

@Service
public class GoogleSheetService {
    private final String SPREADSHEET_ID = "1g0P2zNo6MwpYDxXs8RMeCAztAKbyWMkYxuRIdIDX1OI"; // https://docs.google.com/spreadsheets/d/THIS_ID/edit
    private final String RANGE = "sheet1!A2:I"; // 시작 행 포함 범위

    public Sheets getSheetsService() throws IOException, GeneralSecurityException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                        new FileInputStream("src/main/resources/credentials/smsreceiver.json"))
                .createScoped(List.of("https://www.googleapis.com/auth/spreadsheets"));

        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("SpringSheetApp").build();
    }

    public boolean updateStatusIfMatch(String 번호, String 가격) throws IOException, GeneralSecurityException {
        Sheets sheetsService = getSheetsService();
        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, RANGE)
                .execute();

        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) return false;

        for (int i = 0; i < values.size(); i++) {
            List<Object> row = values.get(i);
            if (row.size() < 2) continue; // 번호, 가격 없는 경우 skip

            String customerPhoneNumber = row.get(3).toString().trim();
            String row가격 = row.get(7).toString().trim();

            if (번호.equals(customerPhoneNumber) && 가격.equals(row가격)) {
                int rowNum = i + 2; // A2 기준이므로
                String targetCell = "C" + rowNum;

                ValueRange body = new ValueRange().setValues(List.of(List.of("입금완료")));
                sheetsService.spreadsheets().values()
                        .update(SPREADSHEET_ID, "sheet1!" + targetCell, body)
                        .setValueInputOption("RAW")
                        .execute();
                return true;
            }
        }
        return false;
    }
}