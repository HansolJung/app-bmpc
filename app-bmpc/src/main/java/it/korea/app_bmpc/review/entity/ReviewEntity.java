package it.korea.app_bmpc.review.entity;

import java.util.ArrayList;
import java.util.List;

import it.korea.app_bmpc.common.entity.BaseEntity;
import it.korea.app_bmpc.order.entity.OrderEntity;
import it.korea.app_bmpc.store.entity.StoreEntity;
import it.korea.app_bmpc.user.entity.UserEntity;
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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bmpc_review")
public class ReviewEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;
    private int rating;
    private String content;

    @Column(columnDefinition = "CHAR(1)")
    private String delYn;

    // 주문 매핑
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private OrderEntity order;

    // 가게 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    // 회원 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // 파일(이미지) 매핑
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true) // 기본적으로 fetch = FetchType.LAZY
    private List<ReviewFileEntity> fileList = new ArrayList<>();

    // 리뷰 답변 매핑
    @OneToOne(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true) // 기본적으로 fetch = FetchType.LAZY
    private ReviewReplyEntity reply;

    // 파일(이미지) 추가
    public void addFiles(ReviewFileEntity entity, boolean isUpdate) {
        entity.setReview(this);
        fileList.add(entity);

        if (isUpdate) { // 만약 파일을 업데이트 했다면...
            this.preUpdate();  // review 의 updateDate 갱신
        }
    }

    // 리뷰 답변 추가
    public void addReply(ReviewReplyEntity entity) {
        this.reply = entity;
        entity.setReview(this);
    }
}
