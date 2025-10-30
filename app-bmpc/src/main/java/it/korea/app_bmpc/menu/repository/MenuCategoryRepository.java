package it.korea.app_bmpc.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import it.korea.app_bmpc.menu.entity.MenuCategoryEntity;
import it.korea.app_bmpc.store.entity.StoreEntity;
import jakarta.persistence.LockModeType;

public interface MenuCategoryRepository extends JpaRepository<MenuCategoryEntity, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<MenuCategoryEntity> findByStoreAndDelYnOrderByDisplayOrderAsc(StoreEntity store, String delYn);
}
