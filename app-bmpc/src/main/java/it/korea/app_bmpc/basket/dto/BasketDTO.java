package it.korea.app_bmpc.basket.dto;

import java.util.List;

import it.korea.app_bmpc.basket.entity.BasketEntity;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BasketDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Detail {
        private int basketId;
        private int totalPrice;
        private List<BasketItemDTO> itemList;

        public static Detail of(BasketEntity entity) {

            List<BasketItemDTO> itemList = entity.getItemList().stream()
                .map(BasketItemDTO::of)
                .toList();

            return Detail.builder()
                .basketId(entity.getBasketId())
                .totalPrice(entity.getTotalPrice())
                .itemList(itemList)
                .build();
        }
    }

    @Data
	public static class Request {

        private String userId;

        @Valid
        private InnerRequest menu;
	}

    @Data
    public static class InnerRequest {
        private int menuId;
        private int quantity;

        @Valid
        private List<InnerOptionRequest> optionList;
    }

    @Data
    public static class InnerOptionRequest {
        private int menuOptId;
        private int quantity;
    }

    @Data
    public static class OrderRequest {
        private String userId;
        private String addr;
        private String addrDetail;
    }
}
