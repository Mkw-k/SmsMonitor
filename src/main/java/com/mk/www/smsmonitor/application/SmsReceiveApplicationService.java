package com.mk.www.smsmonitor.application;

import com.mk.www.smsmonitor.domain.*;
import com.mk.www.smsmonitor.entity.OrderJpaEntity;
import com.mk.www.smsmonitor.infrastructure.OrderMapper;
import com.mk.www.smsmonitor.repository.OrderRepository;
import com.mk.www.smsmonitor.infrastructure.SmsParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsReceiveApplicationService {

    private final SmsParser smsParser;
    private final OrderRepository orderRepository;

    public OrderJpaEntity handleSms(String smsContent, String phoneNumber) {
//        Optional<Payment> maybePayment = smsParser.parse(smsContent);
//        if (!maybePayment.isPresent()) return null;

        OrderJpaEntity order = new OrderJpaEntity();
        order.setCustomerName(smsContent);
        order.setStatus(OrderStatus.PAID);
//        order.setTotalAmount(maybePayment.get().getAmount());
//        order.setTotalAmount(10000);
        order.setSender(phoneNumber);

        Order domain = OrderMapper.toDomain(order);
        OrderJpaEntity save = orderRepository.save(domain);

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