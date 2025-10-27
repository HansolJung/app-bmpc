package it.korea.app_bmpc.store.entity;

import java.time.LocalTime;

import jakarta.persistence.Column;
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
@Table(name = "bmpc_store_hours")
public class StoreHourEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shId;
    private int dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;

    @Column(columnDefinition = "CHAR(1)")
    private String closeYn;

    // 가게 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;
}

