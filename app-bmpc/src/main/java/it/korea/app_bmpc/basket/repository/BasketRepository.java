package it.korea.app_bmpc.basket.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import it.korea.app_bmpc.basket.entity.BasketEntity;

public interface BasketRepository extends JpaRepository<BasketEntity, Integer> {

    /**
     * 사용자 아이디 로 해당 사용자의 단일 장바구니 조회
     * (사용자는 오직 하나의 장바구니만 가짐)
     * N+1 문제를 방지하기 위해 필요한 연관 엔티티를 fetch join
     */
    @EntityGraph(attributePaths = {
        "user",
        "store",
        "itemList",
        "itemList.menu",
        "itemList.itemOptionList",
        "itemList.itemOptionList.menuOption"
    })
    Optional<BasketEntity> findByUser_userId(String userId);
}
