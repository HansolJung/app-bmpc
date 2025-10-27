package it.korea.app_bmpc.basket.dto;

import it.korea.app_bmpc.basket.entity.BasketItemOptionEntity;
import it.korea.app_bmpc.menu.dto.MenuOptionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BasketItemOptionDTO {
    private int basketItemOptId;
    private int quantity;
    private int totalPrice;
    private MenuOptionDTO.Response menuOption; // 옵션 정보

    public static BasketItemOptionDTO of(BasketItemOptionEntity entity) {
        MenuOptionDTO.Response menuOption = MenuOptionDTO.Response.of(entity.getMenuOption());

        return BasketItemOptionDTO.builder()
                .basketItemOptId(entity.getBasketItemOptId())
                .quantity(entity.getQuantity())
                .totalPrice(entity.getTotalPrice())
                .menuOption(menuOption)
                .build();
    }
}

