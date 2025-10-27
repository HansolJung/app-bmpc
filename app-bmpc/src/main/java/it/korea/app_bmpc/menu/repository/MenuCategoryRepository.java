package it.korea.app_bmpc.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.korea.app_bmpc.menu.entity.MenuCategoryEntity;

public interface MenuCategoryRepository extends JpaRepository<MenuCategoryEntity, Integer> {

}
