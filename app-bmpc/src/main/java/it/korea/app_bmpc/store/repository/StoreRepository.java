package it.korea.app_bmpc.store.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.korea.app_bmpc.store.entity.StoreEntity;

public interface StoreRepository extends JpaRepository<StoreEntity, Integer>, JpaSpecificationExecutor<StoreEntity> {

    @EntityGraph(attributePaths = {"fileList", "hourList", "categoryList.category"})   // N+1 현상 해결
    Page<StoreEntity> findAll(Specification<StoreEntity> searchSpecification, Pageable pageable);

    // fetch join 사용해서 N + 1 문제 해결
    // left join fetch s.menuCategoryList <-- subselect 로 해결
    // left join fetch mc.menuList m  <-- subselect 로 해결
    @Query("""
        select distinct s from StoreEntity s
        left join fetch s.categoryList cl
        left join fetch cl.category
        left join fetch s.hourList
        left join fetch s.fileList
        where s.storeId =:storeId
        and s.delYn = 'N'
        """)   
    Optional<StoreEntity> getStore(@Param("storeId") int storeId);
}
