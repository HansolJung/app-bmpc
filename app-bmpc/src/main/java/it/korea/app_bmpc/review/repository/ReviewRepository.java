package it.korea.app_bmpc.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.korea.app_bmpc.review.entity.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer>, JpaSpecificationExecutor<ReviewEntity> {

    @EntityGraph(attributePaths = {"user", "fileList", "reply", "reply.user"})   // N+1 현상 해결
    Page<ReviewEntity> findAllByStore_storeIdAndDelYn(int storeId, String delYn, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "fileList", "reply", "reply.user"})   // N+1 현상 해결
    Page<ReviewEntity> findAllByUser_userIdAndDelYn(String userId, String delYn, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "fileList", "reply", "reply.user"})  // N+1 현상 해결
    Page<ReviewEntity> findAll(Specification<ReviewEntity> searchSpecification, Pageable pageable);

    boolean existsByOrder_orderId(int orderId);

    @Query("select avg(r.rating) from ReviewEntity r where r.store.storeId = :storeId and r.delYn = 'N'")
    Double findRatingAvgByStoreId(@Param("storeId") int storeId);

    Long countByStore_storeIdAndDelYn(int storeId, String delYn);
}
