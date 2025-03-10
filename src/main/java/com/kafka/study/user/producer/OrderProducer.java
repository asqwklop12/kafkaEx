package com.kafka.study.user.producer;

import com.kafka.study.user.dto.event.ErrorOrderEvent;
import com.kafka.study.user.dto.event.FinishOrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProducer {
    private final KafkaTemplate<String, ErrorOrderEvent> errorEventKafkaTemplate;
    private final KafkaTemplate<String, FinishOrderEvent> endEventKafkaTemplate;

    public void sendOrderErrorEvent(ErrorOrderEvent event) {
        errorEventKafkaTemplate.send("order.error", event.orderId(), event);
    }

    public void sendOrderEndEvent(FinishOrderEvent finishOrderEvent) {
        endEventKafkaTemplate.send("order.end", finishOrderEvent.getOrderId(), finishOrderEvent);
    }
}
