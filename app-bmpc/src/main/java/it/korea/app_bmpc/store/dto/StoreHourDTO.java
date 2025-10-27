package it.korea.app_bmpc.store.dto;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import it.korea.app_bmpc.store.entity.StoreHourEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StoreHourDTO {

    private int shId;
    private int dayOfWeek;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;
    private String closeYn;

    public static StoreHourDTO of(StoreHourEntity entity) {
        return StoreHourDTO.builder()
            .shId(entity.getShId())
            .dayOfWeek(entity.getDayOfWeek())
            .openTime(entity.getOpenTime())
            .closeTime(entity.getCloseTime())
            .closeYn(entity.getCloseYn())
            .build();
    }
}

