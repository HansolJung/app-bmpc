package it.korea.app_bmpc.common.dto;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {
    INVALID_PARAMETER(400, "E400", "요청 파라미터가 유효하지 않습니다."),
    METHOD_NOT_ALLOWED(405, "E405", "지원하지 않는 HTTP Method 입니다."),
    DATABASE_ERROR(409, "E409", "DB 제약조건을 위반했습니다."),
    INTERNAL_SERVER_ERROR(500, "E500", "서버에서 에러가 발생했습니다."),
    UN_AUTHORIZED_ERROR(401, "E401", "인증에 실패했습니다. 로그인 정보를 확인해주세요."),
    FORBIDDEN_ERROR(404, "E404", "권한이 없습니다."),
    INVALID_TOKEN(401, "T401", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(401, "TE401", "만료된 토큰입니다."),
    NOT_FOUND_ERROR(404, "E404", "찾을 수 없습니다.");

    private final int status;
    private final String errorCode;
    private final String message;

    private ErrorCodeEnum(int status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }
}
