package com.mk.www.smsmonitor.infrastructure.exporter;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.mk.www.smsmonitor.application.port.out.DataExporter;
import com.mk.www.smsmonitor.domain.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class GoogleSheetExporter implements DataExporter {

    @Value("${google.credentials.path}")
    private String credentialsPath;

    @Value("${google.credentials.json-content}")
    private String jsonContent;
    
    private final String SPREADSHEET_ID = "1g0P2zNo6MwpYDxXs8RMeCAztAKbyWMkYxuRIdIDX1OI";
    private final String SHEET_NAME = "sheet1";

    private Sheets getSheetsService() throws IOException, GeneralSecurityException {
        GoogleCredentials credentials;
        
        if (jsonContent != null && !jsonContent.isEmpty()) {
            // application.yml에 있는 암호화된 JSON 내용을 바로 사용 (Jasypt가 복호화해줌)
            credentials = GoogleCredentials.fromStream(
                    new java.io.ByteArrayInputStream(jsonContent.getBytes()))
                    .createScoped(List.of("https://www.googleapis.com/auth/spreadsheets"));
        } else {
            // 기존 파일 경로 방식 사용
            credentials = GoogleCredentials.fromStream(
                    new FileInputStream(credentialsPath))
                    .createScoped(List.of("https://www.googleapis.com/auth/spreadsheets"));
        }

        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("SmsMonitor").build();
    }

    @Override
    public void export(Transaction transaction) {
        try {
            Sheets sheetsService = getSheetsService();

            String categoryName = transaction.getCategory() != null ? transaction.getCategory().getName() : "";

            List<Object> rowData = List.of(
                    transaction.getTransactionTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    transaction.getVendor(),
                    transaction.getAmount(),
                    categoryName,
                    transaction.getMemo()
            );

            ValueRange body = new ValueRange().setValues(List.of(rowData));

            AppendValuesResponse result = sheetsService.spreadsheets().values()
                    .append(SPREADSHEET_ID, SHEET_NAME, body)
                    .setValueInputOption("USER_ENTERED")
                    .setInsertDataOption("INSERT_ROWS")
                    .execute();

            log.error("Appended data to Google Sheet: " + result);

        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            log.error("Error exporting transaction to Google Sheet: " + e.getMessage());
        }
    }
}
