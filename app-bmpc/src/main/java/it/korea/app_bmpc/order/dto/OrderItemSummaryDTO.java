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
public class OrderItemSummaryDTO {

    private String menuName;
    private int quantity;
    private int totalPrice;
    private List<String> optionNames; // 옵션 이름만 표시

    public static OrderItemSummaryDTO of(OrderItemEntity entity) {
        List<String> optionNames = entity.getItemOptionList().stream()
            .map(opt -> opt.getMenuOption().getMenuOptName())
            .toList();

        return OrderItemSummaryDTO.builder()
            .menuName(entity.getMenu().getMenuName())
            .quantity(entity.getQuantity())
            .totalPrice(entity.getTotalPrice())
            .optionNames(optionNames)
            .build();
    }
}
