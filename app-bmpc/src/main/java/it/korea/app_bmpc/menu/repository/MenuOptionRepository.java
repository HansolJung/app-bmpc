package it.korea.app_bmpc.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.korea.app_bmpc.menu.entity.MenuOptionEntity;

public interface MenuOptionRepository extends JpaRepository<MenuOptionEntity, Integer> {

}
