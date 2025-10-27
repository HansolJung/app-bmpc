package it.korea.app_bmpc.review.entity;

import it.korea.app_bmpc.common.entity.BaseCreateTimeEntity;
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
@Table(name = "bmpc_review_files")
public class ReviewFileEntity extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rfId;
    private String fileName;
    private String storedName;
    private String filePath;
    private Long fileSize;
    private String fileThumbName;
    private int displayOrder;

    // 리뷰 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private ReviewEntity review;
}
