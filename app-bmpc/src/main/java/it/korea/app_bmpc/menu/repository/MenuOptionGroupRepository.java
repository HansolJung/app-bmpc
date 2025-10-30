package it.korea.app_bmpc.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import it.korea.app_bmpc.menu.entity.MenuEntity;
import it.korea.app_bmpc.menu.entity.MenuOptionGroupEntity;
import jakarta.persistence.LockModeType;

public interface MenuOptionGroupRepository extends JpaRepository<MenuOptionGroupEntity, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<MenuOptionGroupEntity> findByMenuAndDelYnOrderByDisplayOrderAsc(MenuEntity menu, String delYn);
}
