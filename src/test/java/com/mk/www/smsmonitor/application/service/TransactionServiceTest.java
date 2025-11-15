package com.mk.www.smsmonitor.application.service;

import com.mk.www.smsmonitor.application.port.out.DataExporter;
import com.mk.www.smsmonitor.domain.model.Transaction;
import com.mk.www.smsmonitor.domain.repository.TransactionRepository;
import com.mk.www.smsmonitor.presentation.dto.MemoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    private TransactionService transactionService;

    @Mock
    private DataExporter exporter1;
    @Mock
    private DataExporter exporter2;
    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(Arrays.asList(exporter1, exporter2), transactionRepository);
    }

    @Test
    @DisplayName("거래내역_저장_요청시_리포지토리에_저장하고_모든_Exporter가_호출된다")
    void 거래내역_저장_요청시_리포지토리에_저장하고_모든_Exporter가_호출된다() {
        // given
        Transaction transaction = Transaction.builder().build();
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        // when
        transactionService.save(transaction);

        // then
        verify(transactionRepository, times(1)).save(transaction);
        verify(exporter1, times(1)).export(transaction);
        verify(exporter2, times(1)).export(transaction);
    }

    @Test
    @DisplayName("메모_수정_요청_시_거래내역을_찾아_저장한다")
    void 메모_수정_요청_시_거래내역을_찾아_저장한다() {
        // given
        MemoRequest memoRequest = new MemoRequest();
        memoRequest.setMemo("새로운 메모");
        Transaction originalTransaction = Transaction.builder().id(1L).memo("옛날 메모").build();

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(originalTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Optional<Transaction> result = transactionService.updateMemo(1L, memoRequest);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getMemo()).isEqualTo("새로운 메모");
        verify(transactionRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
}
