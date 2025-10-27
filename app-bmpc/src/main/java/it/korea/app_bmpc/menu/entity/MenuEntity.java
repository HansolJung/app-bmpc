package it.korea.app_bmpc.menu.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLRestriction;

import it.korea.app_bmpc.common.entity.BaseEntity;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bmpc_menu")
@SQLRestriction("del_yn = 'N'")
public class MenuEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int menuId;
    private String menuName;
    private String description;
    private int price;

    @Column(columnDefinition = "CHAR(1)")
    private String soldoutYn;
    @Column(columnDefinition = "CHAR(1)")
    private String delYn;

    // 메뉴 카테고리 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_ca_id")
    private MenuCategoryEntity menuCategory;

    // 메뉴 옵션 그룹 매핑
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true) // 기본적으로 fetch = FetchType.LAZY
    @Fetch(FetchMode.SUBSELECT) // N+1 문제를 해결하기 위한 설정, 주 엔티티를 조회한 후, 연관 엔티티들은 서브 쿼리(SUBSELECT)를 사용하여 한 번에 일괄적으로 조회하여 불필요한 추가 쿼리 발생을 막아줌.
    // 데이터가 적을 경우에만 해당 옵션을 사용할 것.
    @OrderBy("displayOrder ASC")
    private List<MenuOptionGroupEntity> menuOptionGroupList = new ArrayList<>();

    // 파일(이미지) 매핑
    // 파일과 1:1 양방향 관계
    @OneToOne(mappedBy = "menu", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private MenuFileEntity file;

    // 메뉴 추가
    public void addMenuOptionGroup(MenuOptionGroupEntity entity, boolean isUpdate) {
        if (menuOptionGroupList == null) {
            this.menuOptionGroupList = new ArrayList<>();
        }

        entity.setMenu(this);
        menuOptionGroupList.add(entity);

        if (isUpdate) { // 만약 메뉴 그룹을 업데이트 했다면...
            this.preUpdate();  // store 의 updateDate 갱신
        }
    }

    // 파일(이미지) 추가
    public void addFile(MenuFileEntity entity, boolean isUpdate) {
       
        this.file = entity;
        entity.setMenu(this);

        if (isUpdate) { // 만약 파일을 업데이트 했다면...
            this.preUpdate();  // store 의 updateDate 갱신
        }
    }
}
