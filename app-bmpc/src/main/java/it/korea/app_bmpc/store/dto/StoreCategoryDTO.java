package it.korea.app_bmpc.store.dto;

import it.korea.app_bmpc.store.entity.StoreCategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StoreCategoryDTO {

    private int storeCaId;
    private CategoryDTO category;

    public static StoreCategoryDTO of(StoreCategoryEntity entity) {

        CategoryDTO category = CategoryDTO.of(entity.getCategory());

        return StoreCategoryDTO.builder()
            .storeCaId(entity.getStoreCaId())
            .category(category)
            .build();
    }
}
