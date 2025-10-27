package it.korea.app_bmpc.basket.entity;

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
@Table(name = "bmpc_basket_item_option")
public class BasketItemOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int basketItemOptId;
    private String menuOptName;
    private int menuOptPrice;
    private int quantity;
    private int totalPrice;

    // 장바구니 항목 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_item_id")
    private BasketItemEntity basketItem;

    // 메뉴 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_opt_id")
    private MenuOptionEntity menuOption;
}
