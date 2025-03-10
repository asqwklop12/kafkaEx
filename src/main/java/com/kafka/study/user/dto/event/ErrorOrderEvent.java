package com.kafka.study.user.dto.event;

import com.kafka.study.user.dto.OrderStatus;

public record ErrorOrderEvent(String orderId, OrderStatus status, String errorMessage) {
}
