package it.korea.app_bmpc.review.dto;

import java.time.LocalDateTime;

import it.korea.app_bmpc.review.entity.ReviewFileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReviewFileDTO {

    private int rfId;
    private String fileName;
    private String storedName;
    private String filePath;
    private String fileThumbName;
    private Long fileSize;
    private LocalDateTime createDate;
    private int displayOrder;

    public static ReviewFileDTO of(ReviewFileEntity entity) {
        return ReviewFileDTO.builder()
            .rfId(entity.getRfId())
            .fileName(entity.getFileName())
            .storedName(entity.getStoredName())
            .filePath(entity.getFilePath())
            .fileThumbName(entity.getFileThumbName())
            .fileSize(entity.getFileSize())
            .createDate(entity.getCreateDate())
            .displayOrder(entity.getDisplayOrder())
            .build();
    }
}
