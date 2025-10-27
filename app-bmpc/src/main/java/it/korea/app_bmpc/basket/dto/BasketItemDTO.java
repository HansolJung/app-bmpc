package it.korea.app_bmpc.basket.dto;


import java.util.List;

import it.korea.app_bmpc.basket.entity.BasketItemEntity;
import it.korea.app_bmpc.menu.dto.MenuDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BasketItemDTO {
    private int basketItemId;
    private int quantity;
    private int totalPrice;
    private MenuDTO.Response menu; // 메뉴 정보
    private List<BasketItemOptionDTO> options; // 옵션 정보

    public static BasketItemDTO of(BasketItemEntity entity) {
        MenuDTO.Response menu = MenuDTO.Response.of(entity.getMenu());
        List<BasketItemOptionDTO> options = entity.getItemOptionList().stream()
            .map(BasketItemOptionDTO::of)
            .toList();

        return BasketItemDTO.builder()
                .basketItemId(entity.getBasketItemId())
                .quantity(entity.getQuantity())
                .totalPrice(entity.getTotalPrice())
                .menu(menu)
                .options(options)
                .build();
    }
}
