package it.korea.app_bmpc.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDepositRequestDTO {

    @NotBlank(message = "사용자 아이디는 필수 항목입니다.")
    private String userId;
    @NotNull(message = "보유금은 필수 항목입니다.")
    private Integer deposit;
}
