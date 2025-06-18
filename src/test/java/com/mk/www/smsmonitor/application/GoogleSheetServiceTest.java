package com.mk.www.smsmonitor.application;

import com.google.api.services.sheets.v4.Sheets;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GoogleSheetServiceTest {
    @Autowired
    GoogleSheetService googleSheetService;

    @Test
    void getSheetsService를_통해서_시트의_정보를_불러올수_있다() throws GeneralSecurityException, IOException {
        //given - none

        //when
        Sheets sheetsService = googleSheetService.getSheetsService();

        //then
        assertThat(sheetsService).isNotNull();
    }

    @Test
//    @Transactional
    void updateStatusIfMatch를_통해서_구글시트내용을_업데이트할수_있다() throws GeneralSecurityException, IOException {
        //given
        String 번호 = "01026074128";
        String 가격 = "10000";

        //when
        boolean b = googleSheetService.updateStatusIfMatch(번호, 가격);

        //then
        assertThat(b).isTrue();
    }

}