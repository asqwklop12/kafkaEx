package com.kafka.study.user.listener;

import com.google.gson.Gson;
import com.kafka.study.user.dto.OrderStatus;
import com.kafka.study.user.dto.event.CreateOrderEvent;
import com.kafka.study.user.dto.event.ErrorOrderEvent;
import com.kafka.study.user.dto.event.FinishOrderEvent;
import com.kafka.study.user.entity.Account;
import com.kafka.study.user.producer.OrderProducer;
import com.kafka.study.user.respository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserListener {
    private final OrderProducer orderProducer;
    private final AccountRepository accountRepository;

    @KafkaListener(topics = "order.created", groupId = "order-group")
    @Transactional
    public void order(CreateOrderEvent createOrderEvent) {
        // 계좌는 하나만 등록하도록 설정하였다.
        Account account = accountRepository.findAll().stream().findFirst().orElse(null);
        BigInteger total = BigInteger.ZERO;
        if (account != null) {
            total = account.getAmount();
        }
        // 현재 자산 보다 상품의 가격이 낮으면 사용이 불가능하다.
        if (createOrderEvent.price().compareTo(total) >= 0) {
            orderProducer.sendOrderErrorEvent(new ErrorOrderEvent(createOrderEvent.orderId(), OrderStatus.ERROR, "에러가 발생하였습니다."));
            return;
        }

        if (account != null) {
            account.setAmount(total.subtract(createOrderEvent.price()));
        }

        // 주문이 완료 되었다고 메시지를 보낸다.
        orderProducer.sendOrderEndEvent(new FinishOrderEvent(createOrderEvent));

    }

}
