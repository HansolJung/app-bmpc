package it.korea.app_bmpc.order.dto;

import it.korea.app_bmpc.order.entity.OrderItemOptionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderItemOptionDTO {

    private int orderItemOptId;
    private String menuOptName;
    private int menuOptPrice;
    private int quantity;
    private int totalPrice;

    public static OrderItemOptionDTO of(OrderItemOptionEntity entity) {

        return OrderItemOptionDTO.builder()
            .orderItemOptId(entity.getOrderItemOptId())
            .menuOptName(entity.getMenuOptName())
            .menuOptPrice(entity.getMenuOptPrice())
            .quantity(entity.getQuantity())
            .totalPrice(entity.getTotalPrice())
            .build();
    }
}   
