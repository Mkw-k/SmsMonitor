package com.mk.www.smsmonitor.application.port.out;

import com.mk.www.smsmonitor.domain.model.Transaction;

public interface DataExporter {
    void export(Transaction transaction);
}
