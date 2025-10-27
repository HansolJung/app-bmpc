package it.korea.app_bmpc.menu.entity;

import it.korea.app_bmpc.common.entity.BaseCreateTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bmpc_menu_files")
public class MenuFileEntity extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mfId;
    private String fileName;
    private String storedName;
    private String filePath;
    private Long fileSize;
    private String fileThumbName;

    // 가게 매핑
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_id")
    private MenuEntity menu;
}
