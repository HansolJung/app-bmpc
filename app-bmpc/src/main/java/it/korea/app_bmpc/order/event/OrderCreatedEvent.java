package it.korea.app_bmpc.order.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCreatedEvent {
    private final int orderId;
    private final String ownerPhone;
}