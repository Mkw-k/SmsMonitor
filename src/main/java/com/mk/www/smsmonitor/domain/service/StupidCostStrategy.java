package com.mk.www.smsmonitor.domain.service;

import com.mk.www.smsmonitor.domain.model.Transaction;

public interface StupidCostStrategy {
    boolean isStupidCost(Transaction transaction);
    String getStrategyName();
}
