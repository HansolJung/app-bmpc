package it.korea.app_bmpc.menu.dto;

import it.korea.app_bmpc.menu.entity.MenuOptionEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
        @NotNull(message = "메뉴 옵션 그룹은 필수 항목입니다.")
        private Integer menuOptGrpId;
        @NotBlank(message = "메뉴 옵션명은 필수 항목입니다.")       
        private String menuOptName;
        @NotNull(message = "가격은 필수 항목입니다.")
        @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
        private Integer price;
        @NotBlank(message = "선택 가능 여부는 필수 항목입니다.")
        @Pattern(regexp = "^[YN]$", message = "선택 가능 여부는 'Y' 또는 'N'만 가능합니다.")
        private String availableYn;
        @NotNull(message = "최대 선택 개수는 필수 항목입니다.") 
        private Integer maxSelect;
        @NotNull(message = "정렬 순서는 필수 항목입니다.")
        @Min(value = 1, message = "정렬 순서는 1 이상이어야 합니다.")
        private Integer displayOrder;
	}
}
