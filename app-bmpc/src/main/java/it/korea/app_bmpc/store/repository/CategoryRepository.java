package it.korea.app_bmpc.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.korea.app_bmpc.store.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

}
