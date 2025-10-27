package it.korea.app_bmpc.common.dto;

import it.korea.app_bmpc.common.utils.TimeFormatUtils;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final String date;
    private final String resultCode;
    private final T response;

    private ApiResponse(String resultCode, T response) {
        this.resultCode = resultCode;
        this.response = response;
        this.date = TimeFormatUtils.getNowTime();
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>("200", data);
    }
}
