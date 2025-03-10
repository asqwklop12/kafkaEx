package com.kafka.study.user.dto.event;

import java.math.BigInteger;

public record CreateOrderEvent(String orderId, BigInteger price) {
}
