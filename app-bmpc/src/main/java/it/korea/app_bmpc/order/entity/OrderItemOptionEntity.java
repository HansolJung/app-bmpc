package it.korea.app_bmpc.order.entity;

import it.korea.app_bmpc.menu.entity.MenuOptionEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bmpc_order_item_option")
public class OrderItemOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemOptId;
    private String menuOptName;
    private int menuOptPrice;
    private int quantity;
    private int totalPrice;

    // 주문 아이템 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private OrderItemEntity orderItem;

    // 메뉴 옵션 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_opt_id", nullable = false)
    private MenuOptionEntity menuOption;
}
