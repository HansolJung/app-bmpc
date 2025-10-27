package it.korea.app_bmpc.favorite.dto;

import java.math.BigDecimal;
import java.util.List;

import it.korea.app_bmpc.favorite.entity.FavoriteStoreEntity;
import it.korea.app_bmpc.store.dto.StoreFileDTO;
import it.korea.app_bmpc.store.entity.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FavoriteStoreDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Response {
        private int favoriteId;
        private String userId;
        private int storeId;
        private String storeName;
        private String fileName;
        private String storedName;
        private String filePath;
        private String fileThumbName;
        private BigDecimal ratingAvg;

        public static Response of(FavoriteStoreEntity entity) {

            StoreEntity store = entity.getStore();

            // 파일 엔티티를 파일 DTO 로 객체 변환
            // 바로 이때 파일 리스트가 SELECT 된다.
            List<StoreFileDTO> fileList = 
                store.getFileList().stream().map(StoreFileDTO::of).filter((file)-> 
                    file.getMainYn().equals("Y")).toList();
            
            StoreFileDTO mainImage = null;

            if (fileList != null && fileList.size() > 0) {
                mainImage = fileList.get(0);
            }

            return Response.builder()
                .favoriteId(entity.getFavoriteId())
                .userId(entity.getUser().getUserId())
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .fileName(mainImage != null ? mainImage.getFileName() : null)
                .storedName(mainImage != null ? mainImage.getStoredName() : null)
                .filePath(mainImage != null ? mainImage.getFilePath() : null)
                .fileThumbName(mainImage != null ? mainImage.getFileThumbName() : null)
                .ratingAvg(store.getRatingAvg())
                .build();
        }
    }


    @Data
    public static class Request {
        private String userId;
        private int storeId;
    }
}
