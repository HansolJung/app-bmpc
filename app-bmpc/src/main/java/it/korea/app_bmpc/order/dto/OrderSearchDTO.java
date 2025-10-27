package it.korea.app_bmpc.order.dto;

import lombok.Data;

@Data
public class OrderSearchDTO {
    private String searchText;
    private String status;
    private String userId;
}

