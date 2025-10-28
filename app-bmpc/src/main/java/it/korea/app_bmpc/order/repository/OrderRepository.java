package it.korea.app_bmpc.order.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.korea.app_bmpc.order.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer>, JpaSpecificationExecutor<OrderEntity> {

    @EntityGraph(attributePaths = {"user", "itemList"})   // N+1 현상 해결
    Page<OrderEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"user", "itemList"})   // N+1 현상 해결
    Page<OrderEntity> findAll(Specification<OrderEntity> searchSpecification, Pageable pageable);

    // @EntityGraph(attributePaths = {
    //     "user", 
    //     "itemList",
    //     "itemList.menu",
    //     "itemList.menu.file",
    //     "itemList.itemOptionList",
    //     "itemList.itemOptionList.menuOption"
    // })   // N+1 현상 해결
    @Query(
        value = """
            select distinct o
            from OrderEntity o
            join fetch o.user u
            join fetch o.itemList i
            join fetch i.menu m
            left join fetch m.file f
            left join fetch i.itemOptionList io
            left join fetch io.menuOption mo
            where u.userId = :userId
            order by o.orderDate desc
        """,
        countQuery = """
            select count(distinct o)
            from OrderEntity o
            join o.user u
            where u.userId = :userId
        """
    )
    Page<OrderEntity> findAllByUser_userId(@Param("userId") String userId, Pageable pageable);

    //@EntityGraph(attributePaths = {"user", "itemList", "itemList.itemOptionList"})   // N+1 현상 해결
    @Query(
        value = """
            select distinct o
            from OrderEntity o
            join fetch o.store s
            join fetch o.user u
            join fetch o.itemList i
            join fetch i.menu m
            left join fetch m.file f
            left join fetch i.itemOptionList io
            left join fetch io.menuOption mo
            where s.storeId = :storeId
            order by o.orderDate desc
        """,
        countQuery = """
            select count(distinct o)
            from OrderEntity o
            join o.store s
            where s.storeId = :storeId
        """
    )
    Page<OrderEntity> findAllByStore_storeId(@Param("storeId") int storeId, Pageable pageable);

    // fetch join 사용해서 N + 1 문제 해결
    @Query("""
        select distinct o
        from OrderEntity o
        left join fetch o.itemList i
        left join fetch i.itemOptionList
        where o.orderId = :orderId
    """)
    Optional<OrderEntity> getOrder(@Param("orderId") int orderId);
}
