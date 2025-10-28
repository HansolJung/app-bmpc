package it.korea.app_bmpc.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OrderStatusDTO {
    
    @NotNull(message = "주문 아이디는 필수 항목입니다.")
    private Integer orderId;
    @NotBlank(message = "주문 상태는 필수 항목입니다.")
    @Pattern(regexp = "^(주문취소|배달완료)$", message = "주문 상태는 '주문취소' 또는 '배달완료'만 가능합니다.")
    private String newStatus;
}
