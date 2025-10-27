package it.korea.app_bmpc.order.dto;

import java.util.List;

import it.korea.app_bmpc.order.entity.OrderItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderItemDTO {

    private int itemId;
    private int quantity;
    private int totalPrice;
    private int menuId;
    private String menuName;
    private int menuPrice;
    private List<OrderItemOptionDTO> itemOptionList;

    public static OrderItemDTO of(OrderItemEntity entity) {

        List<OrderItemOptionDTO> itemOptionList = entity.getItemOptionList()
            .stream().map(OrderItemOptionDTO::of).toList();

        return OrderItemDTO.builder()
            .itemId(entity.getItemId())
            .quantity(entity.getQuantity())
            .totalPrice(entity.getTotalPrice())
            .menuId(entity.getMenu().getMenuId())
            .menuName(entity.getMenuName())
            .menuPrice(entity.getMenuPrice())
            .itemOptionList(itemOptionList)
            .build();
    }
}
