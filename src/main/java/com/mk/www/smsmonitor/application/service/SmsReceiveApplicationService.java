package com.mk.www.smsmonitor.application.service;

import com.mk.www.smsmonitor.domain.model.Order;
import com.mk.www.smsmonitor.domain.model.OrderStatus;
import com.mk.www.smsmonitor.domain.model.Payment;
import com.mk.www.smsmonitor.domain.repository.OrderRepository;
import com.mk.www.smsmonitor.domain.service.OrderMatcher;
import com.mk.www.smsmonitor.infrastructure.persistence.entity.OrderEntity;
import com.mk.www.smsmonitor.infrastructure.persistence.mapper.OrderMapper;
import com.mk.www.smsmonitor.infrastructure.sms.SmsParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SmsReceiveApplicationService {

    private final SmsParser smsParser;
    private final OrderRepository orderRepository;

    public OrderEntity handleSms(String smsContent, String phoneNumber) {
//        Optional<Payment> maybePayment = smsParser.parse(smsContent);
//        if (!maybePayment.isPresent()) return null;

        OrderEntity order = new OrderEntity();
        order.setCustomerName(smsContent);
        order.setStatus(OrderStatus.PAID);
//        order.setTotalAmount(maybePayment.get().getAmount());
//        order.setTotalAmount(10000);
        order.setSender(phoneNumber);

        Order domain = OrderMapper.toDomain(order);
        OrderEntity save = orderRepository.save(domain);

        // TODO 여기서 구글시트 업데이트 연동
        /////////////////////////////

        return save;
        /*Payment payment = maybePayment.get();
        List<Order> pendingOrders = orderRepository.findPendingOrders();

        OrderMatcher matcher = new OrderMatcher(pendingOrders);
        matcher.match(payment).ifPresent(order -> {
            order.markAsPaid();
            orderRepository.save(order);
        });*/
    }
}