package it.korea.app_bmpc.menu.dto;

import java.time.LocalDateTime;

import it.korea.app_bmpc.menu.entity.MenuFileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MenuFileDTO {

    private int mfId;
    private String fileName;
    private String storedName;
    private String filePath;
    private String fileThumbName;
    private Long fileSize;
    private String mainYn;
    private LocalDateTime createDate;

    public static MenuFileDTO of(MenuFileEntity entity) {
        return MenuFileDTO.builder()
            .mfId(entity.getMfId())
            .fileName(entity.getFileName())
            .storedName(entity.getStoredName())
            .filePath(entity.getFilePath())
            .fileThumbName(entity.getFileThumbName())
            .fileSize(entity.getFileSize())
            .createDate(entity.getCreateDate())
            .build();
    }
}

