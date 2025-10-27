package it.korea.app_bmpc.favorite.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import it.korea.app_bmpc.favorite.entity.FavoriteStoreEntity;
import it.korea.app_bmpc.store.entity.StoreEntity;
import it.korea.app_bmpc.user.entity.UserEntity;

public interface FavoriteStoreRepository extends JpaRepository<FavoriteStoreEntity, Integer> {

    @EntityGraph(attributePaths = {"user", "store", "store.fileList"})  // N+1 현상 해결
    Page<FavoriteStoreEntity> findAllByUser_userId(String userId, Pageable pageable);

    boolean existsByUserAndStore(UserEntity user, StoreEntity store);

    Optional<FavoriteStoreEntity> findByUserAndStore(UserEntity user, StoreEntity store);
}
