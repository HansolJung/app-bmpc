package it.korea.app_bmpc.store.dto;

import java.time.LocalDateTime;

import it.korea.app_bmpc.store.entity.StoreFileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StoreFileDTO {

    private int sfId;
    private String fileName;
    private String storedName;
    private String filePath;
    private String fileThumbName;
    private Long fileSize;
    private LocalDateTime createDate;
    private String mainYn;

    public static StoreFileDTO of(StoreFileEntity entity) {
        return StoreFileDTO.builder()
            .sfId(entity.getSfId())
            .fileName(entity.getFileName())
            .storedName(entity.getStoredName())
            .filePath(entity.getFilePath())
            .fileThumbName(entity.getFileThumbName())
            .fileSize(entity.getFileSize())
            .createDate(entity.getCreateDate())
            .mainYn(entity.getMainYn())
            .build();
    }
}
