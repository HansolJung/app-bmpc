package it.korea.app_bmpc.menu.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bmpc_menu_option_group")
@SQLRestriction("del_yn = 'N'")
public class MenuOptionGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int menuOptGrpId;
    private String menuOptGrpName;

    @Column(columnDefinition = "CHAR(1)")
    private String requiredYn;
    @Column(columnDefinition = "CHAR(1)")
    private String delYn;

    private int minSelect;
    private int maxSelect;
    private int displayOrder;

    // 메뉴 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuEntity menu;

    // 메뉴 옵션 매핑
    @OneToMany(mappedBy = "menuOptionGroup", cascade = CascadeType.ALL, orphanRemoval = true) // 기본적으로 fetch = FetchType.LAZY
    @Fetch(FetchMode.SUBSELECT) // N+1 문제를 해결하기 위한 설정, 주 엔티티를 조회한 후, 연관 엔티티들은 서브 쿼리(SUBSELECT)를 사용하여 한 번에 일괄적으로 조회하여 불필요한 추가 쿼리 발생을 막아줌.
    // 데이터가 적을 경우에만 해당 옵션을 사용할 것.
    @OrderBy("displayOrder ASC")
    private List<MenuOptionEntity> menuOptionList = new ArrayList<>();

    // 메뉴 추가
    public void addMenuOption(MenuOptionEntity entity, boolean isUpdate) {
        if (menuOptionList == null) {
            this.menuOptionList = new ArrayList<>();
        }

        entity.setMenuOptionGroup(this);
        menuOptionList.add(entity);
    }
}
