package it.korea.app_bmpc.menu.dto;

import java.util.List;

import it.korea.app_bmpc.menu.entity.MenuOptionGroupEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MenuOptionGroupDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Response {
        
        private int menuOptGrpId;
        private String menuOptGrpName;
        private String requiredYn;
        private String delYn;
        private int minSelect;
        private int maxSelect;
        private int displayOrder;
        private List<MenuOptionDTO.Response> menuOptionList;

        public static Response of(MenuOptionGroupEntity entity) {

            List<MenuOptionDTO.Response> menuOptionList = 
                entity.getMenuOptionList().stream().map(MenuOptionDTO.Response::of).toList();

            return Response.builder()
                .menuOptGrpId(entity.getMenuOptGrpId())
                .menuOptGrpName(entity.getMenuOptGrpName())
                .requiredYn(entity.getRequiredYn())
                .delYn(entity.getDelYn())
                .minSelect(entity.getMinSelect())
                .maxSelect(entity.getMaxSelect())
                .displayOrder(entity.getDisplayOrder())
                .menuOptionList(menuOptionList)
                .build();
        }
    }

    @Data
	public static class Request {

		private int menuOptGrpId;
        private int menuId;
        private String menuOptGrpName;
        private String requiredYn;       
        private int minSelect;
        private int maxSelect;
        private int displayOrder;
	}
}
