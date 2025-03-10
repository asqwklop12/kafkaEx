package com.kafka.study.user.dto.event;

import com.kafka.study.user.dto.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinishOrderEvent {
    private String orderId;
    private OrderStatus status;

    public FinishOrderEvent(CreateOrderEvent createOrderEvent) {
        this.orderId = createOrderEvent.orderId();
        status = OrderStatus.CONFIRM;
    }
}
