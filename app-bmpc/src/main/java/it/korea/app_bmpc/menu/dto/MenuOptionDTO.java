package it.korea.app_bmpc.menu.dto;

import it.korea.app_bmpc.menu.entity.MenuOptionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MenuOptionDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Response {
        private int menuOptId;
        private String menuOptName;
        private int price;
        private String availableYn;
        private String delYn;
        private int maxSelect;
        private int displayOrder;

        public static Response of(MenuOptionEntity entity) {

            return Response.builder()
                .menuOptId(entity.getMenuOptId())
                .menuOptName(entity.getMenuOptName())
                .price(entity.getPrice())
                .availableYn(entity.getAvailableYn())
                .delYn(entity.getDelYn())
                .maxSelect(entity.getMaxSelect())
                .displayOrder(entity.getDisplayOrder())
                .build();
        }
    }

    @Data
	public static class Request {

		private int menuOptId;
        private int menuOptGrpId;        
        private String menuOptName;
        private int price;
        private String availableYn;      
        private int maxSelect;
        private int displayOrder;
	}
}
