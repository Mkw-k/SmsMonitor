package com.mk.www.smsmonitor.application;

import com.mk.www.smsmonitor.domain.*;
import com.mk.www.smsmonitor.entity.OrderEntity;
import com.mk.www.smsmonitor.infrastructure.OrderMapper;
import com.mk.www.smsmonitor.repository.OrderRepository;
import com.mk.www.smsmonitor.infrastructure.SmsParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        order.setTotalAmount(10000);

        Order domain = OrderMapper.toDomain(order);
        OrderEntity save = orderRepository.save(domain);

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